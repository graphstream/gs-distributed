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

import java.io.IOException;

import org.miv.graphstream.graph.GraphListener;
import org.miv.graphstream.io2.file.FileInputDGS;
import org.miv.graphstream.io2.file.FileOutputDGS;

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
public class DistGraphConverterDGS implements GraphListener
{

	/**
	 * A reference to the graph it modifies.
	 */
	protected FileInputDGS I ;
	protected FileOutputDGS O ;

	public DistGraphConverterDGS() {
		try {
			I = new FileInputDGS();
			I.addGraphListener(this);
			I.begin("/home/baudryj/workspace-java/gs-distributed/bin/org/miv/graphstream/distributed/data/aaa.dgs");
			O = new FileOutputDGS();
			O.begin("file.txt");
			while(I.nextEvents()) {

			}
			I.end();
			O.end();
		}
		catch(IOException e) {
			System.out.println("DistGraphConverterDGS : " + e.getMessage());
		}

	}

	public void edgeAdded( String graphId, String edgeId, String fromNodeId, String toNodeId,
            boolean directed )
    {
		System.out.println("edgeAdded" + graphId + edgeId + fromNodeId + toNodeId);
		O.edgeAdded(graphId, edgeId, fromNodeId, toNodeId, directed);
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
		System.out.println("nodeAdded" + graphId +"--" + nodeId + O);
		O.nodeAdded(graphId, nodeId);
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