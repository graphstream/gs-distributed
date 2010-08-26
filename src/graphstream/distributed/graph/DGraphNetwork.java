	package graphstream.distributed.graph;

import graphstream.distributed.common.DGraphParser;
import graphstream.distributed.common.EnumUri;
import graphstream.distributed.rmi.RMIDGraphAdapter;
import graphstream.distributed.rmi.RMIHelper;

import java.util.HashMap;

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
