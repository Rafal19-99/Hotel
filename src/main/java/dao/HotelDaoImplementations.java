package dao;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Stateless

public class HotelDaoImplementations implements HotelDao{

    @PersistenceContext
    private  EntityManager entityManager;
	
	
	
	@Override
	public Byte guestBookRoom(String name, String surname, byte typeOfRoom, LocalDate checkIn, LocalDate checkOut) {
		if( checkIn.isAfter(checkOut))
			return 2;

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("AddGuestAndReserveTheRoom")
        		.registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
        		.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
        		.registerStoredProcedureParameter(3, Byte.class, ParameterMode.IN)
        		.registerStoredProcedureParameter(4, LocalDate.class, ParameterMode.IN)
        		.registerStoredProcedureParameter(5, LocalDate.class, ParameterMode.IN)
        		.registerStoredProcedureParameter(6, Byte.class, ParameterMode.OUT)
        		.setParameter(1, name)
        		.setParameter(2, surname)
        		.setParameter(3, typeOfRoom)
        		.setParameter(4, checkIn)
        		.setParameter(5, checkOut);
        
 
       	query.execute();

        Byte result = (Byte) query.getOutputParameterValue(6);
		return result;
	}



	@Override
	public void removeGuestWhoCheckedOutMinimumWeekAgo() {
		 StoredProcedureQuery query = entityManager.createStoredProcedureQuery("RemoveGuestWhoCheckedOutMinimumWeekAgo");
		 query.executeUpdate();
	}



	@Override
	public void updateUrgencyOfRoomToBeCleaned() {
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UpdateUrgencyInCleaning");
		 query.executeUpdate();
	}



	@Override
	public void add(Object serializableObject) {
		if(serializableObject instanceof  Serializable ) {
			entityManager.persist(serializableObject);
		}
		
	}



	@Override
	public void update(Object serializableObject) {
		if(serializableObject instanceof  Serializable ) {
			entityManager.merge(serializableObject);
			
		}
		
	}



	@Override
	public List<?> getAll(Object serializableObject) {
		
		if(serializableObject instanceof Serializable) {
			
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			
			cb.createQuery(serializableObject.getClass());
			CriteriaQuery<?> criteria = cb.createQuery(serializableObject.getClass());
	         criteria.from(serializableObject.getClass());
	         return entityManager
	        		 .createQuery(criteria)
	        		 .getResultList();

		}
		return Collections.emptyList();
	}



	@Override
	public Object findById(Object serializableObject, Object id) {
		if(serializableObject instanceof Serializable) {
			return entityManager.find(serializableObject.getClass(), id);
		}
		return null;
	}



	@Override
	public void delete(Object serializableObject, Object id) throws NullPointerException, IllegalArgumentException{
			entityManager.remove(this.findById(serializableObject, id));
		
	}
	
	@Override
	public void delete(Object serializableObject) throws NullPointerException{
		if(serializableObject instanceof Serializable)
			entityManager.remove(serializableObject);
		
	}




	@Override
	public Object findByProprietes(Object serializableObject, Object id, Map<String, Object> propretes) {
		if(serializableObject instanceof Serializable) {
			return entityManager.find(serializableObject.getClass(), id,propretes);
		}
		return null;
	}





}
