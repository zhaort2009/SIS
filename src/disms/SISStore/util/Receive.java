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
			int year = ca.get(Calendar.YEAR);//��ȡ���   
	        int month=ca.get(Calendar.MONTH);//��ȡ�·�    
	        int day=ca.get(Calendar.DATE);//��ȡ��   
	        int minute=ca.get(Calendar.MINUTE);//��    
	        int hour=ca.get(Calendar.HOUR);//Сʱ    
	        int second=ca.get(Calendar.SECOND);//��   
	             
	        String date = year + "y" + (month + 1 )+ "m" + day + "d"+ hour + "h" + minute + "m" + second + "s";

			System.out.println(date);*/
			new ClientTest();
			
			String DesFolder = "E:\\SIS\\";
			//DesFolder =DesFolder + date;
			ClientTest.start(DesFolder);
	}
}
			