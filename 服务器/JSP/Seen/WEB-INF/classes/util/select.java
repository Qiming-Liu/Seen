package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class select {
	static String aq = "a888";
	public static void main(String[] args) {
		 Connection conn = null;
		 PreparedStatement stmt= null;
		 ResultSet rs= null;
		 
		 String content;
		 String throwID;
		 String adress;
		 
		 try {
			conn = DBHelper.getConnection();
			String sql = "select * from Bottle where throwID='"+aq+"'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				throwID = rs.getString("throwID");
				content = rs.getString("content");
				System.out.println(content);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
