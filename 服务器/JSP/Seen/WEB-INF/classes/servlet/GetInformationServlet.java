package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;




import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Statement;

import net.sf.json.JSONObject;
import util.DBHelper;
import dao.LoginDAO;
import entity.*;

/**
 * Servlet implementation class GetInformationServlet
 */
@WebServlet("/GetInformationServlet")
public class GetInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInformationServlet() {
    	
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
		//¶ÁÈ¡jsonÊý¾Ý
		 LoginDAO user = new LoginDAO();
		 String acceptjson = "";
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
         
             
            String userID = jo.getString("userID");
            try {
				LoginBean  login=  user.getLoginBeanById(userID);
				PrintWriter pw = response.getWriter();
				JSONObject jsonReply = new JSONObject(); 
				jsonReply.put("nickname", login.getNickname());
				jsonReply.put("userID",  login.getUserID());
				jsonReply.put("signature",  login.getSignature());
//				jsonReply.put("property1",  login.getProperty1());
//				jsonReply.put("property2",  login.getProperty2());
//				jsonReply.put("sex",  login.isSex());
		
				jsonReply.put("token",  login.getToken());
				JSONObject json1 = JSONObject.fromObject(jsonReply);
				System.out.println(json1);
				pw.write(jsonReply.toString());   
				pw.flush();  
				pw.close(); 
				
			} catch (SQLException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
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
