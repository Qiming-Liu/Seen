






package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import util.DBHelper;


/**
 * Servlet implementation class ShowTieServlet
 */
@WebServlet("/ShowTieServlet")
public class ShowTieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowTieServlet() {
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
		
		ResultSet rs = null;
		String sql = "select * from Tie where IsVisual=1";
		PrintWriter pw = response.getWriter();
//		String bottleID;
//		String longitude;
//		String atitude;
//		int type;
		
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			System.out.println(sql);
			JSONArray jsonReply = new JSONArray(); 
			while(rs.next()){
				
				JSONObject json = new JSONObject(); 
				json.put("tieID", rs.getString("tieID"));
				json.put("longitude", rs.getString("longitude"));
				json.put("latitude", rs.getString("latitude"));
				json.put("type", "1");
			
				
				jsonReply.add(json);
				
				
				
				JSONObject json1 = JSONObject.fromObject(json);
				System.out.println(json1);
				
				
			}
			pw.write(jsonReply.toString());   
			pw.flush();  
			pw.close(); 
			
			
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
