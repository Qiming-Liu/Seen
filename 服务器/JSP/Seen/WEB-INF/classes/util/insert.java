package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class insert {
	public static void main(String[] args) {
		 Connection conn = null;
		 PreparedStatement stmt= null;
		 
		 
		 try {
			conn = DBHelper.getConnection();
			String sql = "insert into Bottle(bottleID,throwID,content,type,longitude,latitude,adress,time,isVisual,title) values(?,?,?,?,?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, "awkow");
			stmt.setString(2, "awewae");
			stmt.setString(3, "awl113");
			stmt.setInt(4, 1);
			stmt.setString(5, null);
			stmt.setString(6, null);
			stmt.setString(7, null);
			stmt.setString(8, null);
			stmt.setBoolean(9, true);
			stmt.setString(10, "aaaa");
			stmt.executeUpdate();
			System.out.println("aaaaaa");
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
