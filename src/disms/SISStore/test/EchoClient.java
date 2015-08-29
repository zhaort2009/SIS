//与第1章的EchoClient类相同
package disms.SISStore.test;
import java.net.*;
import java.io.*;
import java.util.*;

import disms.SISStore.server.SendFile;
public class EchoClient {
  private String host="localhost";
  private int port=8000;
  private Socket socket;
  
  public EchoClient()throws IOException{
     socket=new Socket(host,port);  
  }
 
  public static void main(String args[])throws IOException{
    new EchoClient().talk("D:\\p.mp3");
  }
  private PrintWriter getWriter(Socket socket)throws IOException{
    OutputStream socketOut = socket.getOutputStream();
    return new PrintWriter(socketOut,true);
  }
  private BufferedReader getReader(Socket socket)throws IOException{
    InputStream socketIn = socket.getInputStream();
    return new BufferedReader(new InputStreamReader(socketIn));
  }
  public void talk(String command){ //talk()throws IOException {
    try{
      BufferedReader br=getReader(socket);
      PrintWriter pw=getWriter(socket);
//      BufferedReader localReader=new BufferedReader(new InputStreamReader(System.in));
//      String msg=null;
      String msg = "now i see!";
//      while((msg=localReader.readLine())!=null){

        pw.println(msg);
        String line = br.readLine();
        System.out.println(line);
        
        if(line.equals("ok")){
        	SendFile sf1 = new SendFile("127.0.0.1", 10000,"C:\\1.mp3");
        	SendFile sf2 = new SendFile("127.0.0.1", 10000,"C:\\2.mp3");
        	SendFile sf3 = new SendFile("127.0.0.1", 10000,"C:\\3.mp3");
        	sf1.start();
        	sf2.start();
        	sf3.start();
        }

//        if(msg.equals("bye"))
//          break;
//      }
    }catch(IOException e){
       e.printStackTrace();
    }finally{
       try{socket.close();}catch(IOException e){e.printStackTrace();}
    }
  }
}
