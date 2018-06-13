package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBHelper;
@WebServlet("/SeenServlet")
public class SeenServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		Connection conn = null;
		Statement stmt = null;
		
		ResultSet rs = null;
		
		PrintWriter pw = response.getWriter();
		String sql;
		String Search;
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
            System.out.println("=======json is========PlusPlusPlusPlusPlusServletÒÑÔÄ£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡£¡"+acceptjson);
            if(acceptjson != "")
            {
                JSONObject jo = JSONObject.fromObject(acceptjson);
                Search = jo.getString("Seen");   
               
		   
			 sql = "update Tie set pageviews=pageviews+1 where tieID='"+Search+"'";
		  
			 conn = DBHelper.getConnection();
			 stmt = conn.createStatement();
			 stmt.executeUpdate(sql);
				 System.out.println(sql);
			
			}
			
                                  
            }
            
		
		
		
            catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
//		String longitude;
//		String atitude;
//		int type;
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
