package disms.SISStore.util;
import java.io.*;

public class CreateFile{
	CreateFile(String path) throws IOException{
		String[] fragment = path.split("\\\\");
		String tmp = new String();
		for(int i = 0;i<fragment.length-1;i++){
			tmp = tmp+fragment[i]+'\\';
		}
		File dir = new File(tmp);
		System.out.println(tmp);
		dir.mkdirs();
		tmp = tmp+fragment[fragment.length-1];
		File file = new File(tmp);
		System.out.println(tmp);
		file.createNewFile();
	}
	public static void main(String[] args) throws IOException{
		String dir = "D:\\f\\g\\h\\wet.rar";
		new CreateFile(dir);
	}
}