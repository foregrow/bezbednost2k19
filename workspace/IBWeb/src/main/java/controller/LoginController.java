package controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dao.UserDAO;
import dto.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import util.ApiToken;
import util.Certificate;
import util.Issuer;
import util.KeyStoreWriter;
import util.PasswordCrypt;
import util.Subject;


@Controller
public class LoginController {

	
	@GetMapping("/")
	public static String loginPage(Model model) {
		return "Login";
	}
	
	@GetMapping("/login")
	public static String login(Model model, @RequestParam("userName") String username, @RequestParam("password") String password) {
		System.out.println("email:" + username);
		System.out.println("password:" + password);
		PasswordCrypt pc = null;
		try {
			pc = new PasswordCrypt();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<User> users = UserDAO.getAllUsers();
		boolean successful = false;
		String decrypted="";
		for(User u: users) {
			if(u.isActive()) {
				if(pc != null) {
					decrypted = pc.decrypt(u.getPassword());
					if(!decrypted.equals("")) {
						if(username.equals(u.getEmail()) && password.equals(decrypted)) {
							successful = true;
							ApiToken.user = u;
							break;
						}
					}		
				}
			}
			
			
		}	
		if(successful) {
				
			ApiToken.tokken = new ResponseEntity<ApiToken>(
					new ApiToken(Jwts.builder().setSubject(username).claim("role", "user")
							.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256,"123#&*zcvAWEE999").compact()),
					HttpStatus.OK);
			return "uploadview";
		}
		else {
			return "Login";
		}		
	}
	
	@GetMapping("/logout")
	public static String logout() {
		ApiToken.tokken = null;
		return "Login";
	}
	
	@GetMapping("/reg")
	public static String regPage() {
		return "Register";
	}
	
	@PostMapping("/register")
	public static String register(@RequestParam("userName") String username,@RequestParam("password") String password) {
		
		List<User> users = UserDAO.getAllUsers();
		List<String> emails = new ArrayList<String>();
		for(User u : users) {
			emails.add(u.getEmail());
		}
		if(emails.contains(username)) {
			
			return "register";
		}
		
		PasswordCrypt pc = null;
		try {
			pc = new PasswordCrypt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String enc = "";
        if(pc!= null) {
        	enc=pc.encrypt(password);
        }
        	
		folders(username);
		String cert = "";
		if (jks(username, enc)) {
			cert = "aa.jks";
		}
		
		UserDAO.add(username, enc, cert, false, 1);
		
		return "Login";
	}
	
	@GetMapping("/manage")
	public static String manage() {
		if(ApiToken.tokenActive()) {
			return "Managing";
		}
		else {
			return "Login";
		}
		
	}
	
	@PostMapping("/managing")
	public static String managingUsers(@RequestParam("email") String email) {
		if(ApiToken.user.getAuthority().getId() == 2) {
			
		}
		List<User> users = UserDAO.getAllUsers();
		
		for(User u : users) {
			if(!u.isActive()) {
				if(email.equals(u.getEmail())) {
					UserDAO.update(email);
					break;
				}
			}
		}
		
		return "Managing";
	}
	
		
	private static void folders(String email) {

		Path base = Paths.get("data/" + email);
		Path imgs = Paths.get("data/" + email + "/zip");
		
		try {
			Files.createDirectory(base);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			Files.createDirectory(imgs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static boolean jks(String email,String pass) {
		try {
			Certificate gen = new Certificate();
			KeyPair keyPair = gen.generateKeyPair();
			
			Calendar calendar = new GregorianCalendar();
			Date startDate = calendar.getTime();
			calendar.add(Calendar.YEAR, 2);
			Date endDate = calendar.getTime();
			
			
			char[] password = pass.toCharArray();

			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.CN, email);
		    builder.addRDN(BCStyle.SURNAME, "");
		    builder.addRDN(BCStyle.GIVENNAME, "");
		    builder.addRDN(BCStyle.O, "ChocMail");
		    builder.addRDN(BCStyle.OU, "ChocMail");
		    builder.addRDN(BCStyle.C, "RS");
		    builder.addRDN(BCStyle.E, email);
		    builder.addRDN(BCStyle.UID, "123445");
			
			String sn = "1";

			Issuer issuerData = new Issuer(keyPair.getPrivate(), builder.build());
			Subject subjectData = new Subject(keyPair.getPublic(), builder.build(), sn, startDate, endDate);
			
			X509Certificate cert = gen.generateCertificate(issuerData, subjectData);
			
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			keyStoreWriter.loadKeyStore(null, password);
			keyStoreWriter.write(email, keyPair.getPrivate(), password, cert);
			keyStoreWriter.saveKeyStore("data/" + email + "/aa.jks", password);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
}

