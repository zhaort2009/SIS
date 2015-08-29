package disms.SISStore.util;

import disms.SISStore.client.*;
import disms.SISStore.test.ServerGetInfo;

public class TestTransInfoCnt{
	public static void main(String[] args){
		/*
		ClientTransInfo test1 = new ClientTransInfo();
		test1.conn();
		test1.sendMSG("Just test it");
		test1.sendMSG("Just test it2");
		test1.close();
		*/

		ClientGetInfo test1 = new ClientGetInfo();
		test1.conn();
		System.out.println(test1.getMSG());
		System.out.println(test1.getMSG());
		test1.close();
	}
}