package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor

public class Booking implements Serializable {

	private static final long serialVersionUID = 7281864782720993816L;
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name="roomId")
	private Room room;
	
	@OneToOne
	@JoinColumn(name = "guestId")
	private Guest guest;
	

	@Column(name = "startDate")
	private LocalDate dateOfCheckIn ;
	
	@Column(name = "endDate")
	private LocalDate dateOfCheckOut ;
	
	
	@Column(name = "hasCheckedIn")
	private Boolean hasCheckedIn;
	
	@Column(name = "hasCkeckedOut")
	private Boolean hasCheckedOut;
	

	@Column(name = "timeOfCheckOut", nullable = true,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	LocalDateTime tiemeOdCheckOut;


	
	
}

