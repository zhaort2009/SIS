package disms.SISStore.util;
/**
 * @(#)ServerSocketTest.java
 *
 * 服务器端
 * @author 
 * @version 1.00 2008/8/2
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class SG{
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static ServerSocket ss;
	private static String line =null;
    public static  void conn(){
        try{
            ss=new ServerSocket(10000,10);//建立服务器，监听...
            System.out.println("ServerGetInfo-->conn:Server is listening at 10000...");
            //while(true){
                socket=ss.accept();
                //获取客户端IP地址
                String remoteIP=socket.getInetAddress().getHostAddress();
                //获取客户端连接端口
                String remotePort=":"+socket.getLocalPort();
                System.out.println("ServerGetInfo-->conn:A client come in!IP:"+remoteIP+remotePort);

        }catch(IOException ex){
            System.out.println(ex.getCause()+"trouble");
        } 	
    }
    public  String getMSG() {
    	String line =null;
        try{//读取客户端输入
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            line=in.readLine();
            //System.out.println("Client send is:"+line);
            
            //将服务器端信息发往客户端               
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println("ServerGetInfo-->getMSG:Your Message Received");
        //    }
        }catch(IOException ex){
            System.out.println(ex.getCause());
        } 
        return line;     	
    }
    public void close(){ 

        try{
        out.close();
        in.close();
		socket.close();
		}catch(IOException ex){
		    System.out.println(ex.getCause());
		} 
    	
    }
    
    public static String getInfo() {
        try{

        	ServerSocket ss;
            ss=new ServerSocket(10000,10);//建立服务器，监听...
            System.out.println("ServerGetInfo-->getInfo:Server is listening at 10000...");
            while(true){
                socket=ss.accept();
                //获取客户端IP地址
                String remoteIP=socket.getInetAddress().getHostAddress();
                //获取客户端连接端口
                String remotePort=":"+socket.getLocalPort();
                System.out.println("ServerGetInfo-->getInfo:A client come in!IP:"+remoteIP+remotePort);
                
                //读取客户端输入
               do{ 
                	in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                line=in.readLine();
	                System.out.println("Client send is:"+line);
	                
	                //将服务器端信息发往客户端               
	                out=new PrintWriter(socket.getOutputStream(),true);
	                out.println("ServerGetInfo-->getInfo:"+line);
                  } while(!line.equalsIgnoreCase("done"));
                out.close();
                in.close();
                socket.close();
            }
        }catch(IOException ex){
            System.out.println(ex.getCause());
        } 
        return line;
    }
    public static String backMessage(){
    	return line;
    }
    
    public static void main (String[] args) {
	    String line = null;
	    new SG();
	    SG.getInfo();
	    
    }  
}