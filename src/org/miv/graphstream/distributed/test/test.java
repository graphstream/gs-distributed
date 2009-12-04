package org.miv.graphstream.distributed.test;

import java.io.IOException;
import java.rmi.RemoteException;

import org.miv.graphstream.distributed.graph.DistGraphClient;
import org.miv.graphstream.distributed.io.DistGraphConverterDGS;
import org.miv.graphstream.distributed.io.GraphListenerDist;
import org.miv.graphstream.distributed.rmi.DistGraphServer;
import org.miv.graphstream.graph.implementations.SingleGraph;
import org.miv.graphstream.io2.file.FileInputDGS;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("coucou");
		SingleGraph g = new SingleGraph();
		g.addNode("coucou");
		//demo() ;
		//testio();
		test_multi();
		//graph_convert();
	}



	/*
	 * demo
	 */
	public static void demo() {
		//try {
			DistGraphClient c = new DistGraphClient();

			//c.addDistGraph("rmi:127.0.0.1/g1:DefaultGraph", true);
			//c.addDistGraph("rmi:127.0.0.1/g2:DefaultGraph", true);

			//DistGraphServer g1 = c.getDistGraphServer("g1");
			//DistGraphServer g2 = c.getDistGraphServer("g2");

			/*g1.exec("addNode", new String[] {"n1"});
			g1.exec("addNode", new String[] {"n2"});
			g2.exec("addNode", new String[] {"n3"});
			g1.exec("eddEdge", new String[] {"e1", "n1", "g2/n3"});

			System.out.println("getNodeCount : " + g1.exec("getNodeCount", new Object[] {}));
			System.out.println("getNodeCount : " + g2.exec("getNodeCount", new Object[] {}));*/

		//}
		//catch(RemoteException e) {
		//	System.out.println("" + e.getMessage());
		//}
	}

	/*
	 * testio
	 */
	public static void testio() {
		try {
		FileInputDGS f = new FileInputDGS();
		GraphListenerDist l = new GraphListenerDist();

		f.addGraphListener(l);
		//f.begin("/home/baudryj/workspace-java/gs-distributed/bin/org/miv/graphstream/distributed/data/madhoc_170stations_v2.dgs");
		f.begin("/home/baudryj/workspace-gs/gs-distributed/bin/org/miv/graphstream/distributed/data/aaa.dgs");

		while(f.nextEvents()) {
		}

		/*FileOutputDGS o = new FileOutputDGS();
		o.*/

		}
		catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
		}
	}

	/*
	 * multi
	 */
	public static void test_multi() {
		try {
			DistGraphClient c = new DistGraphClient();

			c.addDistGraph("rmi:127.0.0.1/g1:DefaultGraph", true);
			c.addDistGraph("rmi:127.0.0.1/g2:DefaultGraph", true);

			DistGraphServer g1 = c.getDistGraphServer("g1");
			DistGraphServer g2 = c.getDistGraphServer("g2");

			String[] methods = new String[] {"addNode", "addNode", "addNode", "addNode", "addNode"} ;
			String[][] params = new String[][] {new String[] {"g1"}, new String[] {"g2"}, new String[] {"g3"}, new String[] {"g4"}, new String[] {"g5"}} ;

			String[] methods2 = new String[] {"addEdge", "addEdge", "addEdge", "addEdge", "addEdge"} ;
			String[][] params2 = new String[][] {new String[] {"e1","g1", "g2"}, new String[] {"e2","g1", "g3"}, new String[] {"e3","g1", "g4"}, new String[] {"e4","g1", "g5"}, new String[] {"e5","g2", "g5"}} ;

			g1.exec(methods, params);
			g1.exec(methods2, params2);
			System.out.println("getNodeCount : " + g1.exec("getNodeCount", new Object[] {}));
			int[] res = ((int[])g1.exec("getEdgeCount", new Object[] {}));
			System.out.println("getEdgeCount : " + res[0]);
		}
		catch(RemoteException e) {
			System.out.println("" + e.getMessage());
		}
	}

	/*
	 * graph convert
	 */
	public static void graph_convert() {
		DistGraphConverterDGS conv = new DistGraphConverterDGS();

	}

}
