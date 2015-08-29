package disms.SISStore.test;
import disms.SISStore.client.*;
public class ClientTest{
	public static void main(String[] args) throws Exception{
		String[] commands1 = "N0.001 restore a500b2c4-b121-4646-a646-d17979f28419".split("[ ]");		
		ClientRestore sis1 = new ClientRestore(commands1);
		/*SISCLIENT sis2 = new SISCLIENT();
		sis2.initService(commands1);*/
		///sis2.initService(commands1);
		
	}
}