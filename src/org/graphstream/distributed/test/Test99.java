package org.graphstream.distributed.test;

import java.io.IOException;
import java.util.HashMap;

import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;
import org.graphstream.distributed.stream.DGraphSink;
import org.graphstream.distributed.stream.FileSinkDGSDGraph;
import org.graphstream.stream.file.FileSourceDGS1And2;


public class Test99 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("coucou");
		RMIDGraphAdapter g = RMIHelper.register("rmi:localhost:1099/g1:DefaultGraph");
		//Graph g = new SingleGraph("1");
		//g.addNode("coucou");
		//g.addNode("coucou2");
		//newtest();
		//demo() ;
		//testio();
		//test_multi();
		//graph_convert();
		//Graph g2 ;
		//testEnum();
		//DGraph_test1();
	}



	/*
	 * demo distributed
	 */
	/*public static void demo() {
		try {
			DGraphClientOld c = new DGraphClientOld();

			c.addDistGraph("rmi:127.0.0.1/g1:DefaultGraph", true);
			c.addDistGraph("rmi:127.0.0.1/g2:DefaultGraph", true);

			RMIDGraphAdapter g1 = c.getDistGraphServer("g1");
			RMIDGraphAdapter g2 = c.getDistGraphServer("g2");

			g1.exec("addNode", new String[] {"n1"});
			g1.exec("addNode", new String[] {"n2"});
			g2.exec("addNode", new String[] {"n3"});
			g2.exec("addNode", new String[] {"n4"});
			g1.exec("addEdge", new String[] {"e1", "n1", "n2"});
			g1.exec("addEdge", new String[] {"e2", "n3", "n4"});
			g2.exec("addEdge", new String[] {"e3", "n2", "g1/n1"});
			g1.exec("addEdge", new String[] {"e4", "n1", "g2/n3"});

			int[] res = (int[])g1.exec("getEdgeCount", new Object[] {}) ;
			
			System.out.println("getNodeCount : " + g1.exec("getNodeCount", new Object[] {}));
			System.out.println("getNodeCount : " + g2.exec("getNodeCount", new Object[] {}));
			System.out.println("getEdgeCount : " + g2.exec("getEdgeCount", new Object[] {}));
			System.out.println("getEdge : "+res[0]);
		}
		catch(RemoteException e) {
			System.out.println("demo error " + e.getMessage());
		}
	}*/

	/*
	 * test io client
	 */
	public static void testio() {
		try {
		FileSourceDGS1And2 f = new FileSourceDGS1And2();
		FileSinkDGSDGraph o = new FileSinkDGSDGraph();
		DGraphSink o2 = new DGraphSink();
		//o.begin("out.dgs");
		f.addSink(o2);
		
		//GraphListenerDist l = new GraphListenerDist();
		//f.addGraphListener(l);
		f.begin("/home/baudryj/workspace-gs/gs1-distributed/bin/org/graphstream/distributed/stream/files/madhoc_170stations_v2.dgs");
		//f.begin("/home/baudryj/workspace-gs/gs1-distributed/bin/org/graphstream/distributed/stream/files/aaa.dgs");
		
		int i = 0 ;
		while(f.nextEvents() && i<100) {
			i++;
			System.out.println("new Elements " + i);
		}

		/*FileOutputDGS o = new FileOutputDGS();
		o.*/

		}
		catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
		}
	}

	/*
	 * envoi de blocs d'evenements
	 */
	/*public static void test_multi() {
		try {
			DGraphClientOld c = new DGraphClientOld();

			c.addDistGraph("rmi:127.0.0.1/g1:DefaultGraph", true);
			c.addDistGraph("rmi:127.0.0.1/g2:DefaultGraph", true);

			RMIDGraphAdapter g1 = c.getDistGraphServer("g1");
			RMIDGraphAdapter g2 = c.getDistGraphServer("g2");

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
	}*/

	/*
	 * graph convert
	 */
	public static void graph_convert() {
		//DistGraphConverterDGS conv = new DistGraphConverterDGS();
	}
	
	public static void testEnum() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("k1", "v1");
		h.put("k2", "v2");
		h.put("k3", "v3");
		for(String k : h.values()) {
			System.out.println("k = " + k);
		}
	}
	

	public static void newtest() {
		RMIDGraphAdapter g = RMIHelper.register("rmi:localhost:1099/g1:DefaultGraph");
	
	}

}
