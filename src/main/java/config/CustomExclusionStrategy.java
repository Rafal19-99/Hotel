package config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import model.Booking;
import model.Employee;
import model.Guest;
import model.User;

public class CustomExclusionStrategy implements ExclusionStrategy{

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return (
				(f.getDeclaringClass()==User.class && f.getName().equals("password"))
				|| (f.getDeclaringClass()==Employee.class && f.getName().equals("id"))
				|| (f.getDeclaringClass()==Guest.class && f.getName().equals("id"))
				|| (f.getDeclaringClass()==Booking.class && f.getName().equals("hasCheckedOut"))
				|| (f.getDeclaringClass()==Booking.class && f.getName().equals("hasCheckedIn"))
				|| (f.getDeclaringClass()==Booking.class && f.getName().equals("tiemeOdCheckOut"))
				);

	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		
		return false;
	}
	


}


