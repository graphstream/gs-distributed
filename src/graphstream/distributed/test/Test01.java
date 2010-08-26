package graphstream.distributed.test;

import graphstream.distributed.common.EnumReg;
import graphstream.distributed.rmi.RMIDGraphAdapter;
import graphstream.distributed.rmi.RMIHelper;

import java.rmi.RemoteException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

public class Test01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test01();
	}
	
public static void test01() {	
	
		Graph g = new DefaultGraph("");
		g.setAutoCreate(true);
		g.setStrict(false);
		g.addEdge("id1", "node1", "node2");
		System.out.println("dd:" + g.getEdge("dsds"));
	
		//Déploiement des objets sur le serveur RMI
		RMIHelper.bind("g1", "localhost");
		RMIHelper.bind("g2", "localhost");

		//Création de la partie cliente
		RMIDGraphAdapter g1 = RMIHelper.register("rmi://localhost:1099/g1");
		RMIDGraphAdapter g2 = RMIHelper.register("rmi://localhost:1099/g2");
				
		try {
			//initialisation des graphs
			g1.exec(".init", new Object[] {"DefaultGraph", new String[] {""}});
			g2.exec(".init", new Object[] {"DefaultGraph", new String[] {""}});
			
			//propagation des voisins
			g1.exec(EnumReg.DGraphNetwork+".add", new Object[] {"rmi://localhost:1099/g2"});
			g2.exec(EnumReg.DGraphNetwork+".add", new Object[] {"rmi://localhost:1099/g1"});
				
			//operations sur des graphs
			g1.exec("g1.addNode", new Object[] {"n1"});
			g1.exec("g1.addNode", new Object[] {"n2"});
			g2.exec("g2.addNode", new Object[] {"n3"});
			g1.exec("g1.addNode", new Object[] {"n4"});
						
			//ajout de plusieurs nodes en 1 commande
			String[] call = {"g2.addNode", "g2.addNode", "g2.addNode", "g2.addNode"} ;
			String[][] params = {new String[] {"n8"}, new String[] {"n5"}, new String[] {"n6"}, new String[] {"n7"}};
			
			g2.exec(call, params);
				
			// ajout edge intra
			g1.exec("g1.addEdge", new Object[] {"e1", "n1", "n2"});
			// ajout edge inter graphe
			g1.exec("g1.addEdge", new Object[] {"e2", "n1", "g2/n3"});
			
			// comptage du nombre de nodes
			System.out.println("g1 getNodeCount : " + g1.exec("g1.getNodeCount", null));
			System.out.println("g2 getNodeCount : " + g2.exec("g2.getNodeCount", null));
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}	
	}
	
/*
	public static void test2() {
	//Déploiement des objets sur le serveur RMI
	RMIHelper.bind("g1", "localhost");
	RMIHelper.bind("g2", "localhost");
	
	//Création de la partie cliente
	RMIDGraphAdapter g1 = RMIHelper.register("rmi://localhost:1099/g1");
	RMIDGraphAdapter g2 = RMIHelper.register("rmi://localhost:1099/g2");
			
	try {
		//initialisation des graphs
		g1.exec(".init2", new Object[] {"DefaultGraph", new String[] {""}});
		g2.exec(".init2", new Object[] {"DefaultGraph", new String[] {""}});
		
		//propagation des voisins
		g1.exec(EnumReg.DGraphNetwork+".add", new Object[] {"rmi://localhost:1099/g2"});
		g2.exec(EnumReg.DGraphNetwork+".add", new Object[] {"rmi://localhost:1099/g1"});
			
		//operations sur des graphs
		g1.exec("g1.addNode", new Object[] {"n1"});
		g1.exec("g1.addNode", new Object[] {"n2"});
	}
	catch(RemoteException e) {
		System.out.println("return function : " + e.getMessage());
	}
}*/

}
