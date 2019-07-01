package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Authority;
import dto.User;



public class UserDAO {

	public static List<User> getAllUsers() {
		List<User> korisnici = new ArrayList<>();

		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * "
					+ "FROM users";

			pstmt = conn.prepareStatement(query);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				String email = rset.getString(index++);
				String password = rset.getString(index++);
				String cert = rset.getString(index++);
				boolean active = rset.getBoolean(index++);
				int auth = rset.getInt(index++);

				User user = new User();
				user.setId(id);
				user.setEmail(email);
				user.setPassword(password);
				user.setCertificate(cert);
				user.setActive(active);
				Authority a = new Authority();
				a.setId(auth);
				user.setAuthority(a);
				
				korisnici.add(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return korisnici;
	}
	
	public static boolean add(String email, String pw, String cert, boolean active, int authority) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO users (email, pw, certificate, active, authority) "
					+ "VALUES (?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			
			pstmt.setString(index++, email);
			pstmt.setString(index++, pw);
			pstmt.setString(index++, cert);
			pstmt.setBoolean(index++, active);
			pstmt.setInt(index++, authority);
			System.out.println(pstmt);

			return pstmt.executeUpdate() == 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return false;
	}
	
	public static boolean update(String email) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE users SET active = 1 WHERE email = ?";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, email);
			System.out.println(pstmt);

			return pstmt.executeUpdate() == 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return false;
	}
	
}
