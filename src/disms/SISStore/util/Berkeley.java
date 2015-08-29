package disms.SISStore.util;
import com.sleepycat.je.*;
import java.io.File;
 
public class Berkeley{
	public static void main(String[] args){
		Environment myDbEnvironment = null;
		
		Database myDatabase = null;
		try {    
		    // Open the environment. Create it if it does not already exist.    
		    EnvironmentConfig envConfig = new EnvironmentConfig();    
		    envConfig.setAllowCreate(true);    
		    myDbEnvironment = new Environment(new File("D:/SIS/dbEnv"), envConfig);    
		    
		    // Open the database. Create it if it does not already exist.    
		    DatabaseConfig dbConfig = new DatabaseConfig();    
		    dbConfig.setAllowCreate(true);    
		    myDatabase = myDbEnvironment.openDatabase(null,  "sampleDatabase", dbConfig); 
		} 
		catch (DatabaseException dbe) {    // Exception handling goes here}
		}
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
 }