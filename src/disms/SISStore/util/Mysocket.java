package disms.SISStore.util;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Mysocket {
    public static void main(String[] args) throws Exception {
    	Socket s = new Socket("localhost",9091);
    	ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
    	out.writeObject(new String[]{"123","232"});
    	out.flush();
    	out.close();
    }
}