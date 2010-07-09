package org.graphstream.distributed.common;

public class EnumReg {
	
	public static String RmiDGraph = "RmiDgraph" ;
	public static String DGraphNetwork = "DGraphNetwork" ;
	public static String Registry = "Registry";
	//public static String GraphInDGraph = "GraphInDGraph";
	//public static String GraphVInDGraph = "GraphVInDGraph";
	
	public static String DGraph = "DGraph" ;
	public static String Graph = "Graph" ;
	public static String GraphV = "GraphV" ;
	
	private EnumReg() {
	}
	
	public static String Graph(String id) {
		return (id+"-"+Graph);
	}
	
	public static String GraphV(String id) {
		return (id+"-"+GraphV);
	}
	
	public static String DGraph(String id) {
		return (id+"-"+DGraph);
	}

}
