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

package org.miv.graphstream.distributed.graph;

import java.util.Iterator;
import java.util.Map;

import org.miv.graphstream.distributed.common.GraphEdgeInfo;
import org.miv.graphstream.distributed.common.GraphParseTag;
import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.GraphFactory;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;

public class DistGraphCoreImpl implements DistGraphCore {

	// Variables
	private Graph Graph ;
	private Graph GraphVirtual ;

	private DistGraphClient Client ;
	//private DistGraphObjects GraphObjects ;

	//private HashMap<String, Object> Registry ;

	// A encapsuler
	private GraphEdgeInfo E ;
	private GraphParseTag Parser ;

	private boolean LastStep ;
	private boolean LastEvent ;
	private static final long serialVersionUID = 0001 ;

	// Constructor
	public DistGraphCoreImpl(String graphClass) {
		init(graphClass);
	}

	// Modifiers

	/**
	 * initialisation
	 */
	public void init(String graphClass) {
		initCst();
		initGraphs(graphClass);
	}

	private void initCst() {
		this.E 		= new GraphEdgeInfo();
		this.Parser = new GraphParseTag();
	}

	private void initGraphs(String graphClass) {
		this.GraphVirtual 	= new DefaultGraph("", false, true);
		GraphFactory graphFactory = new GraphFactory() ;
		this.Graph = graphFactory.newInstance("", graphClass) ;
		this.Graph.setAutoCreate(true);
		this.Graph.setStrict(false);
	}

	/**
	 *
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public DistGraphClient getClient() throws java.rmi.RemoteException {
		return this.Client ;
	}


	/**
	 * notifyNewGraph
	 */
	public void notifyNewGraph(String uri) throws java.rmi.RemoteException {
		this.Client.addDistGraph(uri, false);
	}

	/**
	 * notifyDelGraph
	 */
	public void notifyDelGraph(String graphId) throws java.rmi.RemoteException {
		this.Client.delDistGraph(graphId, false);
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
		E.setEdgeInfo(node1, node2) ;
		if(E.isIntraEdge()) { // intra edge
			System.out.println("intra");
			this.Graph.addEdge(id, node1, node2);
		}
		else {
			System.out.println("requete vient d'un serveur part (1 requete)");
			if (E.getGraphTag2().equals(this.Graph.getId())) { // virtual Edge (part2) - requete vient d'un server
				//System.out.println("addEdge " +id + " --> "+ node1 + "-" + node2 + " on GraphVirtual " + this.graph.getId());
				this.GraphVirtual.addEdge(id, node1, node2);
			}
			else { // virtual edge (part 1 + part2) - requete vient d'un Registry
				System.out.println("requete vient d'un client (2 requetes)");
				System.out.println("addEdge " +id + " --> "+ node1 + "-" + node2 + " on GraphVirtual " + E.getGraphTag2());
				this.GraphVirtual.addEdge(id, node1, node2);
				this.getClient().getDistGraphServer(E.getGraphTag2()).exec("addEdge", new String[] {"id", "node1", "node2"});
			}
		}
	}

	/**
	 * addEdge
	 */
	public void addEdge( String id, String from, String to, boolean directed ) throws java.rmi.RemoteException{
		GraphEdgeInfo e = new GraphEdgeInfo(from, to) ;
		if(e.isIntraEdge()) {
			this.Graph.addEdge(id, from, to, directed);
		}
		else if (e.getGraphTag2().equals(this.Graph.getId())) { // virtual Edge (part2)
			this.GraphVirtual.addEdge(id, from, to);
		}
		else {
			this.GraphVirtual.addEdge(id, from, to);
			this.getClient().getDistGraphServer(e.getGraphTag2()).exec("addEdge", new Object[] {id, from, to, directed});
		}
	}

	/**
	 * addEdge
	 */
	public void addEdge (String id, String from, String to, boolean directed, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		E.setEdgeInfo(from, to) ;
		Edge edge ;
		if(E.isIntraEdge()) {
			edge = this.Graph.addEdge(id, from, to, directed);
		}
		else if (E.getGraphTag2().equals(this.Graph.getId())) { // virtual Edge (part2)
			edge = this.GraphVirtual.addEdge(id, from, to);
		}
		else {
			edge = this.GraphVirtual.addEdge(id, from, to);
			this.getClient().getDistGraphServer(E.getGraphTag2()).exec("addEdge", new Object[] {id, from, to, directed, attributes});
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
		res[1]=this.GraphVirtual.getEdgeCount();
		return (res) ;
	}

	/**
	 * removeEdge
	 */
	public void removeEdge( String id ) throws java.rmi.RemoteException {
		this.Parser.parse(id);
		if(this.Parser.getGraphId().equals(this.Graph.getId()))
			this.Graph.removeEdge(id); // edge intra graph
		else {
			Node Node1 = this.GraphVirtual.getEdge(id).getNode1();
			this.GraphVirtual.removeEdge(id); // edge inter (part1)
			if(!this.Parser.parse(Node1.getId()).getGraphId().equals(this.Graph.getId())) {
				this.getClient().getDistGraphServer(this.Parser.parse(Node1.getId()).getGraphId()).exec("removeEdge", new Object[] {id}); // edge inter (part2)
			}
		}
	}

	/**
	 * removeEdge
	 */
	public void removeEdge( String from, String to ) throws java.rmi.RemoteException {
		E.setEdgeInfo(from, to) ;
		Parser.parse(from);
		if(E.isIntraEdge())
			this.Graph.removeEdge(from, to); // edge intra graph
		else {
			this.GraphVirtual.removeEdge(from, to); // edge inter (part1)
			if(Parser.parse(to).getGraphId().equals(this.Graph.getId())) {
				this.getClient().getDistGraphServer(Parser.parse(to).getGraphId()).exec("removeEdge", new Object[] {from, to}); // edge inter (part2)
			}
		}
	}

	/**
	 * removeNode
	 */
	public void removeNode( String id ) throws java.rmi.RemoteException {
		this.Graph.removeNode(id);
		// si le node appartient a un virtual edge
		if(this.GraphVirtual.getNode(id)!=null) {
			Iterator<? extends Edge> it = this.GraphVirtual.getNode(id).getEdgeIterator();
			this.GraphVirtual.removeNode(id);
			while(it.hasNext()) {
				Edge e = it.next();
				if(this.Parser.parse(e.getNode1().getId()).getGraphId().equals(this.Graph.getId())) {
					this.getClient().getDistGraphServer(Parser.parse(e.getNode0().getId()).getGraphId()).exec("removeNodeOnGraphVirtual", new Object[] {id});
				}
				else {
					this.getClient().getDistGraphServer(Parser.parse(e.getNode1().getId()).getGraphId()).exec("removeNodeOnGraphVirtual", new Object[] {id});
				}
			}
		}
	}


	/**
	 * removeNodeOnGraphVirtual
	 */
	public void removeNodeOnGraphVirtual( String id) throws java.rmi.RemoteException {
		this.GraphVirtual.removeNode(id);
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
