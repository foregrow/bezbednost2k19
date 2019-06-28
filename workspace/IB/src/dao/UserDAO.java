package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserDAO {

	public static List<User> getAllUsers() {
		List<User> korisnici = new ArrayList<>();

		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT email, pw "
					+ "FROM users";

			pstmt = conn.prepareStatement(query);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String email = rset.getString(index++);
				String password = rset.getString(index++);

				User user = new User();
				user.setEmail(email);
				user.setPassword(password);
				
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
	
}
