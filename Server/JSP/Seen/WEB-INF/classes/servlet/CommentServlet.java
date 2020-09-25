package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import random.Serialnumber;
import util.DBHelper;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
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
		
		String commentID;
		String tieID;
		String content;
		String c_time;
		String c_userID;
		
		
		String acceptjson = "";
		
		try {
			
			conn = DBHelper.getConnection();				 
			String sql = "insert into Comment(commentID,tieID,content,c_userID,c_time) values(?,?,?,?,?)";			
			stmt = conn.prepareStatement(sql);
			
			
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
                
                tieID = jo.getString("tieID");
                content = jo.getString("content");
                c_userID = jo.getString("c_userID");
                c_time = jo.getString("c_time");
                
                Serialnumber sn = new Serialnumber();
                commentID = sn.getNum();
                System.out.println(commentID);
                          
   			
    			
    			
                stmt.setString(1, commentID);
                stmt.setString(2, tieID);
                stmt.setString(3, content);
                stmt.setString(4, c_userID);
                stmt.setString(5, c_time);
                stmt.executeUpdate();
              
            }
            else{
                System.out.println("get the json failed");
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
