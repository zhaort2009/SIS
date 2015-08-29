package disms.SISStore.util;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(9091);
		Socket server = ss.accept();
		System.out.println("new connection");
		ObjectInputStream oi = new ObjectInputStream(server.getInputStream());
		String [] arr = (String[]) oi.readObject();
		System.out.println(arr[0] + arr[1]);
	}
}
