package model;

import java.io.Serializable;
import javax.persistence.*;

import lombok.*;


@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements Serializable{

	private static final long serialVersionUID = 7934082089167418283L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column( name= "name")
	private String name;
	
	@Column( name= "surname")
	private String surname;
	
	@Column( name= "position")
	private String position;
	



}
