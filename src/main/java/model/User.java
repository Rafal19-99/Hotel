package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User  implements Serializable{
	
	
	private static final long serialVersionUID = 2366037268182398919L;


	
	@Id
	@Column( name = "login", unique = true)
	private String login;
	
	@Column( name = "password")
	private String password;
	
	@Column( name = "email", unique = true)
	private String email;
	
	@OneToOne
	@JoinColumn(name = "employeeId")
	private Employee  employee;
	
	
}
