package util;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;

import dto.User;

public class ApiToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static ResponseEntity<ApiToken> tokken = null;
	public static User user = null;
	
	private String token;
	
	public ApiToken(String token) {
		super();
		this.token = token;
	}
	
	public ApiToken() {
		
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public static boolean tokenActive() {
		if(tokken != null) {
			return true;
		}
		return false;
	}

}
