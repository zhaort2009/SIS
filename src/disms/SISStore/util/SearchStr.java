package disms.SISStore.util;
import java.io.BufferedReader;   
import java.io.File;   
import java.io.FileInputStream;   
import java.io.IOException;   
import java.io.InputStreamReader;   
import java.util.ArrayList;   
import java.util.HashSet;   
import java.util.Iterator;   
import java.util.List;   
import java.util.Set;   
import java.util.TreeSet;   
  
/**  
 * 考试程序：<br>  
 * 写一个Java SE程序，要求从一个指定目录搜索其中所有文件，找到匹配的字符串(例如从  
 * C:\\apache-tomcat-6.0.20-src\\搜索所有.java文件，查找指定字符串"useBodyEncodingForURI");  
 * @author mamacmm  
 *  
 */  
public class SearchStr {
	
	public static void main(String[] args){
		new SearchStr();
		SearchStr.findStr("dsadasdas", "D:\\SIS\\FileInfo\\");
	}
  
    public static boolean findStr(String fileInfo,String path){   
        //要查找的文件夹   
        //String path = "D:\\SIS\\FileInfo\\";    
        //要查询的字符串   
        String str = "rar";   
        //设置读取文件内容时的文件编码   
        String encoding = "utf8";   
        //某个文件的内容   
        String content = "";   
        //所有文件的地址   
        Set fileNameSet = SearchStr.fileNameSet(path);   
           
        for (Iterator i = new TreeSet(fileNameSet).iterator() ; i.hasNext() ; )      
        {      
            String filePath = i.next().toString();   
            filePath = filePath.replaceAll("\\\\", "\\\\\\\\");   
//          System.out.println(filePath);   
           // if(filePath.length() > 5 && ".txt".equals(filePath.substring(filePath.length()-5, filePath.length()))){   
                try {   
                    content = SearchStr.readTxt(filePath, encoding);   
//                  System.out.println("文件里面的内容:");   
//                  System.out.println(content);   
                    if(content.indexOf(str) != -1){   
                        System.out.println("SearchStr:文件中找到匹配信息");   
                       // System.out.println(filePath);
                        return true;  
                    }
                    else{
                        System.out.println("SearchStr:文件中没找到匹配信息");
                        return false;  	
                    }
                       
                } catch (IOException e) {   
                    e.printStackTrace();   
                }   
           // }   
        }
		return false;
		  
    }   
       
       
       
    /**  
     * 读取文本文件内容  
     *   
     * @param filePathAndName  
     *            带有完整绝对路径的文件名  
     * @param encoding  
     *            文本文件打开的编码方式  
     * @return 返回文本文件的内容  
     */  
    public static String readTxt(String filePathAndName, String encoding)   
            throws IOException {   
        encoding = encoding.trim();   
        StringBuffer str = new StringBuffer("");   
        String st = "";   
        try {   
            FileInputStream fs = new FileInputStream(filePathAndName);   
            InputStreamReader isr;   
            if (encoding.equals("")) {   
                isr = new InputStreamReader(fs);   
            } else {   
                isr = new InputStreamReader(fs, encoding);   
            }   
            BufferedReader br = new BufferedReader(isr);   
            try {   
                String data = "";   
                while ((data = br.readLine()) != null) {   
                    str.append(data + " ");   
                }   
            } catch (Exception e) {   
                str.append(e.toString());   
            }   
            st = str.toString();   
        } catch (IOException es) {   
            st = "";   
        }   
        return st;   
    }   
       
       
    /**  
     * 把一个文件夹及所有子文件夹中包含的文件读取出来  
     * @param path 绝对地址，例如"c:\\test\\"  
     * @return  
     */  
    public static Set fileNameSet(String path)      
    {      
        File directory = new File(path);      
        List fileList = listAllFiles(directory);      
     
        Set fileNameSet = new HashSet(fileList.size());      
        for (int i = 0 ; i< fileList.size() ; i++)      
        {      
            File file = (File)fileList.get(i);      
            fileNameSet.add(file.getAbsolutePath());      
        }      
     
        return fileNameSet;   
     
    }   
       
    /**  
     * fileNameSet 方法的附属方法  
     * @param directory  
     * @return  
     */  
    private static List listAllFiles(File directory)      
    {      
        if (directory == null || !directory.isDirectory()){      
            return null;      
        }      
        List fileList = new ArrayList();      
        addSubFileList(directory, fileList);      
     
        return fileList;      
    }      
     
    /**  
     * fileNameSet 方法的附属方法  
     * @param file  
     * @param fileList  
     */  
    private static void addSubFileList(File file,List fileList){      
        File[] subFileArray = file.listFiles();      
        if (subFileArray == null     
            || subFileArray.length == 0     
        ){      
            return;      
        }      
     
        for (int i = 0 ; i < subFileArray.length ; i++)      
        {      
            File subFile = subFileArray[i];      
            if (subFile == null     
            ){      
                continue;      
            }      
            if (subFile.isFile()      
            ){      
                fileList.add(subFile);      
                continue;      
            }      
            else if (subFile.isDirectory()      
            ){      
                addSubFileList(subFile, fileList);      
            }      
        }      
    }    
       
}  
