package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;




import entity.LoginBean;
import util.DBHelper;


public class LoginDAO {
	
		//获取所有登录信息
		public ArrayList<LoginBean> getAllItems(){
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ArrayList<LoginBean> list = new ArrayList<LoginBean>();//登录信息集合
			try {
				conn = DBHelper.getConnection();
				String sql = "select * from Login;";	//SQL语句
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				while(rs.next()){
					LoginBean login = new LoginBean();
					login.setUserID(rs.getString("userID"));
					login.setPassword(rs.getString("password"));
					
					list.add(login);	//把一个人的信息加入集合
				}
				return list;//返回集合
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null; 
			} finally{
				//释放数据集对象
				if(rs != null){
					try {
						rs.close();
						rs = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//释放语句对象
				if(stmt != null){
					try {
						stmt.close();
						stmt = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		//根据ID查找信息
		public LoginBean getLoginBeanById(String id) throws SQLException{
			Connection conn = null;
			PreparedStatement stmt = null;
			//stmt.setString(1,id);
			ResultSet rs = null;
			try {
				conn = DBHelper.getConnection();
				String sql = "select * from Login where userID=";	//SQL语句
				stmt = conn.prepareStatement(sql+"'"+id+"'");
				System.out.println(sql+"'"+id+"'");
				rs = stmt.executeQuery();
				if(rs.next()){
					LoginBean login = new LoginBean();
					login.setUserID(rs.getString("userID"));
					login.setPassword(rs.getString("password"));
					login.setNickname(rs.getString("nickname"));
					login.setSex(rs.getBoolean("sex"));
					login.setToken(rs.getString("token"));
					login.setCicleimage(rs.getString("headImage"));
					login.setSignature(rs.getString("signature"));
				
					return login;
				}else{
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null; 
			} finally{
				//释放数据集对象
				if(rs != null){
					try {
						rs.close();
						rs = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//释放语句对象
				if(stmt != null){
					try {
						stmt.close();
						stmt = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		public boolean addUser(String userID,String password,String token) throws SQLException{
			Connection conn = null;
			Statement stmt = null;

			try {
				conn = DBHelper.getConnection();
				stmt = conn.createStatement();
				String sql1 = "insert into Login(userID,password,token,nickname) values('"+userID+"','"+password+"','"+token+"','"+userID+"')";	//SQL语句
				System.out.println(sql1);
				stmt.executeUpdate(sql1);
			

				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally{
				
				//释放语句对象
				if(stmt != null){
					try {
						stmt.close();
						stmt = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
}
