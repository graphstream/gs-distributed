package org.graphstream.distributed.common;

import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;

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
	
	public static Object functionCall(Graph g, String input) {
		return functionCall_analyzer(g, input) ;
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
		if(tab.length > 1) {
			data.put(EnumNode.DGraphName, tab[0]);
			data.put(EnumNode.ElementId, tab[1]);
		} else {
			data.put(EnumNode.ElementId, input);
		}
		
		return data ;
	}
	
	/**
	 * functionCall_analyzer (g1.f1('a').f2('b').f3('c').f4, arguments)
	 */
	private static Object functionCall_analyzer(Graph g, String input) {
		data.clear();
		String[] tab = input.split(".");
		Object obj = g ;
		for(int i = 1 ; i < tab.length-1 ; i++) {
			String[] tmp = functionSimple(tab[i]);
			obj = DynamicHelper.call2(obj, tmp[0],true, new Object[] { tmp[1]});
		}
		return obj ;
	}
	
	/**
	 * 
	 * @param input function('arg')
	 * @return
	 */
	public static String[] functionSimple(String input) {
		String[] res = new String[2];
		res[0] = input.split("\\(")[0];
		res[1] = input.split("\'")[1];
		return res ;
	}
	
	
	public static String[] functionAll(String input) {
		String[] res = input.split("\\.");
		return res ;
	}
	
	public static String[][] functionSpliter(String input) {
		String[] f = functionAll(input);
		String[][] res = new String[f.length][2];
		for(int i=0 ; i < f.length ; i++) {
			if(i==0 || i==(f.length-1)) {
				res[i][0] = f[i];
			} else if (i>0 && i<f.length) {
				res[i] = functionSimple(f[i]);
			}
		}
		return res ;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String functionLast(String input) {
		String[] res = input.split("\\.");
		return res[res.length-1] ;
	}
	
	
	

}
