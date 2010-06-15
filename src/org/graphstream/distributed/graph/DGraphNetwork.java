	package org.graphstream.distributed.graph;

import java.util.HashMap;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumUri;
import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;

public class DGraphNetwork {

	// Variables
	private HashMap<String, RMIDGraphAdapter> RmiHandler ;
	private HashMap<String, String> Uri ;
	private String DefaultDGraph ;
	
	// Constructor
	public DGraphNetwork() {
		this.RmiHandler = new HashMap<String, RMIDGraphAdapter>() ;
		this.Uri = new HashMap<String, String>() ;
	}
	
	
	// Methods (public)
	
	/*
	 * add link 
	 */
	public void add(String uri) {
		String name = DGraphParser.uri(uri).get(EnumUri.DGraphName) ;
		this.RmiHandler.put(name, RMIHelper.register(uri));
		this.Uri.put(name, uri);
	}
	
	/*
	 * remove link
	 */
	public void del(String name) {
		this.RmiHandler.remove(name);
		this.Uri.remove(name);
	}
	
	public void setDefaultDGraph(String value) {
		this.DefaultDGraph = value ;
	}
	
	
	/*
	 * get link instance
	 */
	public RMIDGraphAdapter getDGraph(String id) {
		if(this.RmiHandler.containsKey(id))
			return this.RmiHandler.get(id);
		else
			return this.RmiHandler.get(this.DefaultDGraph);
	}
	

}
