package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
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
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import model.Cleaning;
import model.Employee;
import model.User;


@RolesAllowed("Manager")
@Stateless
@Path("/manage")
public class ManagerServlet {


	@Inject
    private HotelDao hotelDao;
	
	 Gson gson = new GsonBuilder()
		        .setExclusionStrategies(new CustomExclusionStrategy())
		        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
		        .create();
	 
	 	private final int NUMBER_OF_ITERATIONS_IN_ARGON2 = 8 ;
		private final int NUMBER_OF_THREADS_IN_ARGON2 = 4;  
		private final int KB_OF_MEMORY_IN_ARGON2 = 4096;
	
	 //DELETE methods
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response fireEmployee(@Size(min=5, max=50,message="Login not in lenght of 5-50")@PathParam("id") String login) 
				throws ServletException, IOException{
		User user = (User) hotelDao.findById(new User(), login);
		Employee def = (Employee) hotelDao.findById(new Employee(), 1);
		try{

			
			@SuppressWarnings("unchecked")
			List<Cleaning> result = (List<Cleaning>) hotelDao.getAll(new Cleaning());
			result = result.stream()
						.filter(n->n.getEmployee().equals(user.getEmployee()))
						.collect(Collectors.toList());
			
			for(Cleaning clean:result) {
				if(clean.getStatus().equals("inprocess")) {
					clean.setEmployee(def);
					clean.setStatus("none");
					continue;
				}
				hotelDao.delete(clean);
			}
			
			hotelDao.delete(user);
			hotelDao.delete(user.getEmployee());
			
			
		}catch(NullPointerException e) {
			return Response.ok().entity("Eployee dosen't exist").build();
		}
		
		return Response.ok().entity("success").build();
	}
	
	//GET methods
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response allEmployess() throws ServletException {
		
		@SuppressWarnings("unchecked")
		List<User> allEmployee =  (List<User>) hotelDao.getAll(new User());
		return Response.ok(gson.toJson(allEmployee)).build();

	}

   //POST methods
	@POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response  addEmployee(
		@Size(min=1, max=30,message="Name not in lenght of 1-30") @FormParam("name")  String name,
		@Size(min=1, max=30,message="Surname not in lenght of 1-30") @FormParam("surname")  String surname,
		@Size(min=5, max=50,message="Login not in lenght of 5-50") @FormParam("login")  String login,
		@Size(min=8, max=20,message="Password not in lenght of  8-20") @FormParam("password")  String password,
		@Email(message="This is not a valid email") @FormParam("email")  String email,
		@Pattern(regexp ="Manager|Receptionist|Cleaner", message =" Wrong positions")@FormParam("position")  String position,
		@Context HttpServletRequest request,
		@Context HttpServletResponse response) throws ServletException, IOException{

		Employee emp = Employee.builder()
				.name(name)
				.surname(surname)
				.position(position)
				.build();
				
		hotelDao.add(emp);
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		password= argon2.hash(NUMBER_OF_ITERATIONS_IN_ARGON2,KB_OF_MEMORY_IN_ARGON2, NUMBER_OF_THREADS_IN_ARGON2,  password.toCharArray());
		
		User user = User.builder()
			.login(login)
			.email(email)
			.password(password)
			.employee(emp)
			.build();
		
		hotelDao.add(user);
		return Response.ok().entity(gson.toJson(user)).build();
		
	}
}
