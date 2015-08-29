package disms.SISStore.util;
/**
 * @(#)ServerSocketTest.java
 *
 * ��������
 * @author 
 * @version 1.00 2008/8/2
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class ClientGetInfo {
    private static ServerSocket ss;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    public ClientGetInfo(){
    	
    }

    public static void conn(){
        try{
            ss=new ServerSocket(10001);//����������������...
            System.out.println("Server is listening at 10001...");
            //while(true){
                socket=ss.accept();
                //��ȡ�ͻ���IP��ַ
                String remoteIP=socket.getInetAddress().getHostAddress();
                //��ȡ�ͻ������Ӷ˿�
                String remotePort=":"+socket.getLocalPort();
                System.out.println("A client come in!IP:"+remoteIP+remotePort);

        }catch(IOException ex){
            System.out.println(ex.getCause());
        } 	
    }
    public static String getMSG() {
    	String line =null;
        try{//��ȡ�ͻ�������
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            line=in.readLine();
            //System.out.println("Client send is:"+line);
            
            //������������Ϣ�����ͻ���               
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println("Your Message Received!");
        //    }
        }catch(IOException ex){
            System.out.println(ex.getCause());
        }
        return line;    	
    }   

    public static void close(){ 

        try{
        out.close();
        in.close();
		socket.close();
		}catch(IOException ex){
		    System.out.println(ex.getCause());
		} 
    	
    }
    
    public static String getInfo() {
    	String line =null;
        try{
            ss=new ServerSocket(10001);//����������������...
            System.out.println("Server is listening at 10001...");
            //while(true){
                socket=ss.accept();
                //��ȡ�ͻ���IP��ַ
                String remoteIP=socket.getInetAddress().getHostAddress();
                //��ȡ�ͻ������Ӷ˿�
                String remotePort=":"+socket.getLocalPort();
                System.out.println("A client come in!IP:"+remoteIP+remotePort);
                
                //��ȡ�ͻ�������
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                line=in.readLine();
                //System.out.println("Client send is:"+line);
                
                //������������Ϣ�����ͻ���               
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println("Your Message Received!");
                
                
                
                out.close();
                in.close();
                socket.close();
        //    }
        }catch(IOException ex){
            System.out.println(ex.getCause());
        } 

        return line;
    }
    
    public static void main (String[] args) {
    	//String[] line = null;
       // new ServerGetInfo();
       // ServerGetInfo.getInfo(line);
    }  
}