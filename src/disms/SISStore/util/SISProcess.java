package disms.SISStore.util;
//import org.apache.commons.cli.Options;
import java.io.File;

import disms.SISStore.client.CalculateFileSize;
import disms.SISStore.client.Main;

public class SISProcess{
	private static long MINFILESIZE = 30*1024; 
	public static void main(String args[]) throws Exception{
		
		
		
		
		String FilePath = args[0];
		Main.UserName = args[1];
		//System.out.println(Main.UserName);
		
		//Examine if the file is case with SIS
		CalculateFileSize CALFILE = new CalculateFileSize();
		File FileHandling = new File(FilePath);
		long FileSize = CALFILE.getFileSizes(FileHandling);
		if(FileSize <= MINFILESIZE){
			//Just store it!
			Main.UserName +="SpecialFiles";			
			new ServerTest();			
			ServerTest.start(FilePath);
			
		}
		else
			System.out.println(FileSize);
	}
}