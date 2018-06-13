package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {

	private static final String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";	
	private static final String connectionUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=Seen";
	private static final String username="sa";//数据库的用户名
	private static final String password="bajiayi8+1";//数据库的密码
	
	
	private  static Connection conn = null;
	
	//静态代码块负责加载驱动
	static {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws Exception{
		if(conn==null){
			//conn = DriverManager.getConnection("jdbc:odbc:item");
			conn = DriverManager.getConnection(connectionUrl,username,password);
			return conn;
		}
		return conn;
	}
}
