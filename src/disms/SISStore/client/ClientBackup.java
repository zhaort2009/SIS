package disms.SISStore.client;
import java.io.*;
import java.net.Socket;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


public class ClientBackup{
	private  String userPath = "D:\\SIS\\";
	private  String fileName = "";
	private  byte[] headHash;
	private  byte[] midHash;
	private  byte[] tailHash;
	private  byte[] g_Hash;
	private  long fileSize = 0;
	private  String fileType = "";
	private  String filePath = "";
	private  UUID luid;
	private  final long MinFileSize = 1;
	private  String strDATE = null;
	private  String fileInfo = null;
	private  String[] receiveInfo = null;
	private  String[] commandLines = null;
	private  boolean smallSize;
	private  File fileObj = null;
	private  String userID = null;
	private String host="localhost";
	private int port=8000;
	private Socket socket;
//	public BufferedReader br;
//  public PrintWriter pw;
	/*
	 * 将Byte数组中前8个元素转换为long型
	 */
	public  long byteToLongLOW(byte[] b) {
		long s = 0; 
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	} 
	/*
	 * 将Byte数组中后8个元素转换为long型
	 */
	public  long byteToLongHIGH(byte[] b) {
		long s = 0; 
		long s0 = b[8] & 0xff;// 最低位
		long s1 = b[9] & 0xff;
		long s2 = b[10] & 0xff;
		long s3 = b[11] & 0xff;
		long s4 = b[12] & 0xff;// 最低位
		long s5 = b[13] & 0xff;
		long s6 = b[14] & 0xff;
		long s7 = b[15] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	} 
	/*
	 * 解析命令:将commands进行处理，保存入commandLines，此时为已声明commandLineS分配空间
	 */
	public  void resolveCommand(String[] commands){		
		commandLines = commands;
		userID = commandLines[0];
		filePath = commandLines[2];
	}
	/*
	 * 检查文件大小是否符合标准
	 */
	public  boolean checkValidFile(String FilePath){
		String SRCFilePath = FilePath;
		//If this file exists
		System.out.println(SRCFilePath);
		File CheckIt = new File(SRCFilePath);
		if( CheckIt.exists()){		
	       	smallSize = false;
	       	if(CheckIt.length() < MinFileSize){
		    	System.out.println("ClientBackup-->checkValidFile:The size of the file is too small");
		    	System.out.println(CheckIt.length());
		    	SISgraphics.jl.append(FilePath+" is too small to backup"+'\n');
	       		smallSize = true;
	       	}
	       	else{
		    	System.out.println("ClientBackup-->checkValidFile:The size of the file is fitable");
	       	}	       		
	    	return smallSize;
	    }
	    else{
	    	System.out.println("ClientBackup-->checkValidFile:The path of the file is invalid");
	    	SISgraphics.jl.append(FilePath+" is invalid"+'\n');
	    	return false;
		}
	}
	/*
	 * 获得执行任务的时间，精确到毫秒
	 */
	public  void getTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd.HH:mm:ss:SSS");      
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间      
		strDATE = formatter.format(curDate);
		System.out.println("ClientBackup-->getTime:the current time is "+strDATE);
	}
	/*
	 *计算文件的HASH组 
	 */
	public  void calcuHashArray(File fileOBJ) throws NoSuchAlgorithmException, DigestException, IOException{
		CalculateFileHash cal = new CalculateFileHash();
		headHash = cal.MD5HashValueFirst(fileOBJ);
		midHash = cal.MD5HashValueSecond(fileOBJ);
		tailHash = cal.MD5HashValueThird(fileOBJ);
		g_Hash = cal.MD5HashValueGlobal(fileOBJ);
		System.out.println("ClientBackup-->calcuHashArray:the hashes have been calculated");
	}
	/*
	 * 将文件的信息写入到文件之中
	 */
	public  void saveFileInfo() throws IOException{
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd");      
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间    
		String key = luid.toString();
		String data = strDATE+'*'+luid.toString()+'*'+receiveInfo[1].toString()+
					'*'+receiveInfo[2].toString()+'*'+fileName.toString()+'*'+
					filePath.toString()+'*'+fileType.toString()+'*'+String.valueOf(fileSize)+'*'+
					String.valueOf(byteToLongLOW(headHash))+'*'+String.valueOf(byteToLongHIGH(headHash))+'*'+
					String.valueOf(byteToLongLOW(midHash))+'*'+String.valueOf(byteToLongHIGH(midHash))+'*'+
					String.valueOf(byteToLongLOW(tailHash))+'*'+String.valueOf(byteToLongHIGH(tailHash))+'*'+
					String.valueOf(byteToLongLOW(g_Hash))+'*'+String.valueOf(byteToLongHIGH(g_Hash));		
		  
//		String today = formatter.format(curDate);String tempPath = userPath + userID + "\\FileInfo\\"+today+".txt";
//		File dir = new File(userPath + userID + "\\"); 
//        if(!dir.exists())
//        	dir.mkdir();
//		File dirFile = new File(userPath + userID + "\\FileInfo\\"); 
//        if(!dirFile.exists())
//        	dirFile.mkdir();
//		FileWriter fosTempCONS = new FileWriter(tempPath,true);fosTempCONS.write(data+'\n');
//		fosTempCONS.close();
		
		SISCLIENT.addElement(key, data);
		System.out.println("ClientBackup-->saveFileInfo:the information of the file has been written into a  file");
		
	}
	/*
	 * 获得文件对象
	 */
	public  void getFileObj(){
		if(!filePath.isEmpty()){	
			fileObj = new File(filePath);
		}
	}
	/*
	 * 获取文件信息
	 */
	public void getFileInfo() throws Exception{
		//1.获得当前时间
		getTime();			
		//2.产生此文件LUID值
		luid = UUID.randomUUID();			
		//3.计算文件大小
		CalculateFileSize calFileSize = new CalculateFileSize();
		fileSize = calFileSize.getFileSizes(fileObj);			
		//4.判断文件类型
		fileName = fileObj.getName();
		fileType = JudgeFileType.getEXTName(filePath);			
		//5.计算HASH值
		calcuHashArray(fileObj);	
	}
	/*
	 * 发送需要备份的新文件信息给服务器端进行检查
	 */
	public  void sendFileInfo() throws IOException{
		BufferedReader br;
	    PrintWriter pw;
	    br=getReader(socket);
	    pw=getWriter(socket);
		fileInfo = fileType.toString()+'*'+fileSize+'*'+byteToLongLOW(headHash)+'*'+byteToLongHIGH(headHash)+'*'+byteToLongLOW(midHash)+'*'+byteToLongHIGH(midHash)+'*'+byteToLongLOW(tailHash)+'*'+byteToLongHIGH(tailHash)+'*'+byteToLongLOW(g_Hash)+'*'+byteToLongHIGH(g_Hash);			
		pw.println(userID+'#'+"backup"+'#'+ fileInfo+'#'+strDATE);
		String line = br.readLine();
		System.out.println("ClientBackup-->sendFileInfo:the request has been sent to the server for further examinnation");
		
		System.out.println(line);
		if(!line.equalsIgnoreCase("")){
			receiveInfo = line.split("[*]");//today
			System.out.println("ClientBackup-->receiveFileInfo:the server has sent the results to the client");
			System.out.println(receiveInfo[0]);
		}
		else{
			System.out.println("ClientBackup-->receiveFileInfo:the server fail to send the results to the client");
		}
	}
	private PrintWriter getWriter(Socket socket)throws IOException{
	    OutputStream socketOut = socket.getOutputStream();
	    return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket socket)throws IOException{
	    InputStream socketIn = socket.getInputStream();
	    return new BufferedReader(new InputStreamReader(socketIn));
	}
	public ClientBackup(String[] commands) throws Exception{
		socket=new Socket(host,port); 	   
		resolveCommand(commands);
		if(!checkValidFile(filePath))
		{
			getFileObj();
			getFileInfo();
			sendFileInfo();
			if(receiveInfo[0].equalsIgnoreCase("completed")){				
				saveFileInfo();
				System.out.println("ClientBackup-->ClientBackup:global duplicate file,clientBackup mission comleted");
			}
			else if(receiveInfo[0].equalsIgnoreCase("undone"))
			{
				System.out.println("ClientBackup-->ClientBackup:complete new File,Send the file");
				SendFile sf1 = new SendFile("127.0.0.1",11000,fileObj.toString());
				sf1.start();				
				saveFileInfo();
			}
	    	SISgraphics.jl.append(filePath+" has been backuped successfully"+'\n');
		}		
	}
}