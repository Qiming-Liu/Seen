package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import random.Serialnumber;
import util.DBHelper;

/**
 * Servlet implementation class AnswerServlet
 */
@WebServlet("/AnswerServlet")
public class AnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswerServlet() {
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
		PreparedStatement stmt1,stmt2 = null;	
		
		String abID;
		String bottleID;
		String answerID;
		String title;
		String content;
		String time;
		String throwID;
		String adress;
		boolean visual;
		
		
		String acceptjson = "";
		
		try {
			
			conn = DBHelper.getConnection();				 
			String sql1 = "insert into AnswerBottle(abID,bottleID,answerID,content,time,title,throwID,adress) values(?,?,?,?,?,?,?,?)";
			String sql2 = "update * from Bottle set IsVisual=0 where bottleID=";
			stmt1 = conn.prepareStatement(sql1);
			stmt2 = conn.prepareStatement(sql2);
			
			
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
                
                answerID = jo.getString("answerID");
                bottleID = jo.getString("bottleID");
                title = jo.getString("title");
                content = jo.getString("content");
                time = jo.getString("time");
                adress = jo.getString("adress");
                throwID = jo.getString("throwID");
                
                
                Serialnumber sn = new Serialnumber();
                abID = sn.getNum();
                System.out.println(abID);
                          

    			stmt2 = conn.prepareStatement(sql2+"bottleID");    			
    			
    			
                stmt1.setString(1, abID);
                stmt1.setString(2, bottleID);
                stmt1.setString(3, answerID);
                stmt1.setString(4, content);
                stmt1.setString(5, time);
                stmt1.setString(6, title);
                stmt1.setString(7,throwID);
                stmt1.setString(8, adress);
                stmt1.executeUpdate();
                conn.close();
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
