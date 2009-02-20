package org.miv.graphstream.distributed.rmi;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;


public class GraphRegistry {


	HashMap<String, GraphRmiClient> clients ;

	// constructor

	public GraphRegistry() {
		clients = new HashMap<String, GraphRmiClient>() ;
	}

	// Modifier

	public void addClient(String uri) {
		try {
		GraphRmiClient client = new GraphRmiClient(uri);
		this.clients.put(client.getId(), client);
		this.getClient(client.getId()).exec().createGraph(client.getId(), client.getGraphResource().getGraphClass());
		}
		catch(RemoteException e) {
			System.out.println("RemoteException in addClient : " + e.getMessage());
		}
	}

	public void addClient(GraphRmiClient value) {
		if(this.clients.get(value.getId())==null) {
			this.clients.put(value.getId(), value);
		}
	}

	public void delClient(String id) {
		this.clients.remove(id);
	}

	public void broadcastClient(String graphId) {
		try {
			Iterator<String> it = this.clients.keySet().iterator() ;
			while(it.hasNext()) {
				String id = it.next();
				if(!id.equals(graphId)) {
					this.clients.get(graphId).exec().register(this.clients.get(id).getGraphResource().getUri());
				}
			}
		}
		catch(RemoteException e) {
			System.out.println("RemoteException : " + e.getMessage());
		}
	}

	// Accessor

	public GraphRmiClient getClient(String id) {
		return this.clients.get(id);
	}


	public int getNbClients() {
		return this.clients.size() ;
	}

	public Iterator<String> getKeySet() {
		return this.clients.keySet().iterator();
	}



}
