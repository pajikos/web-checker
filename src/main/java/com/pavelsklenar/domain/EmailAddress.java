package com.pavelsklenar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity to save email addresses within DB
 * @author pavel.sklenar
 *
 */
@Entity
public class EmailAddress {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String emailAddress;

	@Column
	private String name;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

}
