package disms.SISStore.util;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
public class ReadData{
    /**
     * 读取文件指定行。
     */
    public static void main(String[] args) throws IOException {
        // 指定读取的行号
        int lineNumber = 3;
        // 读取文件
        File sourceFile = new File("E:\\2007.txt");
        // 读取指定的行
        readAppointedLineNumber(sourceFile, lineNumber);
        // 获取文件的内容的总行数
        System.out.println(getTotalLines(sourceFile));
    }
    // 读取文件指定行。
	static void readAppointedLineNumber(File sourceFile, int lineNumber)
            throws IOException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
            System.out.println("不在文件的行数范围之内。");
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
            		System.out.println("当前行号为:" + reader.getLineNumber());
            		reader.setLineNumber(20);
                   	System.out.println("更改后行号为:" + reader.getLineNumber());
                    System.out.println(s);
                    System.exit(0);     
                    s = reader.readLine();
            }*/
        }
        reader.close();
        in.close();
    }
    // 文件内容的总行数。
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