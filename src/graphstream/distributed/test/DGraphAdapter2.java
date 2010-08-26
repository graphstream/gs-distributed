package graphstream.distributed.test;

import java.util.Map;

import org.graphstream.graph.Graph;

public interface DGraphAdapter2 {
	
	
	// addEdge
	
	public void addEdge( String id, String node1, String node2 ) throws java.rmi.RemoteException ;

	public void addEdge( String id, String from, String to, boolean directed ) throws java.rmi.RemoteException ;

	public void addEdge ( String id, String from, String to, boolean directed, Map<String,Object> attributes ) throws java.rmi.RemoteException ;

	public void modifyEdge ( String id, Map<String,Object> attributes ) throws java.rmi.RemoteException ;
	
	// removeEdge
	
	public void removeEdge( String id ) throws java.rmi.RemoteException;

	public void removeEdge( String from, String to ) throws java.rmi.RemoteException;

	// removeNode
	public void removeNode( String id ) throws java.rmi.RemoteException ;
	
	//getGraph
	public Graph getGraph() throws java.rmi.RemoteException ;
	
	//getGraphV
	public Graph getGraphV() throws java.rmi.RemoteException ;
	
}
