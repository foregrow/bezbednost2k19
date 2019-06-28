package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private int id;
	private String email;
	private String password;
	private String Certificate;
	private boolean active;
	private Authority authority;
	
	public User(){
		
	}

	public User(int id, String email, String password, String certificate,
			boolean active, Authority authority) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.Certificate = certificate;
		this.active = active;
		this.authority = authority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCertificate() {
		return Certificate;
	}

	public void setCertificate(String certificate) {
		Certificate = certificate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
	
}
