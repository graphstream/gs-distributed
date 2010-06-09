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

package org.graphstream.distributed.graph;

import java.util.Iterator;
import java.util.Map;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumEdge;
import org.graphstream.distributed.common.EnumNode;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.GraphFactory;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;


public class DGraph implements DGraphAdapter {

	// Variables
	private Graph Graph ;
	private Graph GraphV ;
	private DGraphManager Manager ;

	// A encapsuler
	//private DGraphEdgeInfo E ;
	//private DGraphParseTag Parser ;

	private static final long serialVersionUID = 0001 ;

	// Constructor
	public DGraph() {
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
		
		this.Manager = new DGraphManager("messenger") ;
	}


	/**
	 * notifyNewGraph
	 */
	public void notifyNewGraph(String uri) throws java.rmi.RemoteException {
		//this.Manager.register(uri);
	}

	/**
	 * notifyDelGraph
	 */
	public void notifyDelGraph(String graphId) throws java.rmi.RemoteException {
		this.Manager.unregister(graphId);
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
		System.out.println("addNode");
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
			if (e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) { // virtual Edge (part2) - requete vient d'un server
				this.GraphV.addEdge(id, node1, node2);
			}
			else {
				this.GraphV.addEdge(id, node1, node2);
				this.Manager.getDGraph(e.get(EnumEdge.GraphTo)).exec(null, "g", "addVirtualEdge", new String[] {id, node1, node2});
			}
		}
	}
	
	/*
	 * addVirtualEdge
	 */
	public void addVirtualEdge(String id, String node1, String node2) {
		this.GraphV.addEdge(id, node1, node2);
	}

	/**
	 * addEdge
	 */
	public void addEdge( String id, String from, String to, boolean directed ) throws java.rmi.RemoteException{
		//DGraphEdgeInfo e = new DGraphEdgeInfo(from, to) ;
		Map<String, String> e = DGraphParser.edge(from, to);
		if(e.get(EnumEdge.GraphFrom) == e.get(EnumEdge.GraphTo)) {
			this.Graph.addEdge(id, from, to, directed);
		}
		else if (e.get(EnumEdge.GraphTo).equals(this.Graph.getId())) { // virtual Edge (part2)
			this.GraphV.addEdge(id, from, to);
		}
		else {
			this.GraphV.addEdge(id, from, to);
			this.Manager.getDGraph(e.get(EnumEdge.GraphTo)).exec(null, "g", "addEdge", new Object[] {id, from, to, directed});
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
			this.Manager.getDGraph(EnumEdge.GraphTo).exec(null, "g", "addEdge", new Object[] {id, from, to, directed, attributes});
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
				this.Manager.getDGraph(DGraphParser.node(Node1.getId()).get(EnumNode.DGraphName)).exec(null, "g", "removeEdge", new Object[] {id}); // edge inter (part2)
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
				this.Manager.getDGraph(e.get(EnumEdge.GraphTo)).exec(null, "g", "removeEdge", new Object[] {from, to}); // edge inter (part2)
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
					this.Manager.getDGraph(DGraphParser.edge(e.getNode0().getId()).get(EnumNode.DGraphName)).exec(null, "g", "removeNodeOnGraphVirtual", new Object[] {id});
				}
				else {
					this.Manager.getDGraph(DGraphParser.edge(e.getNode1().getId()).get(EnumNode.DGraphName)).exec(null, "g", "removeNodeOnGraphVirtual", new Object[] {id});
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


















	// Fonctions complementaires


	/**
	 * nextDgsEvent
	 */
	public void nextDgsEvent() throws java.rmi.RemoteException {
		/*try {
			graphReader.nextEvents();
		}
		catch(IOException e) {
			System.out.println("New DGS Events : " + e.getMessage());
		}
		catch(GraphParseException e) {
			System.out.println("New DGS Events : " + e.getMessage());
		}*/
	}


	/**
	 * nextDgsStep
	 */
	/*public void nextDgsStep() throws java.rmi.RemoteException {
		try {
			if (Reader.nextStep()) {
					System.out.println("nextDgsStep");
			}
			else {
				Reader.end();
			}
			System.out.println("Fin du pas");
		}
			catch(IOException e) {
				System.out.println("Error : nextDgsStep --> " + e.getMessage());
			}
			catch(GraphParseException e) {
				System.out.println("Error : nextDgsStep " + e.getMessage());
			}
	}*/

	/**
	 * Affectation d'un reader et listener
	 * @param fileName
	 * @throws java.rmi.RemoteException
	 */
	public void setGraphReader(String fileName) throws java.rmi.RemoteException {
		/*try {
			reader = GraphReaderFactory.readerFor(fileName) ;
			listener = new GraphReaderListenerHelperDistributedLocal(this.graph, this.graphVirtual, this.Registry) ;
			reader.addGraphReaderListener(listener) ;
			reader.begin(fileName) ;
		}
		catch(IOException e) {
			System.out.println("Error : setGraphReader --> " + e.getMessage());
		}
		catch(GraphParseException e) {
			System.out.println("Error : setGraphReader " + e.getMessage());
		}*/
	}

	/**
	 * Execute la lecture de n steps d'un dgs
	 * @param pas
	 * @throws java.rmi.RemoteException
	 */
	/*public boolean nextDgsVirtualStep(Integer pas) throws java.rmi.RemoteException {
		int index = 0 ;
		try {
			for(int i=0 ; i<pas ; i++) {
				while(!this.LastStep) {
					if (Reader.nextStep()) {
						System.out.println("Step " + index);
						index++;
					}
					else {
						Reader.end();
						this.LastStep=true; ;
					}
				}
			}
			return this.LastStep ;
		}
		catch(IOException e) {
			System.out.println("Error : nextDgsVirtualStep --> " + e.getMessage());
			return true ;
		}
		catch(GraphParseException e) {
			System.out.println("Error : nextDgsVirtualStep " + e.getMessage());
			return true ;
		}
	}*/

	/**
	 * Execute la lecture de n events d'un dgs
	 * @param pas
	 * @throws java.rmi.RemoteException
	 */
	/*public boolean nextDgsVirtualEvents(Integer pas) throws java.rmi.RemoteException {
		try {
			for(int i=0 ; i<pas ; i++) {
				while(!this.LastEvent) {
					if (this.Reader.nextEvents()) {
					}
					else {
						this.Reader.end();
						this.LastEvent=true; ;
					}
				}
			}
			return this.LastEvent ;
		}
		catch(IOException e) {
			System.out.println("Error : nextDgsVirtualEvents --> " + e.getMessage());
			return true ;
		}
		catch(GraphParseException e) {
			System.out.println("Error : nextDgsVirtualEvents " + e.getMessage());
			return true ;
		}
	}*/

	// loadData
	/*public void loadData(String fileName) {
		try {
			GraphReader graphReader = GraphReaderFactory.readerFor(fileName) ;
			GraphReaderListener alistener = new GraphReaderListenerHelperDistributedLocal(this.graph, this.vGraph, this.Registry) ;
			graphReader.addGraphReaderListener(alistener) ;
			graphReader.begin(fileName) ;
			while(graphReader.nextEvents()) {
			}
			graphReader.end();
		}
		catch(IOException e) {
			System.out.println("IOException getDgsReader : " + e.getMessage());
		}
		catch(GraphParseException e) {
			System.out.println("GraphParseException in loadData : " + e.getMessage());
			System.out.println("GraphParseException in loadData : " + fileName);
		}
	}*/


	/**
	 * Fonctions dynamiques (invocation d'algo par exemple)
	 */

	/*public Object exec(String method, Object[] params) {
		return this.Run.run(method, params);
	}

	public Object exec(String objectInstanceName, String methode, Object[] params) throws java.rmi.RemoteException {
		return null ;
	}

	public Object newRemoteObject(String objectInstanceName, String className, Object[] params) throws java.rmi.RemoteException {
		Registry.put(objectInstanceName, (new GraphRemoteObjectFactory()).newInstance(className));
		//((Algorithm)(Registry.get(objectInstanceName))).init((Graph)Registry.get(this.graphId));
		return null ;
	}*/


	/**
	 * test
	 */
	public void test() throws java.rmi.RemoteException {
		System.out.println("TEST TEST");
	}


	/**
	 * callBack (� completer)
	 */
	/*public Object callBack() throws java.rmi.RemoteException {
		return null ;
	}*/

	/**
	 * � supprimer
	 */

	/*public void newAlgorithm(String className, String instanceName) {
		Run.newAlgorithm(className, instanceName) ;
	}

	public Object executeReq(Object req) {
		return this.Run.executeRequest((GraphReqContainer)req);
	}*/


}
