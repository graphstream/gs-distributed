package org.graphstream.distributed.graph;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.distributed.rmi.DistGraphServer;
import org.graphstream.distributed.rmi.DistGraphServerRmiHandler;
import org.miv.graphstream.distributed.commun.GraphResource;

public class DistGraphClient {

	// Variables

	HashMap<String, DistGraphServer> distGraphServerList ;


	// Constructor

	public DistGraphClient() {
		init();
	}

	/**
	 * initialization
	 */
	private void init() {
		this.distGraphServerList = new HashMap<String, DistGraphServer>() ;
	}


	/**
	 * addDistGraph (+notification)
	 */
	public void addDistGraph(String uri, boolean notify) {
		addLocal(uri);
		if(notify) {
			addGraphNotify(uri);
		}
	}

	/**
	 * delDistGraph (+notification)
	 */
	public void delDistGraph(String graphId, boolean notify) {
		if(notify) {
			delGraphNotify(graphId);
		}
		delLocal(graphId);
	}

	/**
	 * connect a client to a server
	 */
	public void connectDistGraph() {

	}

	/**
	 * disconnect a client to a server
	 */
	public void disconnect() {

	}

	/**
	 *
	 * @param uri
	 */
	public void addLocal(String uri) {
		try {
			GraphResource resource = new GraphResource(uri);
			DistGraphServerRmiHandler connect = new DistGraphServerRmiHandler(resource) ;
			this.distGraphServerList.put(resource.getGraphId(), connect.getDistGraphServer());
			this.distGraphServerList.get(resource.getGraphId()).newDistGraph(resource.getGraphClass());
		}
		catch(RemoteException e) {
			System.out.println("addRemoteGraph error : " + e.getMessage());
		}
	}

	/**
	 *
	 * @param graphId
	 */
	public void delLocal(String graphId) {
		this.distGraphServerList.remove(graphId);
	}


	/**
	 *
	 * @param uri
	 */
	public void addGraphNotify(String uri) {
		try {
			Iterator<String> it = this.distGraphServerList.keySet().iterator() ;
			GraphResource r = new GraphResource(uri);
			while(it.hasNext()) {
				String id = it.next();
				if(r.getGraphId() != id) {
					System.out.println("id : " + id + " - URI : " + r.getUri());
					this.distGraphServerList.get(id).notifyDistGraphCreation(uri);
				}
			}
		}
		catch(RemoteException e) {
			System.out.println("" + e.getMessage());
		}
	}

	/**
	 *
	 * @param graphId
	 */
	private void delGraphNotify(String graphId) {
		try {
			Iterator<String> it = this.distGraphServerList.keySet().iterator() ;
			while(it.hasNext()) {
				String id = it.next();
				if(graphId != id) {
					this.distGraphServerList.get(id).notifyDistGraphDeletion(graphId);
				}
			}
		}
		catch(RemoteException e) {
			System.out.println("notifyDelGraph error : " + e.getMessage());
		}
	}

	/**
	 *
	 * @param graphId
	 * @return
	 */
	public DistGraphServer getDistGraphServer(String graphId) {
		return this.distGraphServerList.get(graphId) ;
	}

	/**
	 *
	 * @return
	 */
	public Iterator<String> getIterator() {
		return this.distGraphServerList.keySet().iterator();
	}


}
