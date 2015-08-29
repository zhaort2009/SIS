package disms.SISStore.test;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import disms.SISStore.client.ThreadPool;

public class EchoServer {
  private int port=8000;
  private ServerSocket serverSocket;
  private ExecutorService executorService; //线程池
  private final int POOL_SIZE=4;  //单个CPU时线程池中工作线程的数目
  
  private int portForShutdown=8001;  //用于监听关闭服务器命令的端口
  private ServerSocket serverSocketForShutdown;
  private boolean isShutdown=false; //服务器是否已经关闭

  private Thread shutdownThread=new Thread(){   //负责关闭服务器的线程
    public void start(){
      this.setDaemon(true);  //设置为守护线程（也称为后台线程）
      super.start();
    }

    public void run(){
      while (!isShutdown) {
        Socket socketForShutdown=null;
        try {
          socketForShutdown= serverSocketForShutdown.accept();
          BufferedReader br = new BufferedReader(
                            new InputStreamReader(socketForShutdown.getInputStream()));
          String command=br.readLine();
         if(command.equals("shutdown")){
            long beginTime=System.currentTimeMillis(); 
            socketForShutdown.getOutputStream().write("服务器正在关闭\r\n".getBytes());
            isShutdown=true;
            //请求关闭线程池
//线程池不再接收新的任务，但是会继续执行完工作队列中现有的任务
            executorService.shutdown();  
            
            //等待关闭线程池，每次等待的超时时间为30秒
            while(!executorService.isTerminated())
              executorService.awaitTermination(30,TimeUnit.SECONDS); 
            
            serverSocket.close(); //关闭与EchoClient客户通信的ServerSocket 
            long endTime=System.currentTimeMillis(); 
            socketForShutdown.getOutputStream().write(("服务器已经关闭，"+
                "关闭服务器用了"+(endTime-beginTime)+"毫秒\r\n").getBytes());
            socketForShutdown.close();
            serverSocketForShutdown.close();
            
          }else{
            socketForShutdown.getOutputStream().write("错误的命令\r\n".getBytes());
            socketForShutdown.close();
          }  
        }catch (Exception e) {
           e.printStackTrace();
        } 
      } 
    }
  };

  public EchoServer() throws IOException {
    serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(60000); //设定等待客户连接的超过时间为60秒
    serverSocketForShutdown = new ServerSocket(portForShutdown);

    //创建线程池
    executorService= Executors.newFixedThreadPool( 
	    Runtime.getRuntime().availableProcessors() * POOL_SIZE);
    
    shutdownThread.start(); //启动负责关闭服务器的线程
    System.out.println("服务器启动");
  }
  
  public void service() {
    while (!isShutdown) {
      Socket socket=null;
      try {
        socket = serverSocket.accept();  //可能会抛出SocketTimeoutException和SocketException
        socket.setSoTimeout(60000);  //把等待客户发送数据的超时时间设为60秒          
        executorService.execute(new Handler1(socket));  //可能会抛出RejectedExecutionException
      }catch(SocketTimeoutException e){
         //不必处理等待客户连接时出现的超时异常
      }catch(RejectedExecutionException e){
         try{
           if(socket!=null)socket.close();
         }catch(IOException x){}
         return;
      }catch(SocketException e) {
         //如果是由于在执行serverSocket.accept()方法时，
         //ServerSocket被ShutdownThread线程关闭而导致的异常，就退出service()方法
         if(e.getMessage().indexOf("socket closed")!=-1)return;
       }catch(IOException e) {
         e.printStackTrace();
      }
    }
  }

  public static void main(String args[])throws IOException {
    new EchoServer().service();
  }
}

class Handler1 implements Runnable{
  private Socket socket;
  private Socket fileSoc; 
  private final int poolSize = 3;
  private ThreadPool filePool = new ThreadPool(Runtime.getRuntime().availableProcessors()*poolSize);
 
  private ServerSocket serSocket = null;  
  public Handler1(Socket socket) throws IOException{
    this.socket=socket;
    serSocket = new ServerSocket(10000);
  }
  private PrintWriter getWriter(Socket socket)throws IOException{
    OutputStream socketOut = socket.getOutputStream();
    return new PrintWriter(socketOut,true);
  }
  private BufferedReader getReader(Socket socket)throws IOException{
    InputStream socketIn = socket.getInputStream();
    return new BufferedReader(new InputStreamReader(socketIn));
  }
  public String echo(String msg) {
    return "echo:" + msg;
  }
  public void run(){
    try {
      System.out.println("New connection accepted " +
      socket.getInetAddress() + ":" +socket.getPort());
      BufferedReader br =getReader(socket);
      PrintWriter pw = getWriter(socket);

      String msg = null;
//      while ((msg = br.readLine()) != null) {
      msg = br.readLine();
      System.out.println(msg);
      pw.println("ok");
      while(true){
	      fileSoc = serSocket.accept();
	      filePool.execute(new HandlerTrans1(fileSoc,msg));
      }
//        if (msg.equals("bye"))
//          break;
//      }
      
    }catch (IOException e) {
       e.printStackTrace();
    }finally {
       try{
         if(socket!=null)socket.close();
       }catch (IOException e) {e.printStackTrace();}
    }
  }
}

class HandlerTrans1 implements Runnable{
	private Socket socket;
	InputStream inSocket;
    byte byteBuffer[] = new byte[1024];
    RandomAccessFile inFile = null;
    public static int i = 0;
	public HandlerTrans1(Socket soc,String filePath) throws IOException{
		socket = soc;
	    inSocket = socket.getInputStream();          
	    File savedFile = new File(filePath);
	    i++;
        inFile = new RandomAccessFile(savedFile, "rw");    
	}
	public void run() {
		// TODO Auto-generated method stub
		int amount;
	    try {
			while ((amount = inSocket.read(byteBuffer)) != -1) {
	            inFile.write(byteBuffer, 0, amount);
	        }
	        // 关闭流
	        inSocket.close();
	        System.out.println("Get OK");
	        System.out.println("接收完毕!");
	        // 关闭文件
	        inFile.close();
	        // 关闭临时套接字
	        socket.close();      
	       
	    } catch (IOException e) {
	        System.out.println(e.toString());
	        e.printStackTrace();
	    }
	}
	
}
