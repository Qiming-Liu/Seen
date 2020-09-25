package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.getToken;
import net.sf.json.JSONObject;
import dao.LoginDAO;
import entity.LoginBean;
;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginDAO idao = new LoginDAO();
	//登录对象
	private LoginBean loginbean = new LoginBean();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		 
		String acceptjson = "";
		 System.out.println("IN"); 
		
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
            br.close();           
            acceptjson = sb.toString();
            if(acceptjson != ""){
               JSONObject jo = JSONObject.fromObject(acceptjson);	         
                System.out.println("00000000000000000000"+acceptjson);
                System.out.println(jo.get("userID"));
                System.out.println(jo.get("password"));
               String userID =jo.getString("userID");
               String password =jo.getString("password");
               String beanpassword;
               PrintWriter pw = response.getWriter();
		
               

            
               
			loginbean = idao.getLoginBeanById(userID.toString().trim());
			
			if(loginbean != null)
			{	//如果存在id 核对密码
				
			
//				request.getSession().setAttribute("userID",userID);
				
				beanpassword = loginbean.getPassword().toString().trim();
			
				System.out.println(beanpassword);
				
				
				if(password.equals(beanpassword) )
				{//判断密码与类型是否与数据库相符
					JSONObject json = new JSONObject(); 
					
					System.out.println("sucess");
					boolean code = true;
					json.put("code", code);
					pw.write(json.toString());   
					pw.flush();  
					pw.close(); 
					
				}
				else
				{
				JSONObject json = new JSONObject(); 
				boolean code = false;
				json.put("code", code);
				pw.write(json.toString());  
				System.out.println("faied");
				pw.flush();  
				pw.close(); 
				}
				

			}
			
			else
			{
			JSONObject json = new JSONObject(); 
			boolean code = false;
			json.put("code", code);
			pw.write(json.toString());  
			System.out.println("faied");
			pw.flush();  
			pw.close(); 
			}
			
            
            }
            }
            catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
		// TODO Auto-generated method stub
		
	}
		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);

	}

}
