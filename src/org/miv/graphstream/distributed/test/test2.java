package org.miv.graphstream.distributed.test;

import java.rmi.RemoteException;

import org.miv.graphstream.distributed.graph.DistGraphClient;
import org.miv.graphstream.distributed.rmi.DistGraphServer;


public class test2 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		demo();
	}

	
	/*
	 * demo
	 */
	public static void demo() {
		try {
			DistGraphClient c = new DistGraphClient();

			c.addDistGraph("rmi:127.0.0.1/g1:DefaultGraph", true);
			c.addDistGraph("rmi:127.0.0.1/g2:DefaultGraph", true);

			DistGraphServer g1 = c.getDistGraphServer("g1");
			DistGraphServer g2 = c.getDistGraphServer("g2");

			g1.exec("addNode", new String[] {"n1"});
			g1.exec("addNode", new String[] {"n2"});
			g2.exec("addNode", new String[] {"n3"});
			g1.exec("eddEdge", new String[] {"e1", "n1", "g2/n3"});

			System.out.println("getNodeCount : " + g1.exec("getNodeCount", new Object[] {}));
			System.out.println("getNodeCount : " + g2.exec("getNodeCount", new Object[] {}));

		}
		catch(RemoteException e) {
			System.out.println("" + e.getMessage());
		}
	}
	

}
