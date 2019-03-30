package io.webApp.springbootstarter.register;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class currentTime {

	//importing the simple data format
	private DateFormat dateFormat;
	private Date date; //using Class of Date and creating an instance
	
	public currentTime() {
		super();
		this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		this.date = new Date(); //store the value in the date instance variable
	}

	public String getCurrentTime() {
		return dateFormat.format(date); //2016/11/16 12:08:43
	}
}
