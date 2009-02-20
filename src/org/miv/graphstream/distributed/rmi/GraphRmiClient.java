package org.miv.graphstream.distributed.rmi;

import org.miv.graphstream.distributed.GraphConnectorFactory;
import org.miv.graphstream.distributed.GraphResource;

public class GraphRmiClient {

	GraphRmi Client ;
	String Id ;
	GraphResource Resource;

	public GraphRmiClient(String uri) {
		connect(uri) ;
	}

	// Connection to a remote Graph
	public void connect(String uri) {
		GraphConnectorFactory connect = new GraphConnectorFactory() ;
		this.Resource = new GraphResource(uri) ;
		this.Id = this.Resource.getGraphId() ;
		this.Client = connect.newInstance(this.Resource);
	}

	public void setId(String id) {
		this.Id = id ;
	}

	public String getId() {
		return this.Id ;
	}

	public GraphRmi exec() {
		return this.Client ;
	}

	public GraphResource getGraphResource() {
		return this.Resource ;
	}
	
	

}
