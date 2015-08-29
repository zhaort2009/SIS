
package disms.SISStore.util;
/*
 * File: EchoClient.java<2>
 * Author: Java, Java, Java
 * Description: This version of EchoClient extends the
 *   generic Client class. It implements the requestService()
 *   method to define a particular echo service in which input
 *   is accepted from the user and sent to the server. The
 *   server simply echos what it receives.
 */

import java.net.*;
import java.io.*;

public class EchoClient2 extends ClientServer {
  
    /**
     * EchoClient() constructor creates a client object
     *   given the server's url and port
     * @param url -- a String giving the server's URL
     * @param port -- an int giving the port number
     */
	protected Socket socket; 
    public EchoClient2( String url, int port ) {
        try{
        	socket = new Socket(url,port);
        	System.out.println("Client: connected to "+url+":"+port);
        }catch(Exception e){
        	System.exit(1);
        }
    }
   	public void run(){
   		try{
   			requestService(socket);
   			socket.close();
   			System.out.println("Client:connection closed");
   		}catch(IOException e){
   			System.out.println(e.getMessage());
   			e.printStackTrace();
   		}
   	}
    /**
     * requestService() repeatedly accepts input from the
     *  user and transmits it to the server, which echos it.
     * @param socket -- a Socket connection to the server
     */
    protected void requestService(Socket socket) throws IOException { 
        String servStr = readFromSocket(socket);            // Check FOR "Hello"
        System.out.println("SERVER: " + servStr);           // Report the server's response
        System.out.println("CLIENT: type a line or 'goodbye' to quit"); // Prompt the user
        if ( servStr.substring(0,5).equals("Hello") ) {
            String userStr = "";
            do {
                userStr = readFromKeyboard();                  // Get input from user
                writeToSocket(socket, userStr + "\n");         // Send it to server
                servStr = readFromSocket(socket);              // Read the server's response
                System.out.println("SERVER: " + servStr);      // Report the server's response
            } while(!userStr.toLowerCase().equals("goodbye")); // Until user says 'goodbye'
        }
    } // requestService()
    protected String readFromKeyboard() throws IOException{
    	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    	System.out.print("INPUT: ");
    	String line = input.readLine();
    	return line;
    }
    /**
     * main() creates an EchoClient object and makes a connection
     *  to the echo server.
     */
    public static void main(String args[]) {
        EchoClient client = new EchoClient("localhost", 10001);
        client.start();
    } // main()
} // EchoClient
