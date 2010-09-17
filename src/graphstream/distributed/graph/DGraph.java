/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package graphstream.distributed.graph;

import graphstream.distributed.common.DGraphParser;
import graphstream.distributed.common.EnumEdge;
import graphstream.distributed.common.EnumNode;
import graphstream.distributed.common.EnumReg;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.GraphFactory;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;


public class DGraph implements DGraphAdapter {

	// Variables
	private String Name ;
	private Graph Graph ;
	private Graph GraphV ;
	private DGraphNetwork DGNetwork ;

	private static final long serialVersionUID = 0001 ;

	// Constructor
	public DGraph(String id, ConcurrentHashMap<String, Object> registry) {
		this.Name = id ;
		this.DGNetwork = (DGraphNetwork)registry.get(EnumReg.DGraphNetwork);
	}

	// Modifiers

	/**
	 * initialisation
	 */
	public void init(String graphClass) {		
		GraphFactory graphFactory = new GraphFactory() ;
		this.Graph = graphFactory.newInstance("", graphClass) ;
		this.Graph.setAutoCreate(true);
		this.Graph.setStrict(false);
		
		this.GraphV = new DefaultGraph("", false, true) ;
				
	}


	// modifyGraph
	/*public void modifyGraph(String graphId, Map<String,Object> attributes) throws java.rmi.RemoteException {
		if( attributes != null )
			graph.addAttributes( attributes );
	}*/

	/**
	 * addNode
	 */
	public void addNode(String id) throws java.rmi.RemoteException {
		this.Graph.addNode(id);
	}

	/**
	 * addNode
	 */
	public void addNode(String id, Map<String,Object> attributes ) throws java.rmi.RemoteException  {
		this.Graph.addNode(id);
		Node node = this.Graph.getNode(id);
		if( attributes != null )
			node.addAttributes(attributes);
	}

	/**
	 * modifyNode
	 */
	public void modifyNode ( String id, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		Node node = this.Graph.getNode(id);
		if( attributes != null )
			node.addAttributes(attributes);
	}

	/**
	 * addEdge
	 */
	public void addEdge( String id, String node1, String node2 ) throws java.rmi.RemoteException {
		Map<String, String> e = DGraphParser.edge(node1, node2);
		if(e.get(EnumEdge.GraphFrom).equals(e.get(EnumEdge.GraphTo))) { // intra edge
			this.Graph.addEdge(id, node1, node2);
		}
		else {
			if (e.get(EnumEdge.GraphTo).equals(this.Name)) { // virtual Edge (part2) - request from another DGraph
				this.GraphV.addEdge(id, node1, node2);
			}
			else { // virtual Edge (part1) - request from client
				this.GraphV.addEdge(id, node1, node2);
				this.DGNetwork.getDGraph(e.get(EnumEdge.GraphTo)).exec(e.get(EnumEdge.GraphTo),"addVirtualEdge", new Object[] {id, this.Name+"/"+node1, e.get(EnumEdge.NodeTo)});
			}
		}
	}
	
	/**
	 * addVirtualEdge
	 */
	public void addVirtualEdge(String id, String node1, String node2) {
		this.GraphV.addEdge(id, node1, node2);
	}

	/**
	 * addEdge
	 */
	public void addEdge( String id, String from, String to, boolean directed ) throws java.rmi.RemoteException{
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom) == e.get(EnumEdge.GraphTo)) {
			this.Graph.addEdge(id, from, to, directed);
		}
		else if (e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) { // virtual Edge (part2)
			this.GraphV.addEdge(id, from, to);
		}
		else {
			this.GraphV.addEdge(id, from, to);
			this.DGNetwork.getDGraph(e.get(EnumEdge.GraphTo)).exec(EnumEdge.GraphTo,"addEdge", new Object[] {id, from, to, directed});
		}
	}

	/**
	 * addEdge
	 */
	public void addEdge (String id, String from, String to, boolean directed, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		Edge edge ;
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom) == e.get(EnumEdge.GraphTo)) {
			edge = this.Graph.addEdge(id, from, to, directed);
		}
		else if (e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) { // virtual Edge (part2)
			edge = this.GraphV.addEdge(id, from, to);
		}
		else {
			edge = this.GraphV.addEdge(id, from, to);
			this.DGNetwork.getDGraph(EnumEdge.GraphTo).exec(EnumEdge.GraphTo,"addEdge", new Object[] {id, from, to, directed, attributes});
		}
		if( attributes != null )
			edge.addAttributes(attributes);
	}

	/**
	 * modifyEdge
	 */
	public void modifyEdge ( String id, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		if( attributes != null )
			this.Graph.getEdge(id).addAttributes(attributes);
	}

	
	/**
	 * getNodeCount
	 */
	public int getNodeCount() throws java.rmi.RemoteException {
		System.out.println("getNodeCount");
		return (this.Graph.getNodeCount());
	}

	
	/**
	 * getEdgeCount
	 */
	public int[] getEdgeCount() throws java.rmi.RemoteException {
		int[] res = new int[2] ;
		res[0]=this.Graph.getEdgeCount();
		res[1]=this.GraphV.getEdgeCount();
		return (res) ;
	}

	
	/**
	 * removeEdge
	 */
	public void removeEdge( String id ) throws java.rmi.RemoteException {
		Map<String, String> m = DGraphParser.edge(id);
		if(m.get(EnumNode.DGraphName).equals(this.Graph.getId()))
			this.Graph.removeEdge(id); // edge intra graph
		else {
			Node Node1 = this.GraphV.getEdge(id).getNode1();
			this.GraphV.removeEdge(id); // edge inter (part1)
			if(!DGraphParser.node(Node1.getId()).get(EnumNode.DGraphName).equals(this.Graph.getId())) {
				this.DGNetwork.getDGraph(DGraphParser.node(Node1.getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName,"removeEdge", new Object[] {id}); // edge inter (part2)
			}
		}
	}

	
	/**
	 * removeEdge
	 */
	public void removeEdge( String from, String to ) throws java.rmi.RemoteException {
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom).equals(e.get(EnumEdge.GraphTo)))
			this.Graph.removeEdge(from, to); // edge intra graph
		else {
			this.GraphV.removeEdge(from, to); // edge inter (part1)
			if(e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) {
				this.DGNetwork.getDGraph(e.get(EnumEdge.GraphTo)).exec(EnumEdge.GraphTo,"removeEdge", new Object[] {from, to}); // edge inter (part2)
			}
		}
	}

	
	/**
	 * removeNode
	 */
	public void removeNode( String id ) throws java.rmi.RemoteException {
		this.Graph.removeNode(id);
		// si le node appartient a un virtual edge
		if(this.GraphV.getNode(id)!=null) {
			Iterator<? extends Edge> it = this.GraphV.getNode(id).getEdgeIterator();
			this.GraphV.removeNode(id);
			while(it.hasNext()) {
				Edge e = it.next();
				if(DGraphParser.edge(e.getNode1().getId()).get(EnumNode.DGraphName).equals(this.Graph.getId())) {
					this.DGNetwork.getDGraph(DGraphParser.edge(e.getNode0().getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName, "removeNodeOnGraphVirtual", new Object[] {id});
				}
				else {
					this.DGNetwork.getDGraph(DGraphParser.edge(e.getNode1().getId()).get(EnumNode.DGraphName)).exec(EnumNode.DGraphName, "removeNodeOnGraphVirtual", new Object[] {id});
				}
			}
		}
	}


	/**
	 * removeNodeOnGraphVirtual
	 */
	public void removeNodeOnGraphVirtual( String id) throws java.rmi.RemoteException {
		this.GraphV.removeNode(id);
	}

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

}
