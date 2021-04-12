package servlet;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.CustomExclusionStrategy;
import dao.HotelDao;
import model.Cleaning;
import model.User;

@RolesAllowed({"Manager","Receptionist","Cleaner"})
@Stateless
@Path("/clean")
public class CleaningServlet {

	@Inject
	private HotelDao hotelDao;
	Gson gson = new GsonBuilder()
	        .setExclusionStrategies(new CustomExclusionStrategy())
	        .create();
	
	
	
	
	//GET methods
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfRoomToClean() {
		
		@SuppressWarnings("unchecked")
		List<Cleaning > result = (List<Cleaning>) hotelDao.getAll(new Cleaning());
		
			result=	result.stream()
					.filter(c-> c.getStatus().equalsIgnoreCase("none"))
					.sorted(Comparator.comparing(c -> c.getUrgency()))
					.collect(Collectors.toList());
				
			return Response.ok()
					.entity(gson.toJson(result))
					.build();
		
	}
	
	@GET
	@Path("/update/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response startCleaningByEmployee(
			@Min(0) @PathParam("id") int cleaningId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		
		Cleaning cleaning = (Cleaning) hotelDao.findById(new Cleaning (), cleaningId);
		
		User user = (User) hotelDao.findById(new User(), request.getRemoteUser());
		
		try{
			if(cleaning.getEmployee().getId()!=1) {
				return Response.ok().entity("Somebody else has taken it").build();
			}
			cleaning.setEmployee(user.getEmployee());
			cleaning.setStatus("inprocess");
		}catch(NullPointerException e) {
			return Response.ok().entity("Fail").build();
		}
		return  Response.ok("success").build();
	}
	
	
	@GET
	@Path("/finish/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response finishCleaningByEmployee(
			@Min(0) @PathParam("id") int cleaningId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		
		Cleaning cleaning = (Cleaning) hotelDao.findById(new Cleaning (), cleaningId);
		
		User user = (User) hotelDao.findById(new User(), request.getRemoteUser());
		
		try{
			if(!cleaning.getEmployee().equals(user.getEmployee())) {
				return Response.ok().entity("This room is cleaned by somebody else").build();
			}
			if(!cleaning.getStatus().equalsIgnoreCase("inprocess")) {
				return Response.ok().entity("This room is alredy cleaned").build();
			}
			cleaning.setEmployee(user.getEmployee());
			cleaning.setStatus("finished");
		}catch(NullPointerException e) {
			return Response.ok().entity("Fail").build();
		}
		return  Response.ok("success").build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/active")
	public Response getMyHistory(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		
		User user = (User) hotelDao.findById(new User(), request.getRemoteUser());
		@SuppressWarnings("unchecked")
		List<Cleaning> result = (List<Cleaning>) hotelDao.getAll(new Cleaning());
		
		result = result.stream()
				.filter(n-> n.getEmployee().equals(user.getEmployee())
						&& n.getStatus().equalsIgnoreCase("inprocess")
				)
				.collect(Collectors.toList());
		
		return Response.ok()
				.entity(gson.toJson(result))
				.build();
		
	}
	
	
	@RolesAllowed("Manager")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/historyOfCleaning/{id}")
	public Response getCleanigHistory(@NotNull(message="Login is null!") @PathParam("id") String login) {
		
		User user = (User) hotelDao.findById(new User(), login);
		@SuppressWarnings("unchecked")
		List<Cleaning> result = (List<Cleaning>) hotelDao.getAll(new Cleaning());
		
		result.stream()
			.filter(n-> n.getEmployee().equals(user.getEmployee()))
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(gson.toJson(result))
				.build();

		
	}
	
	
}
