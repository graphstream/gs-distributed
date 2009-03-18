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

package org.miv.graphstream.distributed.rmi;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;

import org.miv.graphstream.algorithm.Algorithms;
import org.miv.graphstream.distributed.io.GraphReaderListenerHelperDistributedLocal;
import org.miv.graphstream.distributed.req.GraphReqContainer;
import org.miv.graphstream.distributed.req.GraphReqRunner;
import org.miv.graphstream.distributed.utile.GraphEdgeInfo;
import org.miv.graphstream.distributed.utile.GraphParseTag;
import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.GraphFactory;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.graphstream.io.GraphParseException;
import org.miv.graphstream.io.GraphReader;
import org.miv.graphstream.io.GraphReaderFactory;
import org.miv.graphstream.io.GraphReaderListener;
import org.miv.graphstream.io.GraphReaderListenerExtended;
import org.miv.graphstream.io.GraphReaderListenerHelper;

public class GraphRmiServer extends UnicastRemoteObject implements GraphRmi {

	// Variables

	private Graph graph ;
	private Graph vGraph ;
	private GraphReqRunner run ;
	private GraphRegistry Registry ;

	private GraphEdgeInfo e ;
	private GraphParseTag parser ;

	private GraphReader reader ;
	private GraphReaderListener listener ;

	private boolean lastStep ;
	private boolean lastEvent;

	private static final long serialVersionUID = 0001;


	// Constructor

	public GraphRmiServer() throws java.rmi.RemoteException {
		init();
	}

	public void init() throws java.rmi.RemoteException  {
		Registry = new GraphRegistry();
		this.vGraph = new DefaultGraph("", false, true);
		e = new GraphEdgeInfo();
		parser = new GraphParseTag();
	}

	// Modifiers

	public void register(String uri) throws java.rmi.RemoteException {
		Registry.addClient(new GraphRmiClient(uri));
		this.lastStep=false ;
		this.lastEvent=false ;
	}

	public void unregister(String uri) throws java.rmi.RemoteException {
		Registry.delClient(uri);
	}

	// createGraph
	public void createGraph(String id, String graphClass) throws java.rmi.RemoteException {
		GraphFactory graphFactory = new GraphFactory() ;
		this.graph = graphFactory.newInstance(id, graphClass) ;
		this.graph.setAutoCreate(true);
		this.graph.setStrictChecking(false);
		//this.vGraph.setId(id);
		this.run = new GraphReqRunner(this.graph);
	}

	public void newDgsReader(String fileName) {
		try {
			GraphReader graphReader = GraphReaderFactory.readerFor(fileName) ;
			GraphReaderListenerExtended alistener = new GraphReaderListenerHelper(this.graph);
			graphReader.addGraphReaderListener(alistener) ;
			Algorithms algo = new Algorithms();
			algo.setGraph(this.graph);
			graphReader.begin(fileName) ;
		}
		catch(IOException e) {
			System.out.println("IOException getDgsReader : " + e.getMessage());
		}
		catch(GraphParseException e) {
			System.out.println("GraphParseException " + e.getMessage());
		}
	}

	// modifyGraph
	/*public void modifyGraph(String graphId, Map<String,Object> attributes) throws java.rmi.RemoteException {
		if( attributes != null )
			graph.addAttributes( attributes );
	}*/

	// addNode
	public void addNode(String id) throws java.rmi.RemoteException {
		this.graph.addNode(id);
	}

	public void addNode(String id, Map<String,Object> attributes ) throws java.rmi.RemoteException  {
		this.graph.addNode(id);
		Node node = this.graph.getNode(id);
		if( attributes != null )
			node.addAttributes(attributes);
	}

	public void modifyNode ( String id, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		Node node = this.graph.getNode(id);
		if( attributes != null )
			node.addAttributes(attributes);
	}

	// addEdge
	public void addEdge( String id, String node1, String node2 ) throws java.rmi.RemoteException {
		e.setEdgeInfo(node1, node2) ;
		if(e.isIntraEdge()) { // intra edge
			this.graph.addEdge(id, node1, node2);
		}
		else {
			/*if(this.vGraph.getNode(node1) == null) {
				//System.out.println("addNode " + node1 + " on vGraph " + this.graph.getId());
				this.vGraph.addNode(node1);
			}
			if(this.vGraph.getNode(node2) == null) {
				//System.out.println("addNode " + node2 + " on vGraph " + this.graph.getId());
				this.vGraph.addNode(node2);
			}*/
			if (e.getGraphTag2().equals(this.graph.getId())) { // virtual Edge (part2) - requete vient d'un server
				//System.out.println("addEdge " +id + " --> "+ node1 + "-" + node2 + " on vGraph " + this.graph.getId());
				this.vGraph.addEdge(id, node1, node2);
			}
			else { // virtual edge (part 1 + part2) - requete vient d'un Registry
				//System.out.println("addEdge " +id + " --> "+ node1 + "-" + node2 + " on vGraph " + this.graph.getId());
				this.vGraph.addEdge(id, node1, node2);
				Registry.getClient(e.getGraphTag2()).exec().addEdge(id, node1, node2);
			}
		}
	}

	public void addEdge( String id, String from, String to, boolean directed ) throws java.rmi.RemoteException{
		GraphEdgeInfo e = new GraphEdgeInfo(from, to) ;
		if(e.isIntraEdge()) {
			this.graph.addEdge(id, from, to, directed);
		}
		else if (e.getGraphTag2().equals(this.graph.getId())) { // virtual Edge (part2)
			this.vGraph.addEdge(id, from, to);
		}
		else {
			this.vGraph.addEdge(id, from, to);
			Registry.getClient(e.getGraphTag2()).exec().addEdge(id, from, to, directed);
		}
	}

	public void addEdge (String id, String from, String to, boolean directed, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		e.setEdgeInfo(from, to) ;
		Edge edge ;
		if(e.isIntraEdge()) {
			edge = this.graph.addEdge(id, from, to, directed);
		}
		else if (e.getGraphTag2().equals(this.graph.getId())) { // virtual Edge (part2)
			edge = this.vGraph.addEdge(id, from, to);
		}
		else {
			edge = this.vGraph.addEdge(id, from, to);
			Registry.getClient(e.getGraphTag2()).exec().addEdge(id, from, to, directed, attributes);
		}
		if( attributes != null )
			edge.addAttributes(attributes);
	}

	// modifyEdge
	public void modifyEdge ( String id, Map<String,Object> attributes ) throws java.rmi.RemoteException {
		if( attributes != null )
			this.graph.getEdge(id).addAttributes(attributes);
	}

	// getNodeCount
	public int getNodeCount() throws java.rmi.RemoteException {
		return (this.graph.getNodeCount());
	}

	// getEdgeCount
	public int[] getEdgeCount() throws java.rmi.RemoteException {
		int[] res = new int[2] ;
		res[0]=this.graph.getEdgeCount();
		res[1]=this.vGraph.getEdgeCount();
		return (res) ;
	}

	// removeEdge
	public void removeEdge( String id ) throws java.rmi.RemoteException {
		this.parser.parse(id);
		if(this.parser.getGraphId().equals(this.graph.getId()))
			this.graph.removeEdge(id); // edge intra graph
		else {
			Node Node1 = this.vGraph.getEdge(id).getNode1();
			this.vGraph.removeEdge(id); // edge inter (part1)
			if(!this.parser.parse(Node1.getId()).getGraphId().equals(this.graph.getId())) {
				Registry.getClient(this.parser.parse(Node1.getId()).getGraphId()).exec().removeEdge(id); // edge inter (part2)
			}
		}
	}

	public void removeEdge( String from, String to ) throws java.rmi.RemoteException {
		e.setEdgeInfo(from, to) ;
		parser.parse(from);
		if(e.isIntraEdge())
			this.graph.removeEdge(from, to); // edge intra graph
		else {
			this.vGraph.removeEdge(from, to); // edge inter (part1)
			if(parser.parse(to).getGraphId().equals(this.graph.getId())) {
				Registry.getClient(parser.parse(to).getGraphId()).exec().removeEdge(from, to); // edge inter (part2)
			}
		}
	}

	public void removeNode( String id ) throws java.rmi.RemoteException {
		this.graph.removeNode(id);
		// si le node appartient à un virtual edge
		if(this.vGraph.getNode(id)!=null) {
			Iterator<? extends Edge> it = this.vGraph.getNode(id).getEdgeIterator();
			this.vGraph.removeNode(id);
			while(it.hasNext()) {
				Edge e = it.next();
				if(this.parser.parse(e.getNode1().getId()).getGraphId().equals(this.graph.getId())) {
					Registry.getClient(parser.parse(e.getNode0().getId()).getGraphId()).exec().removeNodeOnVGraph(id);
				}
				else {
					Registry.getClient(parser.parse(e.getNode1().getId()).getGraphId()).exec().removeNodeOnVGraph(id);
				}
			}
		}
	}

	public void removeNodeOnVGraph( String id) throws java.rmi.RemoteException {
		this.vGraph.removeNode(id);
	}


	/**
	 * Fonctions complementaires
	 */

	// nextDgsEvent
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

	public void nextDgsStep() throws java.rmi.RemoteException {
		try {
			if (reader.nextStep()) {
					System.out.println("nextDgsStep");
			}
			else {
				reader.end();
			}
			System.out.println("Fin du pas");
		}
			catch(IOException e) {
				System.out.println("Error : nextDgsStep --> " + e.getMessage());
			}
			catch(GraphParseException e) {
				System.out.println("Error : nextDgsStep " + e.getMessage());
			}
	}

	/**
	 * Affectation d'un reader et listener
	 * @param fileName
	 * @throws java.rmi.RemoteException
	 */
	public void setGraphReader(String fileName) throws java.rmi.RemoteException {
		try {
			reader = GraphReaderFactory.readerFor(fileName) ;
			listener = new GraphReaderListenerHelperDistributedLocal(this.graph, this.vGraph, this.Registry) ;
			reader.addGraphReaderListener(listener) ;
			reader.begin(fileName) ;
		}
		catch(IOException e) {
			System.out.println("Error : setGraphReader --> " + e.getMessage());
		}
		catch(GraphParseException e) {
			System.out.println("Error : setGraphReader " + e.getMessage());
		}
	}

	/**
	 * Execute la lecture de n steps d'un dgs
	 * @param pas
	 * @throws java.rmi.RemoteException
	 */
	public boolean nextDgsVirtualStep(Integer pas) throws java.rmi.RemoteException {
		int index = 0 ;
		try {
			for(int i=0 ; i<pas ; i++) {
				while(!this.lastStep) {
					if (reader.nextStep()) {
						System.out.println("Step " + index);
						index++;
					}
					else {
						reader.end();
						this.lastStep=true; ;
					}
				}
			}
			return lastStep ;
		}
		catch(IOException e) {
			System.out.println("Error : nextDgsVirtualStep --> " + e.getMessage());
			return true ;
		}
		catch(GraphParseException e) {
			System.out.println("Error : nextDgsVirtualStep " + e.getMessage());
			return true ;
		}
	}

	/**
	 * Execute la lecture de n events d'un dgs
	 * @param pas
	 * @throws java.rmi.RemoteException
	 */
	public boolean nextDgsVirtualEvents(Integer pas) throws java.rmi.RemoteException {
		try {
			for(int i=0 ; i<pas ; i++) {
				while(!this.lastEvent) {
					if (reader.nextEvents()) {
					}
					else {
						reader.end();
						this.lastEvent=true; ;
					}
				}
			}
			return this.lastEvent ;
		}
		catch(IOException e) {
			System.out.println("Error : nextDgsVirtualEvents --> " + e.getMessage());
			return true ;
		}
		catch(GraphParseException e) {
			System.out.println("Error : nextDgsVirtualEvents " + e.getMessage());
			return true ;
		}
	}

	// loadData
	public void loadData(String fileName) {
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
	}

	public GraphRmi getServerInst(String id) throws java.rmi.RemoteException {
		System.out.println("exec : " + this.Registry.getClient(id).exec());
		return this.Registry.getClient(id).exec();
	}


	/**
	 * Fonctions dynamiques (invocation d'algo par exemple)
	 */

	public void newAlgorithm(String className, String instanceName) {
		run.newAlgorithm(className, instanceName) ;
	}

	public Object executeReq(Object req) {
		return run.executeRequest((GraphReqContainer)req);
	}

	public void test() throws java.rmi.RemoteException {
		System.out.println("TEST TEST");
	}


	/*
	 * (non-Javadoc)
	 * @see org.miv.graphstream.distributed.rmi.GraphRmi#callBack()
	 */

	public void execute(Object req) throws java.rmi.RemoteException {

	}

	public Object callBack() throws java.rmi.RemoteException {
		return null ;
	}


}
