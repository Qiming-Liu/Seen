package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBHelper;
import util.getToken;

/**
 * Servlet implementation class GetTieServlet
 */
@WebServlet("/GetTieServlet")
public class GetTieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTieServlet() {
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
		PreparedStatement stmt1, stmt2 ,  stmt3 = null;		 
		ResultSet rs1, rs2,rs3 = null;
		

		 String tieID;
		 String commentID;
		 String comment;
		 String c_userID;
		 String title;
		 String t_userID;
		 String content;
		 String time;
		 String Image1;
		 String Image2;
		 String Image3;
		 String c_time;
		 String circleImage;
		 String nickname;
		 int agree;
		 int pageviews;
		 
		String acceptjson = "";
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
            br.close();           
            acceptjson = sb.toString();
            System.out.println("=======json is========GetTieServlet"+acceptjson);
            if(acceptjson != ""){
                JSONObject jo = JSONObject.fromObject(acceptjson);
                tieID = jo.getString("tieID");     
                
                
                
                	conn = DBHelper.getConnection();
    				String sql1 = "select * from Tie where tieID=";
    				 System.out.println(sql1+"'"+tieID+"'");
    				String sql2 = "select * from Comment where tieID=";
    				 System.out.println(sql2+"'"+tieID+"'");
    		
    				stmt1 = conn.prepareStatement(sql1+"'"+tieID+"'");
    				stmt2 = conn.prepareStatement(sql2+"'"+tieID+"'");
    				rs1 = stmt1.executeQuery();
    				rs2 = stmt2.executeQuery();				
    				JSONArray jsonReply = new JSONArray(); 
    				PrintWriter pw = response.getWriter();
    				while(rs1.next()){
    					 
    					t_userID = rs1.getString("t_userID");
    					 String sql3 = "select * from Login where userID=";
        				 System.out.println(sql3+"'"+t_userID+"'");
        				 stmt3 = conn.prepareStatement(sql3+"'"+t_userID+"'");
        				 
        				 rs3 = stmt3.executeQuery();
        				 rs3.next();
    					content = rs1.getString("content");
    					time = rs1.getString("time");
    					title = rs1.getString("title");
    					Image1 = rs1.getString("Image1");
    					Image2 = rs1.getString("Image2");
    					Image3 = rs1.getString("Image3");
    					
    					agree = rs1.getInt("agree");
    					pageviews = rs1.getInt("pageviews");
    					
    					nickname = rs3.getString("nickname");
    					circleImage = rs3.getString("HeadImage");
    					
    					
    					
    					
    					
    					JSONObject json = new JSONObject(); 
    					json.put("t_userID", t_userID);
    					json.put("title", title);
    					json.put("content", content);
    					json.put("time", time);
    					json.put("nickname", nickname);
    					json.put("pageviews", pageviews);
    					json.put("agree", agree);
    					json.put("circleImage", circleImage);
    				
    					json.put("Image1", Image1);
    					json.put("Image2", Image2);
    					json.put("Image3", Image3);
    					
    				
    					
    					jsonReply.add(json);
    					
    				}
    				
    				while(rs2.next()) {
    					int i = 0;
    					
    					commentID = rs2.getString("commentID");
    					content = rs2.getString("content");
    					c_userID = rs2.getString("c_userID");
    					c_time = rs2.getString("c_time");
   					
    					JSONObject json = new JSONObject(); 
    					json.put("commentID", commentID);
    					json.put("content", content);
    					json.put("c_userID", c_userID);
    					json.put("c_time", c_time);
    					
    					jsonReply.add(json);
    					
    					 
    				}
    				
    				 System.out.println("=======11111="+acceptjson);
    				pw.write(jsonReply.toString());   
					pw.flush();  
					pw.close(); 
					 
				
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
