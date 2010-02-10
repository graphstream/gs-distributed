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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.distributed.common.DGraphEdgeInfo;


/**
 * Convert a graph file in a given format to another file in another format.
 *
 * @author Antoine Dutot
 * @author Yoann Pign�
 * @since 2007
 */
public class GraphConvertDistributed implements GraphReaderListenerExtended
{
	protected GraphReader reader;

	protected GraphWriter writer;

	static private HashMap<String,DGraphEdgeInfo> edgeIndex ;

	int nbNodeAddedd ;

	public static void main( String args[] )
	{
		edgeIndex = new HashMap<String,DGraphEdgeInfo>() ;

		new GraphConvertDistributed( args );

	}

	public GraphConvertDistributed( String args[] )
	{
		if( args.length >= 2 )
		{
			try
			{
				convert( args[0], args[1] );
			}
			catch( GraphParseException e )
			{
				e.printStackTrace();
				System.exit( 1 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
				System.exit( 1 );
			}
		}
	}

	/**
	 *
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 * @throws GraphParseException
	 */
	public void convert( String fromFile, String toFile )
		throws IOException, GraphParseException
	{
		reader = GraphReaderFactory.readerFor( fromFile );
		writer = GraphWriterFactory.writerFor( toFile );

		reader.addGraphReaderListener( this );
		reader.begin( fromFile );

		writer.begin(toFile, "myGraph");
		while( reader.nextEvents() );
		reader.end();
		writer.end();
	}

// GraphReaderListener

	/***
	 *
	 */
	public void edgeAdded( String id, String from, String to, boolean directed, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			DGraphEdgeInfo edgeInfo = new DGraphEdgeInfo(modifyId(from),modifyId(to)) ;
			edgeIndex.put(id, edgeInfo);
			if(edgeInfo.isIntraEdge()) {
				writer.addEdge( edgeInfo.getGraphTag1()+"/"+id, modifyId(from), modifyId(to), directed, attributes );
			}
			else {
				writer.addEdge( id, modifyId(from), modifyId(to), directed, attributes );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	/**
	 *
	 */
	public void graphChanged( String attribute, Object value, boolean removed ) throws GraphParseException {

	}

	/**
	 *
	 * @param id
	 * @param attributes
	 * @throws GraphParseException
	 */
	public void edgeChanged( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			DGraphEdgeInfo edgeInfo = edgeIndex.get(id) ;
			if(edgeInfo.isIntraEdge()) {
				writer.changeEdge( edgeInfo.getGraphTag1()+"/"+id, attributes );
			}
			else {
				writer.changeEdge( id, attributes );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}


	/**
	 *
	 */
	public void edgeChanged( String id, String attribute, Object value, boolean removed  ) throws GraphParseException {

	}


	/**
	 *
	 */
	public void edgeRemoved( String id ) throws GraphParseException
	{
		try
		{
			DGraphEdgeInfo edgeInfo = edgeIndex.get(id) ;
			if(edgeInfo.isIntraEdge()) {
				writer.delEdge( edgeInfo.getGraphTag1()+"/"+id );
			}
			else {
				writer.delEdge( id );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}


	/**
	 *
	 * @param attributes
	 * @throws GraphParseException
	 */
	public void graphChanged( Map<String, Object> attributes ) throws GraphParseException
	{
/*		try
		{
*/			/* TODO no equivalent in writer ?? */
/*		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
*/	}


	/**
	 *
	 */
	public void nodeAdded( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			writer.addNode( modifyId(id), attributes );
			this.nbNodeAddedd++ ;
			System.out.println("-->" + this.nbNodeAddedd);
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	/**
	 *
	 */
	public void nodeChanged( String id, String attribute, Object value, boolean removed ) throws GraphParseException
	{
		/*try
        {
	        writer.changeNode( id, attributes );
        }
        catch( IOException e )
        {
        	lastError = e;
	        e.printStackTrace();
        }*/
	}

	public void nodeRemoved( String id ) throws GraphParseException
	{
		try
		{
			writer.delNode( modifyId(id) );
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void stepBegins( double time ) throws GraphParseException
	{
		try
		{
			writer.step( time );
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void unknownEventDetected( String unknown ) throws GraphParseException
	{
		System.err.printf( "Ignored graph event: %s%n", unknown );
	}

	private String modifyId(String id) {
		/*if(Integer.parseInt(id) < 100) {
			return "g01/"+id ;
		}
		else {
			return "g02/"+id ;
		}*/
		return "obl/"+id ;
	}
}