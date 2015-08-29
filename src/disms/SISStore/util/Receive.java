package disms.SISStore.util;
import java.io.File;
//import org.apache.commons.cli.Options;
import java.sql.*;
import java.util.Calendar;

import disms.SISStore.client.CalculateFileSize;
import disms.SISStore.client.Main;
public class Receive{
	public static void main(String args[])
	{
			/*
			Calendar ca = Calendar.getInstance();   
			int year = ca.get(Calendar.YEAR);//获取年份   
	        int month=ca.get(Calendar.MONTH);//获取月份    
	        int day=ca.get(Calendar.DATE);//获取日   
	        int minute=ca.get(Calendar.MINUTE);//分    
	        int hour=ca.get(Calendar.HOUR);//小时    
	        int second=ca.get(Calendar.SECOND);//秒   
	             
	        String date = year + "y" + (month + 1 )+ "m" + day + "d"+ hour + "h" + minute + "m" + second + "s";

			System.out.println(date);*/
			new ClientTest();
			
			String DesFolder = "E:\\SIS\\";
			//DesFolder =DesFolder + date;
			ClientTest.start(DesFolder);
	}
}
			