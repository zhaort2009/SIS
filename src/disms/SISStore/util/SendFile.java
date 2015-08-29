package disms.SISStore.util;
/*�ļ����Ͷ�*/
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import javax.swing.JFileChooser;

/**
* �ڷ������˿���������� ʵ�����׽��� �������ļ�
*
* @author 
*/
//public class SendFile extends Thread {
public class SendFile{

    // Զ�̵�IP�ַ���
    String remoteIPString = null;
    // Զ�̵ķ���˿�
    int port;
    // ��ʱ�׽���
    Socket tempSocket;
    // �����ļ��õ������
    OutputStream outSocket;
    // �����͵��ļ�
    RandomAccessFile outFile;
    // �����ļ��õ���ʱ������
    byte byteBuffer[] = new byte[1024];

    /**
    * ���췽��������ѡ�����ļ���λ�� �����ⲿ����Զ�̵�ַ�Ͷ˿�
    *
    */
    public void sendTheFile(String remoteIPString,String filePath, int port) {
        try {
            this.remoteIPString = remoteIPString;
            this.port = port;

           // ѡ���͵��ļ�λ��
/*            JFileChooser jfc = new JFileChooser(".");
            File file = null;
            int returnVal = jfc.showOpenDialog(new javax.swing.JFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = jfc.getSelectedFile();

            }
*/
            File file = new File(filePath);
            outFile = new RandomAccessFile(file, "r");

        } catch (Exception e) {
        }
    }

    /**
    * �Ⱦ������Ƿ��������ȿ���
    *
    */
    public void run() {
        try {
            this.tempSocket = new Socket(this.remoteIPString, this.port);
            System.out.println("����������ӳɹ�!");
            outSocket = tempSocket.getOutputStream();

            int amount;
            System.out.println("��ʼ�����ļ�...");
            while ((amount = outFile.read(byteBuffer)) != -1) {
                outSocket.write(byteBuffer, 0, amount);
                System.out.println("�ļ�������...");
            }
            System.out.println("Send File complete");
          //  javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),"�ѷ������", "��ʾ!", javax.swing.JOptionPane.PLAIN_MESSAGE);
            outFile.close();
            tempSocket.close();

        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }

    /**
    * ���Է���
    *
    * @param args
    */
    public static void main(String args[]) {
        SendFile sf = new SendFile();
        sf.sendTheFile("127.0.0.1", "D:\\table.zip",10000);
        sf.run();

        sf.sendTheFile("127.0.0.1", "D:\\table.zip",10000);
        sf.run();

    }
}