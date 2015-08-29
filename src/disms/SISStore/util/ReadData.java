package disms.SISStore.util;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
public class ReadData{
    /**
     * ��ȡ�ļ�ָ���С�
     */
    public static void main(String[] args) throws IOException {
        // ָ����ȡ���к�
        int lineNumber = 3;
        // ��ȡ�ļ�
        File sourceFile = new File("E:\\2007.txt");
        // ��ȡָ������
        readAppointedLineNumber(sourceFile, lineNumber);
        // ��ȡ�ļ������ݵ�������
        System.out.println(getTotalLines(sourceFile));
    }
    // ��ȡ�ļ�ָ���С�
	static void readAppointedLineNumber(File sourceFile, int lineNumber)
            throws IOException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
            System.out.println("�����ļ���������Χ֮�ڡ�");
        }
        else
        {
        	String line="";
            int i=0;
            while(i<lineNumber){
                line=reader.readLine();
                i++;
            }
            System.out.println(line);            
        	/*reader.setLineNumber(lineNumber);            
            s = reader.readLine();
            System.out.println(s);
           while (s != null) {
            		System.out.println("��ǰ�к�Ϊ:" + reader.getLineNumber());
            		reader.setLineNumber(20);
                   	System.out.println("���ĺ��к�Ϊ:" + reader.getLineNumber());
                    System.out.println(s);
                    System.exit(0);     
                    s = reader.readLine();
            }*/
        }
        reader.close();
        in.close();
    }
    // �ļ����ݵ���������
    static int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }
}