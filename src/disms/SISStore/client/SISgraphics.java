package disms.SISStore.client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.io.File;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;


public class SISgraphics extends JFrame implements ChangeListener,ActionListener{
	Environment myDbEnvironment = null;
	Database myDatabase = null;
	EnvironmentConfig envConfig = null;
	DatabaseConfig dbConfig = null;
	private static final long serialVersionUID = 1L;
	String user = "NO.001";
	//JFrame下的
	JPanel jp = new JPanel();
	//JTabbedPane下的
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	//jp1下的
	JPanel jp11 = new JPanel();
	JPanel jp21 = new JPanel();
	JTabbedPane jtp1 = new JTabbedPane(JTabbedPane.TOP);
	int initROWS = 0;
	int initCOLS1 = 2;
	int initCOLS2 = 3;
	public static JTextArea jl=new JTextArea(5,1);
	JButton jb1 = new JButton("选择备份文件");
	JButton jb2 = new JButton("开始");
	JButton jb3 = new JButton("导入备份文件信息");
	JButton jb4 = new JButton("还原文件");
	JButton jb5 = new JButton("删除文件");
	JButton jb6 = new JButton("删除选项");
	Vector<String> cmd = new Vector<String>();
	String filePath = "D:\\SIS\\"+user+"\\FileInfo\\";
	Vector<String> txt = new Vector<String>();
	Set fileNameSet = SearchStr.fileNameSet(filePath);
	Vector rows = new Vector();
	final Vector<String> luid = new Vector<String>();
	Vector cols = new Vector();
	Vector fs = new Vector();
	public void setUser(String name){
		user = name;
	}
	public void example(){
		String aKey = "myFirstKey";
		String aData = "myFirstData";
		try {
		DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
		myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		// Exception handling goes here
		}
		 try { 
		      // Create a pair of DatabaseEntry objects. theKey
		      // is used to perform the search. theData is used 
		      // to store the data returned by the get() operation.
		     DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		     DatabaseEntry theData = new DatabaseEntry();
		      
		     // Perform the get.
		      if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
		            // Recreate the data String.
		             byte[] retData = theData.getData();
		             String foundData = new String(retData);
		             System.out.println("For key: '" + aKey + "' found data: '" + foundData + "'.");
		      } else {
		          System.out.println("No record found for key '" + aKey + "'."); 
		     }
		  } catch (Exception e) {    // Exception handling goes here}
		  }
	}
    public void createDB(){
		try {    
		    // Open the environment. Create it if it does not already exist.    
		    envConfig = new EnvironmentConfig();    
		    envConfig.setAllowCreate(true); 
		    envConfig.setTransactional(true);   
		    myDbEnvironment = new Environment(new File("D:/SIS/dbEnv"), envConfig);    
		    
		    // Open the database. Create it if it does not already exist.    
		    dbConfig = new DatabaseConfig();    
		    dbConfig.setAllowCreate(true); 

		    myDatabase = myDbEnvironment.openDatabase(null,"sampleDatabase", dbConfig); 
		} 
		catch (DatabaseException dbe) {    // Exception handling goes here}
		}
	}
	public void closeDB(){
			try {
		        if (myDatabase != null) {
		            myDatabase.close();
		        }
		        if (myDbEnvironment != null) {
		            myDbEnvironment.close();
		        }
		} catch (DatabaseException dbe) {
		    // 错误处理
		}
	}
	public void cursorDB() throws UnsupportedEncodingException{
		Cursor cursor = null;
		 // Open the cursor. 
		try {

	    cursor = myDatabase.openCursor(null, null);

	    // Cursors need a pair of DatabaseEntry objects to operate. These hold
	    // the key and data found at any given position in the database.
	    DatabaseEntry foundKey = new DatabaseEntry();
	    DatabaseEntry foundData = new DatabaseEntry();

	    // To iterate, just call getNext() until the last database record has been 
	    // read. All cursor operations return an OperationStatus, so just read 
	    // until we no longer see OperationStatus.SUCCESS
	    while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
	        OperationStatus.SUCCESS) {
	        // getData() on the DatabaseEntry objects returns the byte array
	        // held by that object. We use this to get a String value. If the
	        // DatabaseEntry held a byte array representation of some other data
	        // type (such as a complex object) then this operation would look 
	        // considerably different.
	        String keyString = new String(foundKey.getData());
	        String dataString = new String(foundData.getData(),"UTF-8");
	        txt.addElement(dataString);
	        System.out.println("Key - Data : " + keyString + " - " + dataString + "");
	    }
	} catch (DatabaseException de) {
	    System.err.println("Error accessing database." + de);
	} finally {
	    // Cursors must be closed.
	    cursor.close();
	}
	}
	public void addElement(String key,String data){
		String aKey = key;
		String aData = data;
		try {
		DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
		myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		// Exception handling goes here
		}
		
	}
	public void deletePairs(String key){
		try {
		    String aKey = key;
		    DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		    
		    // Perform the deletion. All records that use this key are
		    // deleted.
		    myDatabase.delete(null, theKey); 
		    System.out.println("delete successfully!");
		} catch (Exception e) {
		    // Exception handling goes here
		}
	}
	public void getValue(String key){
		 try { 
		      // Create a pair of DatabaseEntry objects. theKey
		      // is used to perform the search. theData is used 
		      // to store the data returned by the get() operation.
		     DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
		     DatabaseEntry theData = new DatabaseEntry();
		      
		     // Perform the get.
		      if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { 
		            // Recreate the data String.
		             byte[] retData = theData.getData();
		             String foundData = new String(retData);
		             System.out.println("For key: '" + key + "' found data: '" + foundData + "'.");
		      } else {
		          System.out.println("No record found for key '" + key + "'."); 
		     }
		  } catch (Exception e) {
			  // Exception handling goes here}
		  }
	}
	public SISgraphics(String name) throws UnsupportedEncodingException{
		//Frame下的

		user = name;
		String FilePath = "D:\\SIS\\"+user+"\\FileInfo\\";
		this.setLayout(new BorderLayout());
		this.setTitle("SIS");
		this.setBounds(300, 300, 600, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp.setLayout(new BorderLayout());
		this.add(jp,BorderLayout.CENTER);		
		
		jp.add(jtp1,BorderLayout.CENTER);
		jtp1.add("备份文件",jp1);
		jtp1.add("删除或还原文件",jp2);
		
		this.add(jl,BorderLayout.SOUTH);
		jl.setEditable(false);

		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new BorderLayout());
		
		jp1.add(jp11,BorderLayout.NORTH);
		jp11.setLayout(new FlowLayout());
		jp11.add(jb1);
		jp11.add(jb2);
		jp11.add(jb6);

		final JTable tb1 = new JTable(initROWS,initCOLS1);
		final DefaultTableModel temp1 = (DefaultTableModel)tb1.getModel();
		tb1.getColumnModel().getColumn(0).setHeaderValue("文件名");
		tb1.getColumnModel().getColumn(1).setHeaderValue("文件路径");
		JScrollPane scrollPane1 = new JScrollPane(tb1);
		jp1.add(scrollPane1,BorderLayout.CENTER);
		
		jp2.add(jp21,BorderLayout.NORTH);
		jp21.setLayout(new FlowLayout());
		jp21.add(jb3);
		jp21.add(jb4);
		jp21.add(jb5);
		initTab();
		final JTable tb2 =  new JTable(rows,cols);
		final DefaultTableModel temp2 = (DefaultTableModel)tb2.getModel();
		final JScrollPane scrollPane2 = new JScrollPane(tb2);
		final Vector<Integer> sel = new Vector<Integer>();
		sel.addElement(-100);
		final Vector<Integer> sel2 = new Vector<Integer>();
		sel2.addElement(-100);
		jp2.add(scrollPane2,BorderLayout.CENTER);
		tb2.setVisible(false);
			
		jb1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				chooseFile(fs);
				for(int i = 0;i < fs.size(); i++){
					File tmp  = new File(fs.get(i).toString());
					String[] t = {tmp.getName(),tmp.toString()};
					temp1.addRow(t);
					cmd.addElement(user+"*backup*"+tmp.toString());
				}
			}
		});
		jb2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jl.setText("");
				SISCLIENT bcp = null;
				try {
					bcp = new SISCLIENT();
				} catch (UnknownHostException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				for(int i = 0;i<cmd.size();i++){
					String[] c = cmd.get(i).toString().split("[*]");
					try {
						bcp.initService(c);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				cmd.removeAllElements();
				fs.removeAllElements();
				int j = temp1.getRowCount();
				for(int i = 0;i < j;i++){
					temp1.removeRow(0);
				}
				int k = temp2.getRowCount();
				for(int i = 0;i < k;i++){
					temp2.removeRow(0);
				}
				try {
					initTab();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		jb3.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tb2.setVisible(true);
				int j = temp2.getRowCount();
				for(int i = 0;i < j;i++){
					temp2.removeRow(0);
				}
				try {
					initTab();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
			
		});
		jb4.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				jl.setText("");
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				jfc.showSaveDialog(new javax.swing.JFrame());
				String path = jfc.getSelectedFile().toString()+"\\";
				while(tb2.getSelectedRow()>=0){
//					System.out.println(luid.elementAt(tb2.getSelectedRow()));
					String[] tmp = luid.elementAt(tb2.getSelectedRow()).toString().split("[*]");
					String[] tmp2 = tmp[1].split("[.]");
					String tmp3 = tmp2[0]+'.'+tmp2[1]+'.'+tmp2[2];
					//+tb2.getValueAt(tb2.getSelectedRow(), 2)+" "
					String des = tmp[1].replace(":", ".");
					String filePath = path+'['+des+']'+tb2.getValueAt(tb2.getSelectedRow(), 0);
					File f = new File(filePath);
					if(f.exists()){
						JOptionPane jop = new JOptionPane();
						jop.showMessageDialog(null, "已存在"+filePath+"！"+"请重新选择文件路！");
						JFileChooser j = new JFileChooser();
						j.showSaveDialog(new javax.swing.JFrame());
						filePath = j.getSelectedFile().toString();
					}
					String cmd = user+"*restore*"+tmp[0]+'*'+tmp3+'*'+filePath;
					System.out.println(cmd);
					String[] L1 = cmd.split("[*]");	
					SISCLIENT res = null;
					try {
						res = new SISCLIENT();
					} catch (UnknownHostException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						res.initService(L1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int i = tb2.getSelectedRow();
					tb2.removeRowSelectionInterval(i, i);
					sel2.removeElement(i);
			    	SISgraphics.jl.append("The file has been restored to "+filePath+'\n');
				}
				
			}			
		});
		jb5.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				jl.setText("");
				for(int k = sel2.size()-1; k > 0  ; k--)
				{
					String[] tmp = luid.elementAt(sel2.get(k)).toString().split("[*]");
					String[] tmp2 = tmp[1].split("[.]");
					String tmp3 = tmp2[0]+'.'+tmp2[1]+'.'+tmp2[2];
					String cmd = user+"*delete*"+tmp[0]+'*'+tmp3;
					System.out.println(cmd);
					String[] L1 = cmd.split("[*]");	
					for(int m = 0;m < L1.length;m++)
						System.out.println(L1[m]);
					SISCLIENT res;
					try {
						res = new SISCLIENT();
						res.initService(L1);	
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					};
							
						
					
					System.out.println(sel2.get(k)+"abc");
					temp2.removeRow((sel2.get(k)));	
				}
				sel2.removeAllElements();
				sel2.addElement(-100);
			    SISgraphics.jl.append("These files has been deleted"+'\n');
			}
		});
		jb6.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel temp = (DefaultTableModel)tb1.getModel();
				//int[] i = tb1.getSelectedRows();
				for(int k = sel.size()-1; k > 0  ; k--)
				{
					temp1.removeRow((sel.get(k)));	
					cmd.removeElementAt((sel.get(k)));
				}
				sel.removeAllElements();
				sel.addElement(-100);
			}
		});
		tb1.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseReleased(java.awt.event.MouseEvent e) {				
				int[] i = tb1.getSelectedRows();
				tb1.clearSelection();
				tb1.selectAll();
				tb1.getRowCount();
				for(int k = 0;k < i.length;k++)
				{
					if(sel.indexOf((Object)i[k])>0){
						sel.removeElement((Object)i[k]);
					}
					else
						sel.addElement(i[k]);
				}
				
				System.out.println(sel);
				for(int j = 0;j < tb1.getRowCount();j++)
					if(sel.indexOf(j)<0)
						tb1.removeRowSelectionInterval(j, j);
			}
		});
		tb2.addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseReleased(java.awt.event.MouseEvent e) {				
				int[] i = tb2.getSelectedRows();
				tb2.clearSelection();
				tb2.selectAll();
				tb2.getRowCount();
				for(int k = 0;k < i.length;k++)
				{
					if(sel2.indexOf((Object)i[k])>0){
						sel2.removeElement((Object)i[k]);
					}
					else
						sel2.addElement(i[k]);
				}
				System.out.println(sel2);
				for(int j = 0;j < tb2.getRowCount();j++)
					if(sel2.indexOf(j)<0)
						tb2.removeRowSelectionInterval(j, j);
			}
		});
		
	}
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void chooseFile(Vector fs){
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(true);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showOpenDialog(new javax.swing.JFrame());
		File[] sf = jfc.getSelectedFiles();
		for(int i = 0; i < sf.length;i++ ){
			if(!sf[i].isDirectory())
				fs.addElement(sf[i]);
			else
				fileList(sf[i],fs);
		}
		System.out.println(fs);
	}
	public void fileList(File file,Vector vt) {
        File[] files = file.listFiles();
        if (files != null) {
              for (File f : files) {
            	  if(!f.isDirectory())
            	  	vt.addElement(f.getPath());
                    fileList(f,vt);
              }
        }
   }
	public static void fileListTree(File file,int node) {
        node++;
        File[] files = file.listFiles();
        if (files != null) {
              for (File f : files) {
                    for(int i=0;i<node;i++){
                         if(i==node-1){
                               System.out.print("├");
                         }
                         else{
                               System.out.print(" ");
                         }
                    }
                    System.out.println(f.getName());
                    fileListTree(f,node);
              }
        }
   }
	public static void main(String[] args) throws UnsupportedEncodingException{
		new SISgraphics("NO.001");
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
	}
	public void initTab() throws UnsupportedEncodingException{
		cols.removeAllElements();
		rows.removeAllElements();
		txt.removeAllElements();
		luid.removeAllElements();
//		for(Iterator i = new TreeSet(fileNameSet).iterator() ; i.hasNext() ; ){
//			BufferedReader in = null;
//			try {
//				in = new BufferedReader(new FileReader(i.next().toString()));
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			String line = null;
//			try {
//				line = in.readLine();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			while(line != null){
//				txt.addElement(line);
//				try {
//					line = in.readLine();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		}
		createDB();
		cursorDB();
		closeDB();
		for(int i = 0;i < txt.size();i++){
			String[] str = txt.elementAt(i).toString().split("[*]");
			Vector row = new Vector();
			row.addElement(str[4]);
			row.addElement(str[5]);
			row.addElement(str[0]);
			String tmp = str[1]+'*'+str[0];
			luid.addElement(tmp);
			rows.addElement(row);
		}
		cols.addElement("文件名");
		cols.addElement("文件路径");
		cols.addElement("备份时间");
	}
}