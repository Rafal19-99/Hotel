package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name ="guest")
public class Guest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column( name = "name")
	private String name;
	
	@Column( name = "surname")
	private String surname;
	
	@Transient
	@OneToOne(mappedBy="guest")
	private Booking booking;
	


}
