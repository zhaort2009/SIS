package disms.SISStore.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Durability;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;


public class ServerBackup{
	private String fileInfoPath = "E:\\SISServer\\";
	private String[] fileInfoList = null;
	private String fileInfo = null;
	private String getInfo = null;
	private String[] getInfoList = null;
	private String userID = null;
	private UUID link_NO = null;
	private UUID file_UUID = null;
	private String strDATE = null;
	private String userInfoPath = null;
	private String tempPath = null;
	private String txtInfoPath = null;
	private String returnInfo = null;
	private Socket fileSoc;
	private Socket socket;
	private Environment myDbEnvironment = null;
	private Database myDatabase = null;
	private EnvironmentConfig envConfig = null;
	private DatabaseConfig dbConfig = null;
	private String linkDB = "E:/SIServer/dbEnv1";
	private String infoDB = "E:/SIServer/dbEnv2";
	public void createDB(String dbPath){
			try {    
			    // Open the environment. Create it if it does not already exist.    
			    envConfig = new EnvironmentConfig();    
			    envConfig.setAllowCreate(true); 
			    envConfig.setTransactional(true);
			    envConfig.setDurability(Durability.COMMIT_WRITE_NO_SYNC);
			    myDbEnvironment = new Environment(new File(dbPath), envConfig);    
			    
			    // Open the database. Create it if it does not already exist.    
			    dbConfig = new DatabaseConfig();    
			    dbConfig.setAllowCreate(true); 

			    myDatabase = myDbEnvironment.openDatabase(null,"sampleDatabase", dbConfig); 
			} 
			catch (DatabaseException dbe) {    // Exception handling goes here}
			}
		}
	public void closeDB(){
				try {
			        if (myDatabase != null) {
			            myDatabase.close();
			        }
			        if (myDbEnvironment != null) {
			            myDbEnvironment.close();
			        }
			} catch (DatabaseException dbe) {
			    // 错误处理
			}
		}
	public void cursorDB(){
			Cursor cursor = null;
			 // Open the cursor. 
			try {

		    cursor = myDatabase.openCursor(null, null);

		    // Cursors need a pair of DatabaseEntry objects to operate. These hold
		    // the key and data found at any given position in the database.
		    DatabaseEntry foundKey = new DatabaseEntry();
		    DatabaseEntry foundData = new DatabaseEntry();

		    // To iterate, just call getNext() until the last database record has been 
		    // read. All cursor operations return an OperationStatus, so just read 
		    // until we no longer see OperationStatus.SUCCESS
		    while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
		        OperationStatus.SUCCESS) {
		        // getData() on the DatabaseEntry objects returns the byte array
		        // held by that object. We use this to get a String value. If the
		        // DatabaseEntry held a byte array representation of some other data
		        // type (such as a complex object) then this operation would look 
		        // considerably different.
		        String keyString = new String(foundKey.getData());
		        String dataString = new String(foundData.getData());
		        System.out.println("Key - Data : " + keyString + " - " + dataString + "");
		    }
		} catch (DatabaseException de) {
		    System.err.println("Error accessing database." + de);
		} finally {
		    // Cursors must be closed.
		    cursor.close();
		}
		}
	public void addElement(String key,String data){
			String aKey = key;
			String aData = data;
			try {
			DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
			myDatabase.put(null, theKey, theData);
			} catch (Exception e) {
			// Exception handling goes here
			}
			
		}
	public void deletePairs(String key){
			try {
			    String aKey = key;
			    DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
			    
			    // Perform the deletion. All records that use this key are
			    // deleted.
			    myDatabase.delete(null, theKey); 
			    System.out.println("delete successfully!");
			} catch (Exception e) {
			    // Exception handling goes here
			}
		}
	public String getValue(String key){
		 String foundData = null;
		 try { 
		      // Create a pair of DatabaseEntry objects. theKey
		      // is used to perform the search. theData is used 
		      // to store the data returned by the get() operation.
		     DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		     DatabaseEntry theData = new DatabaseEntry();
		      
		     // Perform the get.
		      if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
		            // Recreate the data String.
		             byte[] retData = theData.getData();
		             foundData = new String(retData);
		             System.out.println("For key: '" + key + "' found data: '" + foundData + "'.");
		      } else {
		          System.out.println("No record found for key '" + key + "'."); 
		     }
		  } catch (Exception e) {
			  // Exception handling goes here}
		  }
		  return foundData;
	}
	public void saveUserFileInfo(String userInfoPath) throws IOException{
		FileWriter fosTempCONS = new FileWriter(userInfoPath,true);
		fosTempCONS.write(userID+'*'+strDATE+'*'+link_NO.toString()+'*'+getInfoList[1]+'*'+getInfoList[2]+'*'+getInfoList[3]+'*'+getInfoList[4]+'*'+getInfoList[5]+'\n');
		fosTempCONS.close();
//		String link = link_NO.toString();
//		String info = userID+'*'+strDATE+'*'+link+'*'+getInfoList[1]+'*'+getInfoList[2]+'*'+getInfoList[3]+'*'+getInfoList[4]+'*'+getInfoList[5];
//		FileWriter fosTempCONS = new FileWriter(userInfoPath,true);
//		fosTempCONS.write(info+'\n');
//		fosTempCONS.close();
//		createDB(linkDB);
//		addElement(getInfoList[1],link);
//		cursorDB();
//		closeDB();
		
	}
	public void saveInfoFile(String infoFilePath) throws IOException
	{
		
		FileWriter fosTempCONS = new FileWriter(infoFilePath,true);
		fosTempCONS.write('#'+strDATE+'*'+file_UUID.toString()+'*'+file_UUID.toString()+'.'+getInfoList[0]+'*'+tempPath+'*'+fileInfo+'\n');
		fosTempCONS.close();	
	}
	public static byte[] longToByte(long[] longArray)throws IOException{ 
		byte[] byteArray=new byte[longArray.length*8]; 
		for(int i=0;i<longArray.length;i++) 
		{ 
		byteArray[0+8*i]=(byte)(longArray[i]>>56); 
		byteArray[1+8*i]=(byte)(longArray[i]>>48); 
		byteArray[2+8*i]=(byte)(longArray[i]>>40); 
		byteArray[3+8*i]=(byte)(longArray[i]>>32); 
		byteArray[4+8*i]=(byte)(longArray[i]>>24); 
		byteArray[5+8*i]=(byte)(longArray[i]>>16); 
		byteArray[6+8*i]=(byte)(longArray[i]>>8); 
		byteArray[7+8*i]=(byte)(longArray[i]>>0); 
		} 
		return byteArray; 
		} 

	//将指定byte数组以16进制的形式打印到控制台
	public static void printHexString( byte[] b) {  
	   for (int i = 0; i < b.length; i++) { 
	     String hex = Integer.toHexString(b[i] & 0xFF); 
	     if (hex.length() == 1) { 
	       hex = '0' + hex; 
	     } 
	     System.out.print(hex.toUpperCase() ); 
	   } 
	}
	public void createFile() throws IOException{
		//给文件分类
		fileInfoList = fileInfo.split("[*]");
		for(int i = 0;i< fileInfoList.length;i++)
		{
			System.out.println("fileInfoList"+"["+i+"]:"+fileInfoList[i]);
		}
		CreateHashDIR chd = new CreateHashDIR();
		long hash1 = Long.parseLong(fileInfoList[2]);
		long hash2 = Long.parseLong(fileInfoList[4]);
		long hash3 = Long.parseLong(fileInfoList[6]);
		long hash4 = Long.parseLong(fileInfoList[8]);
		chd.createFolder(hash1, hash2, hash3, hash4);
		fileInfoPath = chd.getFilePath();
	}
	public void processMSG(String command,String ID,String time){
		userID = ID;
		fileInfo = command;
		strDATE = time;
	}
	public void findDedup(){
		new ServerLocalDedup();
		getInfo = ServerLocalDedup.findLocalDedup(fileInfo, fileInfoPath);
	}
	public void getUserInfo() throws IOException{
		userInfoPath = fileInfoPath + "linkInfo.txt";
		File InfoUser = new File(userInfoPath);
		if (!(InfoUser.exists())&&!(InfoUser.isDirectory()))
		{
            boolean  creadok  =  InfoUser.createNewFile();
            if (creadok)  {
               System.out.println("ServerBackup-->createFile:the user file has been created successfully");
           } else  {
               System.out.println("ServerBackup-->createFile:fail to create the user file");                    
           } 
		} 
		if(!getInfo.equalsIgnoreCase(""))
			getInfoList = getInfo.split("[*]");
		else 
			System.out.println("wrong message!");
	}
	public void getLink(){
		link_NO = UUID.randomUUID();	
	}
	private PrintWriter getWriter(Socket socket)throws IOException{
	    OutputStream socketOut = socket.getOutputStream();
	    return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket socket)throws IOException{
	    InputStream socketIn = socket.getInputStream();
	    return new BufferedReader(new InputStreamReader(socketIn));
	}
	public void sendInfo() throws IOException{
		System.out.println("into sendInfo");
		@SuppressWarnings("unused")
		BufferedReader br;
	    PrintWriter pw;
	    br=getReader(socket);
	    pw=getWriter(socket);
        pw.println(returnInfo);
		/*sendTo = new ServerTransInfo();			
		sendTo.sendInfo(returnInfo);
		sendTo.close();*/
	}
	public void getFileInfo(){

		if(!fileInfo.equalsIgnoreCase(""))
			getInfoList = fileInfo.split("[*]");
		else 
			System.out.println("wrong message!");
		file_UUID = UUID.randomUUID();	
		txtInfoPath = fileInfoPath + "fileInfo.txt";
		tempPath = fileInfoPath+"files\\"+file_UUID+'.'+getInfoList[0];		
	}
	public void getFile() throws IOException{
		File createFile = new File(tempPath);
		if(!createFile.exists())
			createFile.createNewFile();
		if(!SISSERVER.serSocket.isBound()){
			SISSERVER.serSocket.bind(new InetSocketAddress(11000));
		}
		System.out.println(SISSERVER.serSocket.getLocalPort());
		fileSoc = SISSERVER.serSocket.accept();
	    SISSERVER.filePool.execute(new HandlerTrans(fileSoc,tempPath));
	}
	ServerBackup(String command,String ID,String time,Socket socket) throws Exception{
		this.socket= socket; 
		System.out.println("ServerBackup-->ServerBackup:into the server backup service");
		processMSG(command,ID,time);
		createFile();
		findDedup();	
		if(getInfo != null){
			getUserInfo();
			getLink();
			saveUserFileInfo(userInfoPath);
			returnInfo = "completed"+'*'+link_NO.toString()+'*'+getInfoList[1];
			sendInfo();
		}
		else
		{
			getFileInfo();
			getLink();
			returnInfo = "undone"+'*'+link_NO.toString()+'*'+file_UUID.toString();
			sendInfo();
			getFile();
			saveInfoFile(txtInfoPath);
			findDedup();
			getUserInfo();
			saveUserFileInfo(userInfoPath);
		}
	}
}