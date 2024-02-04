package com.akash.model;

public class StudentResponse {
	private Long id;
	private String firstname;
	private String lastName;
	private String email;

	public StudentResponse() {
		super();
	}

	public StudentResponse(Long id, String firstname, String lastName, String email) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastName = lastName;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "StudentResponse [id=" + id + ", firstname=" + firstname + ", lastName=" + lastName + ", email=" + email
				+ "]";
	}

}
