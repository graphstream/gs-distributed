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
package org.graphstream.distributed.stream.old;

import java.rmi.RemoteException;
import java.util.Map;

import org.graphstream.distributed.common.DGraphEdgeInfo;
import org.graphstream.distributed.common.DGraphParseTag;
import org.graphstream.distributed.graph.DGraphClientOld;
import org.graphstream.distributed.graph.DGraphCore;

import com.sun.corba.se.impl.orbutil.graph.Graph;

/**
 * Default implementation for a graph reader listener. It takes as argument a
 * graph, and modifies this graph according to the events received. This is a
 * simple and quick way to build a graph from a graph reader.
 *
 * @author Yoann Pigne
 * @author Antoine Dutot
 * @since 20060101
 * @see GraphReader
 * @see GraphReaderListener
 */
public class GraphReaderListenerHelperDistributedLocal2 implements GraphReaderListener
{
	/**
	 * A reference to the graph it modifies.
	 */

	private DGraphEdgeInfo e ;
	private DGraphParseTag parser ;

	private DGraphCore g ;

	/**
	 * New default graph reader listener that modifies the given graph according
	 * to the received events.
	 * @param graph The graph to modify according to events.
	 */
	public GraphReaderListenerHelperDistributedLocal2(Graph aGraph, Graph aVgraph, DGraphClientOld c)
	{
		e = new DGraphEdgeInfo() ;
		parser = new DGraphParseTag() ;
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#edgeAdded(java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	public void edgeAdded(String id, String from, String to, boolean directed, Map<String, Object> attributes) throws GraphParseException
	{
		try {
			g.addEdge(id, from, to, directed, attributes);
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
		//Edge edge = graph.getEdge(id);
		//if( attributes != null && edge != null )
			//edge.addAttributes( attributes );
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#edgeRemoved(java.lang.String)
	 */
	public void edgeRemoved(String id) throws GraphParseException
	{
		try {
			g.removeEdge(id);
		}
		catch(RemoteException e) {
			System.out.println("RemoteException : " + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#graphChanged(java.util.Map)
	 */
	public void graphChanged(Map<String, Object> attributes) throws GraphParseException
	{
		/*if( attributes != null )
			graph.addAttributes( attributes );*/
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeAdded(java.lang.String, java.util.Map)
	 */
	public void nodeAdded(String id, Map<String, Object> attributes) throws GraphParseException {
		try {
			g.addNode(id, attributes);
		}
		catch(RemoteException e) {
			System.out.println("RemoteException : " + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeChanged(java.lang.String, java.util.Map)
	 */
	public void nodeChanged(String id, Map<String, Object> attributes) throws GraphParseException
	{
		// nodechanged
	}

	/* (non-Javadoc)
	 * @see org.miv.graphstream.io.GraphReaderListener#nodeRemoved(java.lang.String)
	 */
	public void nodeRemoved(String id) throws GraphParseException {
		try {
			g.removeNode(id);
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