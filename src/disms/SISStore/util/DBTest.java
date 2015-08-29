package disms.SISStore.util;

import java.io.*;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Durability;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Cursor;


public class DBTest{

	Environment myDbEnvironment = null;
	Database myDatabase = null;
	EnvironmentConfig envConfig = null;
	DatabaseConfig dbConfig = null;
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
	public static String bytes2HexString(byte[] b) {
		  String ret = "";
		  for (int i = 0; i < b.length; i++) {
		   String hex = Integer.toHexString(b[ i ] & 0xFF);
		   if (hex.length() == 1) {
		    hex = '0' + hex;
		   }
		   ret += hex.toUpperCase();
		  }
		  return ret;
	}
	public static String bytesToHexString(byte[] src){
       StringBuilder stringBuilder = new StringBuilder("");
       if (src == null || src.length <= 0) {
           return null;
       }
       for (int i = 0; i < src.length; i++) {
           int v = src[i] & 0xFF;
           String hv = Integer.toHexString(v);
           if (hv.length() < 2) {
               stringBuilder.append(0);
           }
           stringBuilder.append(hv);
       }
       return stringBuilder.toString();
   }
   /**
    * Convert hex string to byte[]
    * @param hexString the hex string
    * @return byte[]
    */
	public static byte[] hexStringToBytes(String hexString) {
       if (hexString == null || hexString.equals("")) {
           return null;
       }
       hexString = hexString.toUpperCase();
       int length = hexString.length() / 2;
       char[] hexChars = hexString.toCharArray();
       byte[] d = new byte[length];
       for (int i = 0; i < length; i++) {
           int pos = i * 2;
           d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
       }
       return d;
   }
   /**
    * Convert char to byte
    * @param c char
    * @return byte
    */
    private static byte charToByte(char c) {
       return (byte) "0123456789ABCDEF".indexOf(c);
   }
    public void example(){
		String aKey = "myFirstKey";
		String aData = "myFirstData";
		try {
		DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
		myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		// Exception handling goes here
		}
		 try { 
		      // Create a pair of DatabaseEntry objects. theKey
		      // is used to perform the search. theData is used 
		      // to store the data returned by the get() operation.
		     DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		     DatabaseEntry theData = new DatabaseEntry();
		      
		     // Perform the get.
		      if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
		            // Recreate the data String.
		             byte[] retData = theData.getData();
		             String foundData = new String(retData);
		             System.out.println("For key: '" + aKey + "' found data: '" + foundData + "'.");
		      } else {
		          System.out.println("No record found for key '" + aKey + "'."); 
		     }
		  } catch (Exception e) {    // Exception handling goes here}
		  }
	}
    public void createDB(){
		try {    
		    // Open the environment. Create it if it does not already exist.    
		    envConfig = new EnvironmentConfig();    
		    envConfig.setAllowCreate(true); 
		    envConfig.setTransactional(true);
		    envConfig.setDurability(Durability.COMMIT_SYNC);
		    myDbEnvironment = new Environment(new File("D:/SIS/dbEnv"), envConfig);    
		    
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
	        deletePairs(keyString);
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
	public void getValue(String key){
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
		             String foundData = new String(retData);
		             System.out.println("For key: '" + key + "' found data: '" + foundData + "'.");
		      } else {
		          System.out.println("No record found for key '" + key + "'."); 
		     }
		  } catch (Exception e) {
			  // Exception handling goes here}
		  }
	}
	public static void main(String[] args) throws IOException{
		File file = new File("D:\\SIS\\NO.001\\FileInfo\\2012.05.02.txt");
		BufferedReader inStream = new BufferedReader(new FileReader(file));
		String line = inStream.readLine();
		String[] str = line.split("[*]");
		DBTest dbt = new DBTest();
		dbt.createDB();
//		dbt.deletePairs("b9aea646-c633-4ea4-a586-5a9ff0307174");
//		dbt.addElement("123", "453");
//		dbt.createDB();
		dbt.cursorDB();
		dbt.closeDB();
//		dbt.getValue("db7d837b-e31b-48f8-94df-c44c1eebe542");
//		dbt.closeDB();
//		dbt.addElement(line,line);
//		dbt.closeDB();
//		dbt.createDB();
//		long temp1 = Long.parseLong(str[str.length-8]);	
//		long temp2 = Long.parseLong(str[str.length-7]);	
//		long temp3 = Long.parseLong(str[str.length-6]);	
//		long temp4 = Long.parseLong(str[str.length-5]);	
//		long temp5 = Long.parseLong(str[str.length-4]);	
//		long temp6 = Long.parseLong(str[str.length-3]);	
//		long temp7 = Long.parseLong(str[str.length-2]);	
//		long temp8 = Long.parseLong(str[str.length-1]);	
//		long[] l = {temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8,};	
//		byte[] bs = longToByte(l);
//		printHexString(bs);
		
		
	}
}

//2012.05.02.16:20:00:734*61c94286-3122-4981-bb1b-1b76f5e75869*ae2384dd-433b-464c-abfa-7c4a3ac3948b*e639aa20-b017-423b-b618-bbf3aef8b748*4.mp3*C:\4.mp3*mp3*1979591*1671085575131749272*3856988736255993716*-7877641377561112643*-2976005714987305871*-3233846614439260224*-6347295208440583146*-6973981907159954875*3292836778370533466