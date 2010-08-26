package graphstream.distributed.test;

import graphstream.distributed.common.DGraphParser;
import graphstream.distributed.common.EnumEdge;
import graphstream.distributed.common.EnumNode;
import graphstream.distributed.common.EnumReg;
import graphstream.distributed.graph.DGraphNetwork;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.GraphFactory;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

public class DGraph2 implements DGraphAdapter2 {
	
	////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////

	private String Name ;
	private Graph Graph ;
	private Graph GraphV ;
	private DGraphNetwork DGNetwork ;

	private static final long serialVersionUID = 0001 ;

	
	////////////////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////////////////

	
	public DGraph2(String id, ConcurrentHashMap<String, Object> registry) {
		this.Name = id ;
		this.DGNetwork = (DGraphNetwork)registry.get(EnumReg.DGraphNetwork);
	}

	public void init(String graphClass) {		
		GraphFactory graphFactory = new GraphFactory() ;
		this.Graph = graphFactory.newInstance("", graphClass) ;
		this.Graph.setAutoCreate(true);
		this.Graph.setStrict(false);	
		this.GraphV = new DefaultGraph("", false, true) ;
	}
	
	////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////
	
	/**
	 * getGraph
	 */
	public Graph getGraph() throws java.rmi.RemoteException {
		return this.Graph ;
	}
	
	/**
	 * getGraphV
	 */
	public Graph getGraphV() throws java.rmi.RemoteException {
		return this.GraphV ;
	}

	/**
	 * addEdge
	 */
	public void addEdge(String id, String node1, String node2)
			throws RemoteException {
		// TODO Auto-generated method stub
		addEdge(id, node1, node2, false, null);
	}

	/**
	 * addEdge
	 */
	public void addEdge(String id, String from, String to, boolean directed)
			throws RemoteException {
		// TODO Auto-generated method stub
		addEdge(id, from, to, directed, null);
	}

	/**
	 * addEdge
	 */
	public void addEdge(String id, String from, String to, boolean directed,
		Map<String, Object> attributes) throws RemoteException {
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom).equals(e.get(EnumEdge.GraphTo))) {
			this.Graph.addEdge(id, from, to, directed);
		}
		else {
			if (e.get(EnumEdge.GraphTo).equals(this.Name)) {
				this.GraphV.addEdge(id, from, to, directed);
			} else {
				this.GraphV.addEdge(id, from, to);
				this.DGNetwork.getDGraph(EnumEdge.GraphTo).exec(e.get(EnumEdge.GraphTo)+".addVirtualEdge", 
																new Object[] {id, this.Name+"/"+from, 
																e.get(EnumEdge.NodeTo), 
																directed, 
																null});
			}
		}
		if( attributes != null )
			this.Graph.getEdge(id).addAttributes(attributes);	
	}
		
	/**
	 * modifyEdge
	 */
	public void modifyEdge(String id, Map<String, Object> attributes)
			throws RemoteException {	
		if( attributes != null ) {
			if(this.GraphV.getEdge(id) != null) 
				this.Graph.getEdge(id).addAttributes(attributes);
			else
				this.DGNetwork.getDGraph(id);
		}
	}

	/**
	 * removeEdge
	 */
	public void removeEdge(String id) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> m = DGraphParser.edge(id);
		if(m.get(EnumNode.DGraphName).equals(this.Graph.getId()))
			this.Graph.removeEdge(id); // edge intra graph
		else {
			Node Node1 = this.GraphV.getEdge(id).getNode1();
			this.GraphV.removeEdge(id); // edge inter (part1)
			if(!DGraphParser.node(Node1.getId()).get(EnumNode.DGraphName).equals(this.Graph.getId())) {
				this.DGNetwork.getDGraph(DGraphParser.node(Node1.getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName+".removeEdge", new Object[] {id}); // edge inter (part2)
			}
		}
	}

	/**
	 * removeEdge
	 */
	public void removeEdge(String from, String to) throws RemoteException {
		// TODO Auto-generated method stub
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom).equals(e.get(EnumEdge.GraphTo)))
			this.Graph.removeEdge(from, to); // edge intra graph
		else {
			this.GraphV.removeEdge(from, to); // edge inter (part1)
			if(e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) {
				this.DGNetwork.getDGraph(e.get(EnumEdge.GraphTo)).exec(EnumEdge.GraphTo+".removeEdge", new Object[] {from, to}); // edge inter (part2)
			}
		}
	}

	/**
	 * removeNode
	 */
	public void removeNode(String id) throws RemoteException {
		// TODO Auto-generated method stub
		this.Graph.removeNode(id);
		// si le node appartient a un virtual edge
		if(this.GraphV.getNode(id)!=null) {
			Iterator<? extends Edge> it = this.GraphV.getNode(id).getEdgeIterator();
			this.GraphV.removeNode(id);
			while(it.hasNext()) {
				Edge e = it.next();
				if(DGraphParser.edge(e.getNode1().getId()).get(EnumNode.DGraphName).equals(this.Graph.getId())) {
					this.DGNetwork.getDGraph(DGraphParser.edge(e.getNode0().getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName+".removeNodeOnGraphVirtual", new Object[] {id});
				}
				else {
					this.DGNetwork.getDGraph(DGraphParser.edge(e.getNode1().getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName+".removeNodeOnGraphVirtual", new Object[] {id});
				}
			}
		}
	}
	
	
	////////////////////////////////////////////////////////////
	//Other methods
	////////////////////////////////////////////////////////////

	/**
	 * addVirtualEdge
	 */
	public void addVirtualEdge(String id, String node1, String node2) {
		this.GraphV.addEdge(id, node1, node2);
	}
	
}
