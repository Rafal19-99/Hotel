package config;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import dao.HotelDao;

@Singleton
@Startup
public class Scheduler {
	
	@Inject
	private HotelDao hotelDao;
	
	@Schedule(minute="01",hour="0",dayOfWeek="*")
	public void updateUrgencyOfRoomToBeCleaned() {
		hotelDao.updateUrgencyOfRoomToBeCleaned();
	}
	
	@Schedule(minute="01",hour="0", dayOfWeek="0")
	public void deleteGuestWhoCheckedOutMinimumWeekAgo() {
		hotelDao.removeGuestWhoCheckedOutMinimumWeekAgo();
	}
}
