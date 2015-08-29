/*Tips:
* Lock mechanism should be used.
*
*
*/
package disms.SISStore.util;

import java.io.*;
import java.util.*;


public class SIS{

	private static byte[] hash = null;
	private static final long MinFileSize = 6*1024;
	private static boolean SmallSize = false;
	private static ArrayList SmallFileList = new ArrayList();
	private static String UserID="";
	
	//Check if the user is a valid one
	public static void checkValidUser(String UserName,String UserPWD){
	
	}

	//Create the unique folder for each user to store small files,or in the later the
	//user may need to use other techniques to process these
	public static String createUserSFFolder(){
		File UserSFFolder = new File("D:\\SIS\\SMALLFILE" + UserID);
	 	UserSFFolder.mkdirs();
	 	return UserSFFolder.getAbsolutePath();
	}
	//Check if the path of the file is valid
	public static boolean checkValidFile(String FilePath){
	String SRCFilePath = FilePath;
	//If this file exists
	File CheckIt = new File(SRCFilePath);
	if( CheckIt.exists()){		
       	SmallSize = false;
       	//If this file is an empty one or too small to calculate
       	if(CheckIt.length() < MinFileSize){
       	SmallFileList.add(SRCFilePath);
       	SmallSize = true;
       	}
       	else{
       	//Still thinking....
       	}
       	      	     	
    	 return true;
    	}
    else{
    	//
    	System.out.println("The path of the file is invalid");
    	return false;
		}
	}
	
	//Calculate the hash of the file.
	public static void calculateFileHash(String FilePath){
	String SRCFilePath = FilePath;
	checkValidFile(SRCFilePath);
	if(SmallSize){
		//Just copy it!
		/*Every user need a unique folder to store its small files*/
		//copyFileToDir("USER_1",SRCFilePath,FileName);
		
		
		
		}
	}
	
	//Calculate the hash of the files in the folder
	public static void calculateFolderHash(String FilePath){
	}
	
	private static void checkJavaVersion() {
		Properties sProp = java.lang.System.getProperties();
		String sVersion = sProp.getProperty("java.version");
		sVersion = sVersion.substring(0, 3);
		Float f = Float.valueOf(sVersion);
		if (f.floatValue() < (float) 1.7) {
			System.out.println("Java version must be 1.7 or newer");
			System.out.println("To get Java 7 go to https://jdk7.dev.java.net/");
			System.exit(-1);
		}
		else{
			System.out.println("Java version is 1.7");
		}
	}
	
	public static void main(String[] args) throws Exception {
	
	checkJavaVersion();
	

	
		
	}
}