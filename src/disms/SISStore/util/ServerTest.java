package disms.SISStore.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

import disms.SISStore.client.Main;

public class ServerTest {
	static int port = 8821;

    public static void start(String filePath) {
        Socket s = null;
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                // 选择进行传输的文件
                //String filePath = "D:\\lib.rar";
                File fi = new File(filePath);

                System.out.println("文件长度:" + (int) fi.length());

                // public Socket accept() throws
                // IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。

                s = ss.accept();
                System.out.println("建立socket链接");
                DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                dis.readByte();

                DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
                DataOutputStream ps = new DataOutputStream(s.getOutputStream());
                //将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
                //System.out.println(Main.UserName);
                ps.writeUTF(Main.UserName);                
                ps.flush();
                /*
                Calendar ca = Calendar.getInstance();   
    			int year = ca.get(Calendar.YEAR);//获取年份   
    	        int month=ca.get(Calendar.MONTH);//获取月份    
    	        int day=ca.get(Calendar.DATE);//获取日   
    	        int minute=ca.get(Calendar.MINUTE);//分    
    	        int hour=ca.get(Calendar.HOUR);//小时    
    	        int second=ca.get(Calendar.SECOND);//秒   
    	             
    	        String date = year + "年" + (month + 1 )+ "月" + day + "日"+ hour + "时" + minute + "分" + second + "秒";
				*/
                //ps.writeUTF(date);
                //ps.flush();
                ps.writeUTF(fi.getName());
                ps.flush();
                ps.writeLong((long) fi.length());
                ps.flush();

                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];

                while (true) {
                    int read = 0;
                    if (fis != null) {
                        read = fis.read(buf);
                    }

                    if (read == -1) {
                        break;
                    }
                    ps.write(buf, 0, read);
                }
                ps.flush();
                // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
                // 直到socket超时，导致数据不完整。                
                fis.close();
                s.close();                
                System.out.println("文件传输完成");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
    	new ServerTest();
		ServerTest.start(args[0]);
    }
}
