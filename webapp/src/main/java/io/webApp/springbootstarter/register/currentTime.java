package io.webApp.springbootstarter.register;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class currentTime {
	private DateFormat dateFormat;
	private Date date; 
	
	public currentTime() {
		super();
		this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		this.date = new Date(); 
	}

	public String getCurrentTime() {
		return dateFormat.format(date); 
	}
}
