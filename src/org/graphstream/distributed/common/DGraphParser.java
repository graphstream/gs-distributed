package org.graphstream.distributed.common;

import java.util.HashMap;
import java.util.Map;

public class DGraphParser {
	
	private static HashMap<String, String> data = new HashMap<String, String>();
	private static String sep = "/" ;
	
	//constructor
	private DGraphParser() {
	}
	
	//public method
	
	public static Map<String, String> uri(String uri) {
		return uri_analyzer(uri) ;
	}
	
	public static Map<String, String> edge(String from, String to) {
		return edge_analyzer(from, to) ;
	}
	
	public static Map<String, String> edge(String input) {
		return node_analyzer(input) ;
	}
	
	public static Map<String, String> node(String input) {
		return node_analyzer(input) ;
	}
	
	
	
	//private method
	
	/**
	 * uri_analyzer rmi://<host>:<port>/<id> ou rmi://<host>/<id>
	 */
	private static Map<String, String> uri_analyzer(String uri) {
		data.clear();
		data.put(EnumUri.Protocole, uri.split("://")[0]) ;
		
		if(uri.split(":").length > 2) { // avec port specifie
			data.put(EnumUri.Port,uri.split(":")[2].split("/")[0]);
			data.put(EnumUri.Host,uri.split("://")[1].split(":")[0]);
		}
		else { // sans port specif
			data.put(EnumUri.Host,uri.split("://")[1].split("/")[0]) ;
		}
		data.put(EnumUri.DGraphName, uri.split("/")[3]);
		return data ;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private static Map<String, String> edge_analyzer(String from, String to) {
		data.clear();
		String[] tab = from.split(DGraphCst.sep);
		if(tab.length > 1) {
			data.put(EnumEdge.GraphFrom, tab[0]);
			data.put(EnumEdge.NodeFrom, tab[1]);
		} else {
			data.put(EnumEdge.GraphFrom, "");
			data.put(EnumEdge.NodeFrom, tab[0]);
		}
		tab = to.split(sep);
		if(tab.length > 1) {
			data.put(EnumEdge.GraphTo, tab[0]);
			data.put(EnumEdge.NodeTo, tab[1]);
		} else {
			data.put(EnumEdge.GraphTo, "");
			data.put(EnumEdge.NodeTo, tab[0]);
		}
		return data ;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private static Map<String, String> node_analyzer(String input) {
		data.clear();
		String[] tab = input.split(DGraphCst.sep);
		data.put(EnumNode.DGraphName, tab[0]);
		data.put(EnumNode.ElementId, tab[1]);
		return data ;
	}

}
