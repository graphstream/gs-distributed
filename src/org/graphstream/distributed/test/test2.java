package org.graphstream.distributed.test;

import java.rmi.RemoteException;

import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;




public class test2 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Binding des objets RMI
		RMIHelper.bind("g1", "localhost");
		RMIHelper.bind("g2", "172.30.14.68");
		
		RMIDGraphAdapter g1 = RMIHelper.register("rmi:localhost:1099/g1");
		RMIDGraphAdapter g2 = RMIHelper.register("rmi:localhost:1099/g2");
		
		try {
			g1.exec(null, "", "init", new Object[] {"DefaultGraph", new String[] {""}});
			g2.exec(null, "", "init", new Object[] {"DefaultGraph", new String[] {""}});
			
			g1.exec(null, "dgraph", "addNode", new String[] {"n1"});
			g1.exec(null, "dgraph", "addNode", new String[] {"n2"});
			g2.exec(null, "dgraph", "addNode", new String[] {"n3"});
			g1.exec(null, "dgraph", "addNode", new String[] {"n4"});
			System.out.println("hello --> " + g1.hello("hello 1") + "nb node : " + g1.exec(null, "dgraph", "getNodeCount", null));
			System.out.println("hello --> " + g1.hello("hello 2"));
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}
	}

	
	
	

}
