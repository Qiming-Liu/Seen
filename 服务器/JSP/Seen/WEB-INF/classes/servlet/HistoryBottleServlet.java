package servlet;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.LoginDAO;
import entity.LoginBean;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import util.DBHelper;

/**
 * Servlet implementation class HistoryBottleServlet
 */
@WebServlet("/HistoryBottleServlet")
public class HistoryBottleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	String bottleID;
	String abID;
	String title;
	String throwID;
	String content;
	String answerID;
	String time;
	 LoginDAO user = new LoginDAO();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoryBottleServlet() {
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
		PreparedStatement stmt1 = null;		
		PreparedStatement stmt2 = null;
		ResultSet rs1, rs2 = null;
			
		
		
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
            System.out.println("=======json is========"+acceptjson);
            if(acceptjson != ""){
                JSONObject jo = JSONObject.fromObject(acceptjson);
                throwID = jo.getString("userID");
                LoginBean  login=  user.getLoginBeanById(throwID);
                System.out.println();
                
                conn = DBHelper.getConnection();
                
				String sql1 = "select * from Bottle1 where throwID='"+throwID+"'";
				System.out.println(sql1);
				String sql2 = "select * from AnswerBottle where throwID='"+throwID+"'";
				System.out.println(sql2);
				stmt1 = conn.prepareStatement(sql1);
				rs1 = stmt1.executeQuery();
				stmt2 = conn.prepareStatement(sql2);
				rs2 = stmt2.executeQuery();
												
				
				PrintWriter pw = response.getWriter();
				
				
				
				JSONArray jsonReply = new JSONArray(); 
				while(rs1.next()){				
					title = rs1.getString("title");									
					content = rs1.getString("content");
					time = rs1.getString("time");
					bottleID = rs1.getString("bottleID");
										
					JSONObject json = new JSONObject(); 
					
					
				
					json.put("title", title);
					json.put("throwID", throwID);
					json.put("content", content);
					json.put("time", time);
			
					json.put("bottleID", bottleID);
					jsonReply.add(json);
					
					System.out.println(title);
					System.out.println(time);
					
					
					JSONObject json1 = JSONObject.fromObject(json);
					System.out.println(json1);
					
					
					
					
					
				
					
				}					
				System.out.println("bbbbbbbbbbbbbb");
				
				while(rs2.next()){
					
					JSONObject json = new JSONObject(); 
					answerID = rs2.getString("answerID");					
					content = rs2.getString("content");
					time = rs2.getString("time");
					abID = rs2.getString("abID");				
					

					
					json.put("answerID", answerID);
					json.put("content", content);
					json.put("time", time);
					json.put("abID", abID);												
														
					jsonReply.add(json);
				
					System.out.println(json);
					
				}
				pw.write(jsonReply.toString());
				pw.flush();  
					
				
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
