package random;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Serialnumber {
	
	public String getStringDate() {
		Date currentTime = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");  
        String dateString = formatter.format(currentTime);  
        return dateString;  
	}
	
	public String getNum() {
		String t = getStringDate();  
        int x=(int)(Math.random()*900)+100;  
        String serial = t + x;  
        System.out.println(serial);
        return serial;  
	}
 
}
