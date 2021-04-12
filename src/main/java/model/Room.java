package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@NoArgsConstructor
@Data
@AllArgsConstructor

public class Room implements Serializable {

	private static final long serialVersionUID = -1070227842563924265L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column( name= "floor")
	private int floor;
	
	@Column( name= "type")
	private byte numberOfPeople ;


}
