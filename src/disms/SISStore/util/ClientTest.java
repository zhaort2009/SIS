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

    private static String ip = "localhost";// ���óɷ�����IP

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
            System.out.print("���ӷ������ɹ�!" + "\n");
            return true;
        } catch (Exception e) {
            System.out.print("���ӷ�����ʧ��!" + "\n");
            return false;
        }

    }

    private static void sendMessage() {
        if (cs == null)
            return;
        try {
            cs.sendMessage(sendMessage);
        } catch (Exception e) {
            System.out.print("������Ϣʧ��!" + "\n");
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
            System.out.print("������Ϣ�������\n");
            return;
        }

        try {
        	
            Calendar ca = Calendar.getInstance();   
			int year = ca.get(Calendar.YEAR);//��ȡ���   
	        int month=ca.get(Calendar.MONTH);//��ȡ�·�    
	        int day=ca.get(Calendar.DATE);//��ȡ��   
	        int minute=ca.get(Calendar.MINUTE);//��    
	        int hour=ca.get(Calendar.HOUR);//Сʱ    
	        int second=ca.get(Calendar.SECOND);//��   
	             
	        String date = year + "��" + (month + 1 )+ "��" + day + "��"+ hour + "ʱ" + minute + "��" + second + "��";
			
            //���ر���·�����ļ������Զ��ӷ������˼̳ж�����
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
            System.out.println("�ļ��ĳ���Ϊ:" + len + "\n");
            System.out.println("��ʼ�����ļ�!" + "\n");
                    
            while (true) {
                int read = 0;
                if (inputStream != null) {
                    read = inputStream.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                //�����������Ϊͼ�ν����prograssBar���ģ���������Ǵ��ļ������ܻ��ظ���ӡ��һЩ��ͬ�İٷֱ�
                System.out.println("�ļ�������" +  (passedlen * 100/ len) + "%\n");
                fileOut.write(buf, 0, read);
            }
            System.out.println("������ɣ��ļ���Ϊ" + Path + "\n");

            fileOut.close();
        } catch (Exception e) {
            System.out.println("������Ϣ����" + "\n");
            return;
        }
    }

    public static void main(String args[]) {
    	new ClientTest();
		ClientTest.start(args[0]);
    }
}