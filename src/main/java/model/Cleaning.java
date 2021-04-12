package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cleaning")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Cleaning implements Serializable {

	private static final long serialVersionUID = -4023202328572358166L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name ="roomId")
	private Room room;
	
	@ManyToOne          
	@JoinColumn(name = "employeeId")
	private Employee employee;
	
	@Column(name = "urgency")
	private String urgency;
	
	@Column(name = "status")
	private String status;


	

}
