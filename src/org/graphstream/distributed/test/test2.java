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
		System.out.println("test2");
		Graph g = new SingleGraph("");
		g.addNode("g1");
		g.addNode("g2");
		RMIHelper.bind("g1", "localhost");
		RMIHelper.bind("g2", "172.30.14.68");
		RMIDGraphAdapter g1 = RMIHelper.register("rmi:localhost:1099/g1:DefaultGraph");
		RMIDGraphAdapter g2 = RMIHelper.register("rmi:localhost:1099/g2:DefaultGraph");
		try {
			System.out.println("hello");
			System.out.println("hello : " + g1.hello("hello 1"));
			System.out.println("hello : " + g1.hello("hello 2"));
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}
	}

	
	
	

}
