package org.miv.graphstream.distributed;

import java.rmi.RemoteException;

import org.miv.graphstream.distributed.rmi.GraphRmi;

public class GraphConnection {

	// Fields

	private String id ;
	private GraphRmi graphConnector ;
	private GraphResource graphResourceIdentifier ;


	// Constructor

	public GraphConnection(GraphResource resource) {
		// Enregistrement des informations de connexion
		setId(resource.GraphId);
		this.graphResourceIdentifier = resource ;
		// Creation du connecteur vers la resource reseau
		GraphConnectorFactory aGraphConnectorFactory = new GraphConnectorFactory() ;
		this.graphConnector = aGraphConnectorFactory.newInstance(this.graphResourceIdentifier) ;
		// Creation du graph (Simple ...)
		try {
			this.graphConnector.createGraph(this.graphResourceIdentifier.getGraphId(), this.graphResourceIdentifier.getGraphClass());
		}
		catch(RemoteException e) {
			System.out.println("GraphConnection error (graphConnector.createGraph) :" + e.getMessage());
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
	public GraphRmi getGraphConnector() {
		return this.graphConnector ;
	}

	// getGraphResourceIdentifier
	public GraphResource getGraphResourceIdentifier() {
		return this.getGraphResourceIdentifier() ;
	}

}
