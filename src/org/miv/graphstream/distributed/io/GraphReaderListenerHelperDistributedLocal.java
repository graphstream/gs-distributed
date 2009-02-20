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
package org.miv.graphstream.distributed.io;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import org.miv.graphstream.distributed.rmi.GraphRegistry;
import org.miv.graphstream.distributed.utile.GraphEdgeInfo;
import org.miv.graphstream.distributed.utile.GraphParseTag;
import org.miv.graphstream.graph.Edge;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.io.GraphParseException;
import org.miv.graphstream.io.GraphReader;
import org.miv.graphstream.io.GraphReaderListener;

/**
 * Default implementation for a graph reader listener. It takes as argument a
 * graph, and modifies this graph according to the events received. This is a
 * simple and quick way to build a graph from a graph reader.
 *
 * @author Yoann Pigné
 * @author Antoine Dutot
 * @since 20060101
 * @see GraphReader
 * @see GraphReaderListener
 */
public class GraphReaderListenerHelperDistributedLocal implements GraphReaderListener
{
	/**
	 * A reference to the graph it modifies.
	 */
	protected GraphRegistry registry;
	protected Graph graph ;
	protected Graph vGraph ;

	private GraphEdgeInfo e ;
	private GraphParseTag parser ;

	/**
	 * New default graph reader listener that modifies the given graph according
	 * to the received events.
	 * @param graph The graph to modify according to events.
	 */
	public GraphReaderListenerHelperDistributedLocal(Graph aGraph, Graph aVgraph, GraphRegistry aGraphClientRegistry)
	{
		this.graph = aGraph ;
		this.vGraph = aVgraph ;
		this.registry = aGraphClientRegistry ;
		e = new GraphEdgeInfo() ;
		parser = new GraphParseTag() ;
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#edgeAdded(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	public void edgeAdded(String id, String from, String to, boolean directed, Map<String, Object> attributes) throws GraphParseException
	{
		try {
		e.setEdgeInfo(from, to) ;
		Edge res ;
		if(e.isIntraEdge()) { // intra edge
			res = this.graph.addEdge(id, from, to);
		}
		else {
			if(this.vGraph.getNode(from) == null) this.vGraph.addNode(from);
			if(this.vGraph.getNode(to) == null) this.vGraph.addNode(to);
			if (e.getGraphTag2().equals(this.graph.getId())) { // virtual Edge (part2) - requete vient d'un server
				res = this.vGraph.addEdge(id, from, to);
			}
			else { // virtual edge (part 1 + part2) - requete vient d'un Registry
				res = this.vGraph.addEdge(id, from, to);
				this.registry.getClient(e.getGraphTag2()).exec().addEdge(id, from, to);
			}
		}
		if( attributes != null )
			res.addAttributes(attributes);
		}
		catch(RemoteException e) {
			System.out.println("RemoteException : " + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#edgeChanged(java.lang.String, java.util.Map)
	 */
	public void edgeChanged(String id, Map<String, Object> attributes) throws GraphParseException
	{
		Edge edge = graph.getEdge(id);

		if( attributes != null && edge != null )
			edge.addAttributes( attributes );
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#edgeRemoved(java.lang.String)
	 */
	public void edgeRemoved(String id) throws GraphParseException
	{
		this.parser.parse(id);
		if(this.parser.getGraphId().equals(this.graph.getId())) {
			this.graph.removeEdge(id); // edge intra graph
		}
		else {
			Node Node1 = this.vGraph.getEdge(id).getNode1();
			this.vGraph.removeEdge(id); // edge inter (part1)
			if(!this.parser.parse(Node1.getId()).getGraphId().equals(this.graph.getId())) {
				try {
					this.registry.getClient(this.parser.parse(Node1.getId()).getGraphId()).exec().removeEdge(id);
				}// edge inter (part2)
				catch(RemoteException e ) {
					System.out.println("RemoteException edgeRemoved " + e.getMessage()) ;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#graphChanged(java.util.Map)
	 */
	public void graphChanged(Map<String, Object> attributes) throws GraphParseException
	{
		if( attributes != null )
			graph.addAttributes( attributes );
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeAdded(java.lang.String, java.util.Map)
	 */
	public void nodeAdded(String id, Map<String, Object> attributes) throws GraphParseException
	{
		Node node = graph.addNode(id);

		if( attributes != null && node != null )
			node.addAttributes(attributes);
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeChanged(java.lang.String, java.util.Map)
	 */
	public void nodeChanged(String id, Map<String, Object> attributes) throws GraphParseException
	{
		Node n = graph.getNode( id );
		//System.out.println("nodeChanged " + id);
		if(n == null)
		{
			if(graph.isStrictCheckingEnabled())
			{
				throw new GraphParseException();
			}
			if(graph.isAutoCreationEnabled()) {
				n = graph.addNode(id);
			}
		}

		if( n != null )
		{
			if( attributes != null )
				n.addAttributes(attributes);
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeRemoved(java.lang.String)
	 */
	public void nodeRemoved(String id) throws GraphParseException
	{
		try {
			//graph.removeNode(id);
			this.graph.removeNode(id);
			// si le node appartient à un virtual edge

			if(this.vGraph.getNode(id)!=null) {
				Iterator<? extends Edge> it = this.vGraph.getNode(id).getEdgeIterator();
				this.vGraph.removeNode(id);
				while(it.hasNext()) {
					Edge e = it.next();
					if(this.parser.parse(e.getNode1().getId()).getGraphId().equals(this.graph.getId())) {
						registry.getClient(parser.parse(e.getNode0().getId()).getGraphId()).exec().removeNodeOnVGraph(id);
					}
					else {
						registry.getClient(parser.parse(e.getNode1().getId()).getGraphId()).exec().removeNodeOnVGraph(id);
					}
				}
			}
		}
		catch(RemoteException e) {
			System.out.println("Erreur nodeRemoved : " + this.getClass().getName() + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#stepBegins(double)
	 */
	public void stepBegins(double time) throws GraphParseException
	{
		//System.out.println("GraphReaderListener.stepBegins : "+time);
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#unknownEventDetected(java.lang.String)
	 */
	public void unknownEventDetected(String unknown) throws GraphParseException
	{
		System.out.println("GraphReaderListener.unknownEventDetected");
	}
}