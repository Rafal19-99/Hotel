package dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface HotelDao {

	public Byte guestBookRoom(String name, String surname, byte typeOfRoom, LocalDate checkIn, LocalDate CheckOut);
	
	public void add(Object serializableObject);
	public void update(Object serializableObject);
	public void delete(Object serializableObject, Object id);
	public void delete(Object serializableObject);
	
	
	public void removeGuestWhoCheckedOutMinimumWeekAgo();
	public void updateUrgencyOfRoomToBeCleaned();
	
	public  List<?> getAll( Object serializableObject);
	public Object findById(Object serializableObject, Object id);
	public Object findByProprietes(Object serializableObject, Object id, Map<String, Object> propretes);

	
	
	 
}
