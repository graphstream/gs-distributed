package org.miv.graphstream.distributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.miv.graphstream.distributed.rmi.GraphRmi;

public class GraphConnectorFactory {

	// Constructor
	public GraphConnectorFactory() {
	}

	// Return an instance of
	public GraphRmi newInstance(GraphResource aGraphResource) {
		if(aGraphResource.Protocole.equals("rmi")) {
			return newGraphConnectorRmi(aGraphResource);
		}
		else {
			return null ;
		}
	}

	// Create an instance of GraphConnector for Rmi
	private GraphRmi newGraphConnectorRmi(GraphResource aGraphResource) {
		try {
			
			GraphRmi aGraphRmi = (GraphRmi)Naming.lookup("rmi://" + aGraphResource.getHost()+ "/" + aGraphResource.getGraphId()) ;
			return aGraphRmi ;
		}
		catch (RemoteException exp) {
				System.out.println("Pb de RemoteException dans newGraphConnectorRmi : " +	exp);
				return null ;
		}
		catch (NotBoundException exp) {
				System.out.println("Pb de NotBoundException dans newGraphConnectorRmi : "	+ exp);
				return null ;
		}
		catch (MalformedURLException exp) {
				System.out.println("Pb de MalformedURLException dans newGraphConnectorRmi : " + exp);
				return null ;
		}
	}


}
