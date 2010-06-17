package org.graphstream.distributed.test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumReg;
import org.graphstream.distributed.graph.DGraphNetwork;
import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;
import org.graphstream.distributed.stream.DGraphSink;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSourceDGS1And2;




public class test2 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test01();
		//testio();
		//parser();
	}

	//test01
	public static void test01() {
		
		Graph g = new DefaultGraph("");
		g.addNode("n1").addAttribute("att1",  "val1");
		//g.addNode("n1").
		
		//Déploiement des objets sur le serveur RMI
		RMIHelper.bind("g1", "localhost");
		RMIHelper.bind("g2", "localhost");
		
		//Création de la partie cliente
		RMIDGraphAdapter g1 = RMIHelper.register("rmi://localhost:1099/g1");
		RMIDGraphAdapter g2 = RMIHelper.register("rmi://localhost:1099/g2");
				
		try {
			//initialisation des graphs
			g1.exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
			g2.exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
			
			//propagation des voisins
			g1.exec(EnumReg.DGraphNetwork, "add", new String[] {"rmi://localhost:1099/g2"});
			g2.exec(EnumReg.DGraphNetwork, "add", new String[] {"rmi://localhost:1099/g1"});
				
			//operations sur des graphs
			g1.exec(EnumReg.DGraph, "addNode", new String[] {"n1"});
			g1.exec(EnumReg.DGraph, "addNode", new String[] {"n2"});
			g2.exec(EnumReg.DGraph, "addNode", new String[] {"n3"});
			g1.exec(EnumReg.DGraph, "addNode", new String[] {"n4"});
			
			//ajout de plusieurs nodes en 1 commande
			String[] methods = {"addNode", "addNode", "addNode", "addNode"} ;
			String[] objects = {EnumReg.DGraph,EnumReg.DGraph,EnumReg.DGraph,EnumReg.DGraph};
			String[][] params = {new String[] {"n8"}, new String[] {"n5"}, new String[] {"n6"}, new String[] {"n7"}};
			
			g2.exec(objects, methods, params);
			
			Object rr = "eee".isEmpty();
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("key", "value");
			g1.exec(EnumReg.GraphInDGraph+".getNode('n1').addAttribute", new Object[] {map});
			//ajout d'un attribut
			System.out.println("hasAttribute : " + g1.exec(EnumReg.GraphInDGraph+".getNode('n1').hasAttribute", new String[] {"att1"}));
			
			// ajout edge intra
			//g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e1", "n1", "n2"});
			// ajout edge inter graphe
			//g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e2", "n1", "g2/n3"});
			//g2.exec(EnumReg.DGraph, "addEdge", new String[] {"e3", "n3", "g1/n4"});
			
			// comptage du nombre de nodes
			System.out.println("g1 getNodeCount : " + g1.exec(EnumReg.DGraph, "getNodeCount", null));
			System.out.println("g2 getNodeCount : " + g2.exec(EnumReg.DGraph, "getNodeCount", null));
			
			
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}	
	}
	
	//testio
	public static void testio() {
		try {
		RMIHelper.bind("g3", "localhost");
		RMIHelper.bind("g4", "localhost");
		FileSourceDGS1And2 f = new FileSourceDGS1And2();
		//FileSinkDGSDGraph o = new FileSinkDGSDGraph();
		DGraphSink o2 = new DGraphSink();
		DGraphNetwork d = new DGraphNetwork();
		d.add("rmi://localhost:1099/g3");
		d.add("rmi://localhost:1099/g4");
		d.getDGraph("g3").exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
		d.getDGraph("g4").exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
		d.setDefaultDGraph("g3");
		
		o2.setDGraphNetwork(d);
		f.addSink(o2);
		
		f.begin("/home/baudryj/workspace01/gs-distributed2/bin/org/graphstream/distributed/stream/files/madhoc_170stations_v2.dgs");
		
		int i = 0 ;
		while(f.nextEvents() && i<20) {
			i++;
			System.out.println("new Elements " + i);
		}
		System.out.println("g2 getNodeCount : " + d.getDGraph("g2").exec(EnumReg.DGraph, "getNodeCount", null));

		}
		catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
		}
	}

	//parser
	public static void parser() {
		System.out.println("parser : " +
				DGraphParser.functionSimple("toto('id')")[0] + "-" + 
				DGraphParser.functionSimple("toto('id')")[1]);
	}
	

}
