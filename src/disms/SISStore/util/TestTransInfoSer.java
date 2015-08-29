package disms.SISStore.util;

import disms.SISStore.server.*;

public class TestTransInfoSer{
	public static void main(String[] args){/*
			ServerGetInfo test1 = new ServerGetInfo();
			test1.conn();
			System.out.println(test1.getMSG());
			System.out.println(test1.getMSG());
			test1.close();*/
		

		ServerTransInfo test1 = new ServerTransInfo();
		test1.conn();
		test1.sendMSG("Just test it");
		test1.sendMSG("Just test it2");
		test1.close();
	}
}