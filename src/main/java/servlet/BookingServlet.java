package servlet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.CustomExclusionStrategy;
import config.LocalDateAdapter;
import dao.HotelDao;
import model.Booking;
import model.Cleaning;
import model.Employee;

@Stateless
@Path("/booking")
@PermitAll
public class BookingServlet {

	@Inject
    private HotelDao hotelDao;
	
	private Gson gson = new GsonBuilder()
	        	.setExclusionStrategies(new CustomExclusionStrategy())
	        	.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
	        	.create();
	
	
// POST method
	@POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response  addBooking(
			@Size(min=1, max=30,message="Name not in lenght of 1-30")@FormParam("name")  String name,
			@Size(min=1, max=30,message="Surname not in lenght of 1-30")@FormParam("surname")  String surname,
			@Min(1) @Max(5)@FormParam("type")  Byte typeOfRoom,
			@NotBlank(message="You don't filled date of checkin")@FormParam("checkIn")   String checkIn,
			@NotBlank(message="You don't filled date of checkout")@FormParam("checkOut")  String checkOut,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			@FutureOrPresent(message="Wrong date ") LocalDate checkI =  LocalDate.parse(checkIn, formatter);
			@FutureOrPresent(message="Wrong date  ") LocalDate checkO =  LocalDate.parse(checkOut, formatter);
			
			Byte result = hotelDao.guestBookRoom(name, surname, typeOfRoom, checkI, checkO);
			return Response.ok()
					.entity(result.toString())
					.build();
			
		}catch(DateTimeParseException e) {
			return Response.ok("Wrong format of date ").build();
		}
	}
	
	@Path("/changeDate")
	@RolesAllowed({"Manager","Receptionist"})
	@POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response changeBookingDate(@Min(1) @FormParam("id")  int id,
			@NotBlank(message="You don't filled date of checkin")@FormParam("newCheckIn")   String checkIn,
			@NotBlank(message="You don't filled date of checkout")@FormParam("newCheckOut")  String checkOut,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response){
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		@FutureOrPresent(message="Wrong date ") LocalDate newCheckIn =  LocalDate.parse(checkIn, formatter);
		@FutureOrPresent(message="Wrong date  ") LocalDate newCheckOut =  LocalDate.parse(checkOut, formatter);
		

		
		if(newCheckIn.isAfter(newCheckOut))
			return Response.ok("Date of checkIn is after date of checkOut").build();


		Booking booking = (Booking) hotelDao.findById(new Booking(), id);
		if(booking == null)
			return Response.ok("Wrong id").build();
		if(booking.getHasCheckedIn() && !booking.getDateOfCheckIn().isEqual(newCheckIn))
			return Response.ok("You can't change date of checkin if you alredy chekced in").build();
		if(booking.getHasCheckedOut())
			return Response.ok("You alredy checked out you can't change date of booking").build();

		@SuppressWarnings("unchecked")
		List<Booking> bookingList= (List<Booking>) hotelDao.getAll(new Booking());

		 bookingList = bookingList
							.stream()
							.filter(b->b.getRoom().equals(booking.getRoom()) && b.getHasCheckedOut()==false)
							.collect(Collectors.toList());
		if(bookingList.isEmpty()) return Response.ok().entity("Empty list").build();
		bookingList.remove(booking);
									
		
		for(Booking book :bookingList) {
			if(    booking.getDateOfCheckIn().isBefore(book.getDateOfCheckOut().minusDays(1)) 
			   &&  booking.getDateOfCheckOut().isAfter(book.getDateOfCheckIn().plusDays(1))
			) 	return Response.ok("You can't change booking date to this date ").build();
		}

		
		booking.setDateOfCheckIn(newCheckIn);
		booking.setDateOfCheckOut(newCheckOut);
		return Response.ok("success").build();
		
		
	
	}
	
	
// DELETE method
	@RolesAllowed({"Manager","Receptionist"})
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteBooking(@Min(1) @PathParam("id") int id) {
		Booking booking = (Booking) hotelDao.findById(new Booking(), id);
		try{
			booking.setHasCheckedIn(true);
			booking.setHasCheckedOut(true);
			
		}catch(NullPointerException e) {
			return Response.ok().entity("Booking with this id dosen't exist").build();
		}
		
		return  Response.ok("success").build();
	}
	
	@Path("/checkIn")
	@RolesAllowed({"Manager","Receptionist"})
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getBookingListOfNotCheckedIn() {
		
		@SuppressWarnings("unchecked")
		List<Booking> result = (List<Booking>) hotelDao.getAll(new Booking());
		
		result = result.stream()
				.filter(b-> b.getHasCheckedIn()==false)
				.sorted(Comparator.comparing(b -> b.getDateOfCheckIn()))
				.collect(Collectors.toList());
		

		return Response.ok()
				.entity(gson.toJson(result))
				.build();
		
	}
	
	
	//GET METHODS
	@Path("/checkOut")
	@RolesAllowed({"Manager","Receptionist"})
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public Response getBookingListOfCheckedInButNotCheckedOut() {
		
		@SuppressWarnings("unchecked")
		List<Booking> result = (List<Booking>) hotelDao.getAll(new Booking());
		
		result = result.stream()
				.filter(b-> b.getHasCheckedIn()==true && b.getHasCheckedOut()==false)
				.sorted(Comparator.comparing(b -> b.getDateOfCheckOut()))
				.collect(Collectors.toList());

		return Response.ok()
				.entity(gson.toJson(result))
				.build();
		
	}
	
	@Path("/checkIn/{id}")
	@GET
	@RolesAllowed({"Manager","Receptionist"})
	@Produces(MediaType.TEXT_PLAIN)
	public Response checkIn(@Min(0) @PathParam("id")int id) {
		
		Booking booking = (Booking) hotelDao.findById(new Booking(), id);
		
		try {
			if( booking.getHasCheckedIn()==true)
				return Response.ok().entity("Guest alredy checkedin").build();
			
			booking.setHasCheckedIn(true);
		}catch(NullPointerException e) {
			return Response.ok().entity("Booking with this id dosen't exist").build();
		}
		return Response.ok("success").build();
	
		
	}
	
	
	@Path("/checkOut/{id}")
	@GET
	@RolesAllowed({"Manager","Receptionist"})
	@Produces(MediaType.TEXT_PLAIN)
	public Response checkOut(@Min(0) @PathParam("id")int id) {
		
		Booking booking = (Booking) hotelDao.findById(new Booking(), id);
		try {
			if( booking.getHasCheckedOut()==true ||booking.getHasCheckedIn()==false)
				return Response.ok().entity("Guest alredy checked out or hasn't checkedin yet").build();
		}catch(NullPointerException e) {
			return Response.ok().entity("Booking with this id dosen't exist").build();
		}
		
		
		booking.setHasCheckedOut(true);
		Cleaning cleanig = Cleaning
				.builder()
				.room(booking.getRoom())
				.status("none")
				.urgency("normal")
				.employee((Employee) hotelDao.findById(new Employee(), 1))
				.build();
		
		hotelDao.add(cleanig);
		return Response.ok("success").build();
	
		
		
	}
	
	@Path("/late")
	@GET
	@RolesAllowed("Manager")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookingListOfLateGuest() {
		
		@SuppressWarnings("unchecked")
		List <Booking> result = (List<Booking>) hotelDao.getAll(new Booking());
	

		result = result.stream()
					.filter(n ->
						  (n.getHasCheckedIn()==false && n.getDateOfCheckIn().isBefore(LocalDate.now())) 
						||( n.getHasCheckedOut()==false && n.getDateOfCheckOut().isBefore(LocalDate.now()))
					)
					.sorted(Comparator.comparing(b -> b.getDateOfCheckIn()))
					.collect(Collectors.toList());
		
		return Response.ok()
					.entity(gson.toJson(result))
					.build();
	}
	
	@Path("/all")
	@GET
	@RolesAllowed("Manager")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookingListOfGuestNotCheckeOut() {
		
		@SuppressWarnings("unchecked")
		List <Booking> result = (List<Booking>) hotelDao.getAll(new Booking());
	

		result = result.stream()
					.filter(b -> b.getHasCheckedOut()==false )
					.sorted(Comparator.comparing(b -> b.getDateOfCheckIn()))
					.collect(Collectors.toList());
		
		return Response.ok()
					.entity(gson.toJson(result))
					.build();
	}
	
	
	
	
	
}
