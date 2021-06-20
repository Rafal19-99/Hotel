package servlet;

import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import config.CustomExclusionStrategy;
import dao.HotelDao;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import model.User;


@RolesAllowed({"Manager","Receptionist","Cleaner"})
@Stateless
@Path("/user")
public class UserServlet {

	@Inject
	private HotelDao hotelDao;
	
	Gson gson = new GsonBuilder()
	        .setExclusionStrategies(new CustomExclusionStrategy())
	        .create();
	
	private final int NUMBER_OF_ITERATIONS_IN_ARGON2 = 8 ;
	private final int NUMBER_OF_THREADS_IN_ARGON2 = 4;  
	private final int KB_OF_MEMORY_IN_ARGON2 = 4096;
	
	
	//GET methods
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMyDetails(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)throws ServletException, IOException {
		
		User user = (User) hotelDao.findById(new User(), request.getRemoteUser());
		return Response.ok().entity(gson.toJson(user)).build();	
		
	}
	
	
	@GET
	@Path("/logout")
	public Response logout(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response)throws ServletException{
		
		request.getSession().invalidate();
		return Response.ok().build();
		
	}
	
	
	//POST methods
	@POST
	@Path("/changeMyPassword")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changeMyPassword(
			@Size(min=8, max=20,message="Password not in lenght of  8-20") @FormParam("newPassword") String newPassword,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws ServletException, IOException{
		
			
		return (changePassword(request.getRemoteUser(),newPassword)==0)?
				Response.ok().entity("Fail").build(): Response.ok("success").build();
		}
	
	
	@RolesAllowed("Manager")
	@POST
	@Path("/changePassword")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changePasswordByManager(
			@Size(min=5, max=50,message="Login not in lenght of 5-50") @FormParam("log")  String login,
			@Size(min=8, max=20,message="Password not in lenght of  8-20") @FormParam("newPassword")  String newPassword
			)  throws ServletException, IOException{

		return  (changePassword(login,newPassword)==0)?
				Response.ok().entity("Fail").build(): Response.ok("success").build();
			
		}
	
	// private methods
	private Byte changePassword ( String login, String newPassword) {
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		User user = (User) hotelDao.findById(new User(), login);
		if(user==null) {
			return 0;
		}

		user.setPassword(argon2.hash(NUMBER_OF_ITERATIONS_IN_ARGON2,KB_OF_MEMORY_IN_ARGON2, NUMBER_OF_THREADS_IN_ARGON2,  newPassword.toCharArray()));
		return 1;
	}
	
	
}
