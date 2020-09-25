package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import util.DBHelper;
import random.Serialnumber;

/**
 * Servlet implementation class BottleServlet
 */
@WebServlet("/BottleServlet")
public class BottleServlet extends HttpServlet {
	
	 String bottleID = "0";
	 String title = "0";
	 String throwID = "0";
	 int type = 0;
	 String content = "0";
	 String time = "0";
	 String adress = "0";
	 String longitude = "0";
	 String latitude = "0";
	 boolean visual = false;
	
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BottleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/json");
		 
		 Connection conn = null;
		 PreparedStatement stmt = null;	
		 
		 String acceptjson = "";

		 
		 try {
			 	//数据库连接
				conn = DBHelper.getConnection();				 
				String sql = "insert into Bottle1(bottleID,throwID,content,type,longitude,latitude,adress,time,isVisual,title) values(?,?,?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				
				
				//读取json数据
	            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
	            StringBuffer sb = new StringBuffer("");
	            String temp;
	            while((temp = br.readLine()) != null){
	                sb.append(temp);
	            }
	            br.close();               
	            acceptjson = sb.toString();
	            System.out.println("=======json is========发瓶子了!!!!!!!!!!!"+acceptjson);
	            if(acceptjson != ""){
	                JSONObject jo = JSONObject.fromObject(acceptjson);	                	               
	                /*System.out.println(jo.get("title"));
	                System.out.println(jo.get("throwID"));
	                System.out.println(jo.get("type"));
	                System.out.println(jo.get("content"));
	                System.out.println(jo.get("time"));
	                System.out.println(jo.get("adress"));
	                System.out.println(jo.get("longitude"));
	                System.out.println(jo.get("latitude"));
	                System.out.println(jo.get("visual"));*/
	                
	                title = jo.getString("title");
	                throwID = jo.getString("throwID");
	                type = jo.getInt("type");
	                content = jo.getString("content");
	                time = jo.getString("time");
	                adress = jo.getString("adress");
	                longitude = jo.getString("longitude");
	                latitude = jo.getString("latitude");
	                visual = jo.getBoolean("visual");
	               
	                
	                System.out.println(title);
	                System.out.println(throwID);
	                System.out.println(type);
	                
	                //设置瓶子id
	                Serialnumber sn = new Serialnumber();
	                bottleID = "b"+sn.getNum();
	                System.out.println(bottleID);
	                	                
	                
	                
	                //向数据库插入数据
	                stmt.setString(1, bottleID);
	                stmt.setString(2, throwID);
	                stmt.setString(3, content);
	                stmt.setInt(4, type);
	                stmt.setString(5, latitude);
	                stmt.setString(6, longitude);
	                stmt.setString(7, adress);
	                stmt.setString(8, time);
	                stmt.setBoolean(9, visual);
	                stmt.setString(10, title);
	                stmt.executeUpdate();
	                
	                
	                
	               /* stmt.setString(1, "awkow");
	    			stmt.setString(2, "awewae");
	    			stmt.setString(3, "awl113");
	    			stmt.setInt(4, 1);
	    			stmt.setString(5, null);
	    			stmt.setString(6, null);
	    			stmt.setString(7, null);
	    			stmt.setString(8, null);
	    			stmt.setBoolean(9, true);
	    			stmt.setString(10, "aaaa");
	    			stmt.executeUpdate();*/
//	                conn.close();
	                
	                
	            }
	            else{
	                System.out.println("get the json failed");
	            }
	        } catch (Exception e) {
	            // TODO: handle exception
	            e.printStackTrace();
	            }		 			 
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
