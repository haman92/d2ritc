package com.blizzard.d2ritemauction;



import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import javax.persistence.*;

@Data
@Table(name = "User")
@Entity
@Getter
public class User {

	/*
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	*/
	@Id
	@Column(nullable = false)
	private String battletag;
	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	public User()
	{
		
	}
	@Builder
	public User(Long id, String battletag, Role role)
	{
		//this.id = id;
		this.battletag=battletag;
		this.role = role;
	}
	@Builder
	public User(String battletag, Role role)
	{
		this.battletag=battletag;
		this.role = role;
	}
}
