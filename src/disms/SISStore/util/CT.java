package disms.SISStore.util;
/**
 * @(#)ClientTest.java
 *
 * �ͻ���
 * @author 
 * @version 1.00 2008/8/2
 */
import java.net.Socket;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
public class CT {
    private static Socket socket;
    static BufferedReader in;
    static PrintWriter out;
    
    public static void conn() { 
	    try{
	        System.out.println("ClientTransInfo-->conn:Try to connect to 127.0.0.1:10000");
	        //�������������������
	        socket=new Socket("127.0.0.1",10000);
	        System.out.println("ClientTransInfo-->conn:The Server Connected!");   
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public static void sendMSG(String fileInfo){

        String str = fileInfo;
        InputStream tmp = new ByteArrayInputStream(str.getBytes());    
        try{
            //��ȡ�û�������Ϣ
            BufferedReader line=new BufferedReader(new InputStreamReader(tmp));
            
            //����ӷ������˻�õ���Ϣ
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println(line.readLine());
            /*            
	                out=new PrintWriter(socket.getOutputStream(),true);
	                out.println("ServerGetInfo-->getInfo:"+line);
             * */
            //��ȡ����������Ϣ
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    	
    }
    public static void close(){
    	try{
            out.close();
            in.close();
            socket.close();    
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public static void sendInfo(String fileInfo) {
        String str = fileInfo;
        InputStream tmp = new ByteArrayInputStream(str.getBytes());    
        try{
            System.out.println("Try to connect to 127.0.0.1:10000");
            //�������������������
            socket=new Socket("127.0.0.1",10000);
            System.out.println("The Server Connected!");   
            
            //System.out.println("Please enter some characters:");
            //��ȡ�û�������Ϣ
            BufferedReader line=new BufferedReader(new InputStreamReader(tmp));
            
            //����ӷ������˻�õ���Ϣ
            out=new PrintWriter(socket.getOutputStream(),true);
            out.println(line.readLine());
            
            //��ȡ����������Ϣ
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(in.readLine());
            
            out.close();
            in.close();
            socket.close();    
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args){
    	CT ct1 = new CT();ct1.conn();ct1.sendMSG("sssss");ct1.sendMSG("done");ct1.close();
    	CT ct2 = new CT();ct2.conn();ct2.sendMSG("ttttt");ct2.sendMSG("done");ct2.close();
    }
    
}