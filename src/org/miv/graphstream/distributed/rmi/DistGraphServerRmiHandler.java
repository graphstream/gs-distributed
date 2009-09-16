package org.miv.graphstream.distributed.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.miv.graphstream.distributed.utile.GraphResource;

public class DistGraphServerRmiHandler {

	// Fields

	private String id ;
	private DistGraphServer distGraphServer ;
	private GraphResource graphResourceIdentifier ;


	// Constructor

	public DistGraphServerRmiHandler(GraphResource resource) {
		// Enregistrement des informations de connexion
		setId(resource.getGraphId());
		this.graphResourceIdentifier = resource ;
		// Creation du connecteur vers la resource reseau
		//DistGraphRmiHandlerFactory aGraphConnectorFactory = new DistGraphRmiHandlerFactory() ;
		//this.distGraph = aGraphConnectorFactory.newInstance(this.graphResourceIdentifier) ;
		this.distGraphServer = newDistGraphRmiHandler(resource);
	}

	private DistGraphServer newDistGraphRmiHandler(GraphResource aGraphResource) {
		try {
			System.out.println("newGraphConnectorRmi : " + aGraphResource.getHost() + " " + aGraphResource.getGraphId());
			DistGraphServer aDistGraphServer = (DistGraphServer)Naming.lookup("rmi://" + aGraphResource.getHost()+ "/" + aGraphResource.getGraphId()) ;
			return aDistGraphServer ;
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


	// Modifiers

	public void setId(String anId) {
		this.id = anId ;
	}


	// Accessors

	// getId
	public String getId() {
		return this.id ;
	}

	// getGraphConnector
	public DistGraphServer getDistGraphServer() {
		return this.distGraphServer ;
	}

	// getGraphResourceIdentifier
	public GraphResource getGraphResourceIdentifier() {
		return this.getGraphResourceIdentifier() ;
	}

}
