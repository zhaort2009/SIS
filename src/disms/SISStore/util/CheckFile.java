package disms.SISStore.util;
import java.io.*;
import java.util.*;

public class CheckFile{
 public static void main(String[] args) throws IOException{
	String FilePath = "D:\\SIS\\NO.001\\FileInfo\\";
	Vector txt = new Vector();
	
	for(int i = 0;i < txt.size();i++){
		System.out.println(txt.elementAt(i));
	}

    Set fileNameSet = SearchStr.fileNameSet(FilePath);
	for(Iterator i = new TreeSet(fileNameSet).iterator() ; i.hasNext() ; ){
//		System.out.println(i.next().toString());
		BufferedReader in = new BufferedReader(new FileReader(i.next().toString()));
		String line = in.readLine();
		while(line != null){
			txt.addElement(line);
			line = in.readLine();
		}
	}	 
	Vector rows = new Vector();
	for(int i = 0;i < txt.size();i++){
		String[] str = txt.elementAt(i).toString().split("[*]");
		Vector row = new Vector();
		row.addElement(str[4]);
		row.addElement(str[5]);
		row.addElement(str[0]);
		rows.addElement(row);
	}
	for(int i = 0;i < rows.size();i++){
		System.out.println(rows.elementAt(i));
	}

	 
	 
 };
}