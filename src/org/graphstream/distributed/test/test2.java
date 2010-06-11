package org.graphstream.distributed.test;

import java.rmi.RemoteException;
import java.util.Map;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumEdge;
import org.graphstream.distributed.common.EnumReg;
import org.graphstream.distributed.common.EnumUri;
import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;




public class test2 {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("coucou");
		Map<String, String> m =  DGraphParser.uri("rmi://localhost/id");
		System.out.println("parser : " + m.get(EnumUri.DGraphName) + "--" + m.get(EnumUri.Host) + "--" + m.get(EnumUri.Port));
		
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
			
			g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e1", "n1", "n2"});
			Map<String, String> e = DGraphParser.edge("n1", "g2/n3");
			System.out.println("parser: " + e.get(EnumEdge.GraphTo));
			g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e2", "n1", "g2/n3"});
			
			//g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e2", "n1", "g2/n3"});
			
			//g1.exec(EnumReg.DGraph, "addEdge", new String[] {"e1", "n1", "g2/n3"});
			
			//Objet type json
			//g1.exec(DG.addNode("n1"));
			
			//g1.exec(EnumReg.Registry, "clear", null);
			
			//System.out.println("hello --> " + g1.hello("hello 1") + "nb node : " + g1.exec(null, "dgraph", "getNodeCount", null));
			//System.out.println("hello --> " + g1.hello("hello 2"));
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}
	}

	
	
	

}
