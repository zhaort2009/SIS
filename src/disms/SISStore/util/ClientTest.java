package disms.SISStore.util;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Calendar;

public class ClientTest {
    private static ClientSocket cs = null;

    private static String ip = "localhost";// 设置成服务器IP

    private static int port = 8821;

    private static String sendMessage = "Windwos";

    public static void start(String path) {
        try {
            if (createConnection()) {
                sendMessage();
                getMessage(path);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean createConnection() {
        cs = new ClientSocket(ip, port);
        try {
            cs.CreateConnection();
            System.out.print("连接服务器成功!" + "\n");
            return true;
        } catch (Exception e) {
            System.out.print("连接服务器失败!" + "\n");
            return false;
        }

    }

    private static void sendMessage() {
        if (cs == null)
            return;
        try {
            cs.sendMessage(sendMessage);
        } catch (Exception e) {
            System.out.print("发送消息失败!" + "\n");
        }
    }

    private static void getMessage(String savePath) {
    	String Path = "";
        if (cs == null)
            return;
        DataInputStream inputStream = null;
        try {
            inputStream = cs.getMessageStream();
        } catch (Exception e) {
            System.out.print("接收消息缓存错误\n");
            return;
        }

        try {
        	
            Calendar ca = Calendar.getInstance();   
			int year = ca.get(Calendar.YEAR);//获取年份   
	        int month=ca.get(Calendar.MONTH);//获取月份    
	        int day=ca.get(Calendar.DATE);//获取日   
	        int minute=ca.get(Calendar.MINUTE);//分    
	        int hour=ca.get(Calendar.HOUR);//小时    
	        int second=ca.get(Calendar.SECOND);//秒   
	             
	        String date = year + "年" + (month + 1 )+ "月" + day + "日"+ hour + "时" + minute + "分" + second + "秒";
			
            //本地保存路径，文件名会自动从服务器端继承而来。
        	//
        	//System.out.println(savePath);
			//Timestamp NowTime = new Timestamp(System.currentTimeMillis());
			Path =savePath;
        	Path =Path + inputStream.readUTF()+"\\";
        	//System.out.println(Path);
			//Path +=inputStream.readUTF();
			System.out.println(Path+"It's here");
        	File SavaFilePath = new File(Path);
            if (!SavaFilePath.exists())
            	SavaFilePath.mkdir();

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            int passedlen = 0;
            long len=0;


            Path =Path+date+ inputStream.readUTF();

			System.out.println(Path+"It's here now");
           // System.out.println(savePath);
            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(Path))));
            System.out.println(len);
            len = inputStream.readLong();
            System.out.println(len);
            System.out.println("文件的长度为:" + len + "\n");
            System.out.println("开始接收文件!" + "\n");
                    
            while (true) {
                int read = 0;
                if (inputStream != null) {
                    read = inputStream.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                //下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
                System.out.println("文件接收了" +  (passedlen * 100/ len) + "%\n");
                fileOut.write(buf, 0, read);
            }
            System.out.println("接收完成，文件存为" + Path + "\n");

            fileOut.close();
        } catch (Exception e) {
            System.out.println("接收消息错误" + "\n");
            return;
        }
    }

    public static void main(String args[]) {
    	new ClientTest();
		ClientTest.start(args[0]);
    }
}
