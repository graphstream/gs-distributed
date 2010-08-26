package graphstream.distributed.test;

import graphstream.distributed.rmi.RMIDGraphAdapter;
import graphstream.distributed.rmi.RMIHelper;

import java.rmi.RemoteException;

public class Test02 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testSerialisation();
	}
	
	public static void testSerialisation() {	
		
		//Déploiement des objets sur le serveur RMI
		RMIHelper.bind("g1", "localhost");
		
		//Création de la partie cliente
		RMIDGraphAdapter g1 = RMIHelper.register("rmi://localhost:1099/g1");
		
		try {
			//initialisation des graphs
			//g1.exec("", "init", new Object[] {"DefaultGraph", new String[] {""}});
			g1.exec(".init", new Object[] {"DefaultGraph", new String[] {""}});
			
			//g1.exec("g1-DGraphNetwork.add", new Object[] {"rmi://localhost:1099/g2"});
			
			g1.exec("g1.addNode", new String[] {"n1"});
			g1.exec("g1.addNode", new String[] {"n2"});
			g1.exec("g1-Graph.getNode('n1').addAttribute", new Object[] {"l1", new Object[] {"v1"}});
			g1.exec("g1-Graph.getNode('n1').addAttribute", new Object[] {"l2", new Object[] {"v2"}});
			
			Object[] params = new Object[] {"l2", new Object[] {"v2"}} ;
			g1.exec("g1-Graph.getNode('n1').addAttribute", params);	
			
			System.out.println("getAttribute : " + g1.exec("g1-Graph.getNode('n1').getAttribute", new Object[] {"l1"}));
			System.out.println("getAttribute : " + g1.exec("g1-Graph.getNode('n1').getAttribute", new String[] {"l2"}));
			
		}
		catch(RemoteException e) {
			System.out.println("return function : " + e.getMessage());
		}
		
	}

}
