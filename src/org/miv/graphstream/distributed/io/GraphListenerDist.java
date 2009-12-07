/*
 * This file is part of GraphStream.
 *
 * GraphStream is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GraphStream is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GraphStream.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2006 - 2009
 * 	Julien Baudry
 * 	Antoine Dutot
 * 	Yoann Pigné
 * 	Guilhelm Savin
 */

package org.miv.graphstream.distributed.io;

import org.miv.graphstream.distributed.common.GraphEdgeInfo;
import org.miv.graphstream.distributed.common.GraphParseTag;
import org.miv.graphstream.distributed.graph.DistGraphClient;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.GraphListener;

/**
 * A void implementation of {@link org.miv.graphstream.graph.GraphListener} that a
 *
 * Inherit the class and override some of its methods so as to handle some of the services of the
 * <code>GraphListener</code> interface.
 *
 * @since 2008/08/01
 * @see org.miv.graphstream.graph.GraphListener
 *
 */
public class GraphListenerDist implements GraphListener
{

	/**
	 * A reference to the graph it modifies.
	 */
	protected DistGraphClient L;
	protected Graph graph ;
	protected Graph vGraph ;

	private GraphEdgeInfo e ;
	private GraphParseTag parser ;


	public void edgeAdded( String graphId, String edgeId, String fromNodeId, String toNodeId,
            boolean directed )
    {
		System.out.println("edgeAdded");
    }

	public void edgeRemoved( String graphId, String edgeId )
    {
		System.out.println("edgeRemoved");
    }

	public void edgeAttributeAdded( String graphId, String edgeId, String attribute, Object value )
    {
		System.out.println("edgeAttributeAdded");
    }

	public void edgeAttributeChanged( String graphId, String edgeId, String attribute, Object oldValue, Object newValue )
    {
		System.out.println("edgeAttributeChanged");
    }

	public void edgeAttributeRemoved( String graphId, String edgeId, String attribute )
    {
		System.out.println("edgeAttributeRemoved");
    }

	public void nodeAdded( String graphId, String nodeId )
    {
		System.out.println("nodeAdded");
    }

	public void nodeRemoved( String graphId, String nodeId )
    {
		System.out.println("nodeRemoved");
    }

	public void graphCleared( String graphId )
	{
		System.out.println("graphCleared");
	}

	public void nodeAttributeAdded( String graphId, String nodeId, String attribute, Object value )
    {
		System.out.println("nodeAttributeAdded");
    }

	public void nodeAttributeChanged( String graphId, String nodeId, String attribute, Object oldValue, Object newValue )
    {
		System.out.println("nodeAttributeChanged");
    }

	public void nodeAttributeRemoved( String graphId, String nodeId, String attribute )
    {
		System.out.println("nodeAttributeRemoved");
    }

	public void graphAttributeAdded( String graphId, String attribute, Object value )
    {
		System.out.println("graphAttributeAdded");
    }

	public void graphAttributeChanged( String graphId, String attribute, Object oldValue, Object newValue )
    {
		System.out.println("graphAttributeChanged");
    }

	public void graphAttributeRemoved( String graphId, String attribute )
    {
		System.out.println("graphAttributeRemoved");
    }

	public void stepBegins( String graphId, double time )
    {
		System.out.println("stepBegins");
    }
}