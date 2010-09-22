package graphstream.distributed.test;

import graphstream.distributed.common.DGraphParser;
import graphstream.distributed.common.EnumReg;
import graphstream.distributed.graph.DGraphNetwork;
import graphstream.distributed.rmi.RMIDGraphAdapter;
import graphstream.distributed.rmi.RMIHelper;
import graphstream.distributed.stream.DGraphSink;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSourceDGS1And2;




public class Test03 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//test01();
		testio();
		//parser();
		//testSerialisation();
	}

	//test01
	public static void test01() {
		
		Graph g = new DefaultGraph("");
		//g.addNode("n1").addAttribute("att1",  "val1");
		//g.getNode("").get
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
			g1.exec(EnumReg.DGraphNetwork, "add", new Object[] {"rmi://localhost:1099/g2"});
			g2.exec(EnumReg.DGraphNetwork, "add", new Object[] {"rmi://localhost:1099/g1"});
				
			//passage de n parametres
			g1.exec("DGraph", "addNode", new Object[] {"n1"});
			g1.exec("DGraph", "addNode", new Object[] {"n2"});
			g2.exec("DGraph", "addNode", new Object[] {"n3"});
			g1.exec("DGraph", "addNode", new Object[] {"n4"});
			
			ArrayList<Object[]> p = new ArrayList<Object[]>();
			p.add(new Object[] {"n51"});
			p.add(new Object[] {"n52"});
			p.add(new Object[] {"n53"});
			p.add(new Object[] {"n54"});
			
			//fixation de parametres
			HashMap<String, String> t = new HashMap<String, String>() ;
			t.put("ObjectName", "DGraph");
			t.put("MethodName", "addNode");
			g1.exec(EnumReg.DGraphRequestManager, "addReq", new Object[] {"R1", t});
			g1.exec("R1", p);
			
				
			// ajout edge intra
			g1.exec("DGraph", "addEdge", new String[] {"e1", "n1", "n2"});
			g1.exec("DGraph", "addEdge", new String[] {"e2", "n1", "n3"});
			// ajout edge inter graphe
			g1.exec("DGraph", "addEdge", new String[] {"e2", "n1", "g2/n3"});
			g2.exec("DGraph", "addEdge", new String[] {"e3", "n3", "g1/n4"});
			
			//g1.exec("DGraph", "getNode.addAttribute", new String[][] {{"n1"}, {"k1", "v1"}}) ;
			
			// comptage du nombre de nodes
			System.out.println("g1 getNodeCount : " + g1.exec("DGraph", "getNodeCount"));
			System.out.println("g1 getEdgeCount : " + ((HashMap)g1.exec("DGraph", "getEdgeCount")).get("Graph"));
			
			//ArrayList<String> methods = new ArrayList<String>();
			//methods.add("getNode");
			//methods.add("getId");
			
			ArrayList<String> methods2 = new ArrayList<String>();
			methods2.add("getNode");
			methods2.add("addAttribute");
			
			ArrayList<String> methods3 = new ArrayList<String>();
			methods3.add("getNode");
			methods3.add("getAttribute");
			
			ArrayList<Object[]> ps = new ArrayList<Object[]>();
			ps.add(new Object[] {"n1"});
			Object[] mm = new Object[] {"aa", "bb"};
			ps.add(new Object[] {"cc", mm});

			ArrayList<Object[]> ps2 = new ArrayList<Object[]>();
			ps2.add(new Object[] {"n1"});
			ps2.add(new Object[] {"cc"});
			
			g1.exec("Graph", methods2, ps);
			System.out.println("--> "+((Object[])g1.exec("Graph", methods3, ps2))[1]);
			
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}	
	}
	
	//testio
	public static void testio() {
		try {
		RMIHelper.bind("g1", "localhost");
		RMIHelper.bind("g2", "localhost");
		FileSourceDGS1And2 f = new FileSourceDGS1And2();
		//FileSinkDGSDGraph o = new FileSinkDGSDGraph();
		DGraphSink o2 = new DGraphSink();
		DGraphNetwork d = new DGraphNetwork();
		d.add("rmi://localhost:1099/g1");
		d.add("rmi://localhost:1099/g2");
		d.getDGraph("g1").exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
		d.getDGraph("g2").exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
		d.setDefaultDGraph("g1");
		
		o2.setDGraphNetwork(d);
		f.addSink(o2);
		
		f.begin("/home/baudryj/workspace01/gs-distributed2/src/graphstream/distributed/stream/files/madhoc_170stations_v2.dgs");
		//f.begin("/home/baudryj/workspace01/gs-distributed2/src/graphstream/distributed/stream/files/aaa.dgs");
		
		int i = 0 ;
		while(f.nextEvents() && i<200) {
			i++;
			//System.out.println("new Elements " + i);
			System.out.println("getNodeCount : " + d.getDGraph("g1").exec("DGraph", "getNodeCount", new Object[]{}));
		}
		//System.out.println("g2 getNodeCount : " + d.getDGraph("g1").exec("DGraph", "getNodeCount", new Object[]{}));

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
