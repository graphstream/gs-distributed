package org.graphstream.distributed.common;

import java.util.HashMap;
import java.util.Map;

public class DGraphParser {
	
	private static HashMap<String, Object> data = new HashMap<String, Object>();
	private static String sep = "/" ;
	
	//constructor
	private DGraphParser() {
	}
	
	//public method
	
	public static Map<String, Object> uri(String uri) {
		return uri_analyzer(uri) ;
	}
	
	public static Map<String, Object> edge(String from, String to) {
		return edge_analyzer(from, to) ;
	}
	
	public static Map<String, Object> node(String input) {
		return node_analyzer(input) ;
	}
	
	
	//private method
	
	/**
	 * uri_analyzer
	 */
	private static Map<String, Object> uri_analyzer(String uri) {
		data.clear();
		data.put("protocole", uri.split(":")[0]) ;
		data.put("port","1099");
		if(uri.split(":").length > 3) { // avec port specifie
			data.put(EnumUri.Port,uri.split(":")[2].split("/")[0]);
			data.put(EnumUri.Host,uri.split(":")[1]);
		}
		else { // sans port specif
			data.put("host",uri.split(":")[1].split("/")[0]) ;
		}
		data.put(EnumUri.DGraphName, uri.split("/")[1]);
		//data.put(EnumUri.DGraphClass, uri.split("/")[1].split(":")[1]);
		return data ;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private static Map<String, Object> edge_analyzer(String from, String to) {
		data.clear();
		String[] tab = from.split(DGraphCst.sep);
		data.put(EnumEdge.DGraphNameFrom, tab[0]);
		data.put(EnumEdge.ElementIdFrom, tab[1]);
		tab = from.split(sep);
		data.put(EnumEdge.DGraphNameTo, tab[0]);
		data.put(EnumEdge.ElementIdTo, tab[1]);
		return data ;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private static Map<String, Object> node_analyzer(String input) {
		data.clear();
		String[] tab = input.split(DGraphCst.sep);
		data.put(EnumNode.DGraphName, tab[0]);
		data.put(EnumNode.ElementId, tab[1]);
		return data ;
	}

}
