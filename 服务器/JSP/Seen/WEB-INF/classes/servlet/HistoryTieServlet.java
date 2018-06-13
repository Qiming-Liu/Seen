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

/**
 * Servlet implementation class HistoryTieServlet
 */
@WebServlet("/HistoryTieServlet")
public class HistoryTieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoryTieServlet() {
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
		ResultSet rs= null;
		
		String t_userID;
		String title;
		String content;
		String time;
		
		String tieID;
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
                t_userID = jo.getString("userID");

                conn = DBHelper.getConnection();
                
				String sql = "select * from Tie where t_userID='"+t_userID+"'";
				
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				JSONArray jsonReply = new JSONArray(); 
				PrintWriter pw = response.getWriter();
				while(rs.next()){
					
				
					title = rs.getString("title");				
					content = rs.getString("content");
					time = rs.getString("time");
				
					t_userID = rs.getString("t_userID");
					 tieID   =rs.getString("tieID");
					
					JSONObject json = new JSONObject(); 
//					json.put("title", title);
//					json.put("content", content);
//					json.put("time", time);
//					json.put("t_userID", t_userID);
					json.put("tieID", tieID);
					
					
					
					jsonReply.add(json);	
					//JSONObject json1 = JSONObject.fromObject(json);
					//System.out.println("Ьћзг+"+json1);
				}
				System.out.println("Ьћзг+"+jsonReply.toString());
				pw.write(jsonReply.toString());  
				pw.flush();  
//				conn.close();
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
