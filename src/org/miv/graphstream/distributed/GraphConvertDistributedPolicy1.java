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

package org.miv.graphstream.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.miv.graphstream.distributed.utile.GraphEdgeInfo;
import org.miv.graphstream.io.GraphParseException;
import org.miv.graphstream.io.GraphReader;
import org.miv.graphstream.io.GraphReaderFactory;
import org.miv.graphstream.io.GraphReaderListener;
import org.miv.graphstream.io.GraphWriter;
import org.miv.graphstream.io.GraphWriterFactory;


/**
 * Convert a graph file in a given format to another file in another format.
 *
 * @author Antoine Dutot
 * @author Yoann Pigné
 * @since 2007
 */
public class GraphConvertDistributedPolicy1 implements GraphReaderListener
{
	protected GraphReader reader;

	protected GraphWriter writer;

	static private HashMap<String,GraphEdgeInfo> edgeIndex ;
	static private HashMap<String,String> nodeIndex ;

	// inputs
	static String fileSrc ;
	static String fileDest ;
	static int distDegree ;

	// metadata
	static int nbNodeAdded ;
	static int nbNodeMax ;
	static int[] steps ;
	static String[] tags ;

	public static void main( String args[] ) {
		if( args.length >= 3 ) {
			System.out.println("Fichier source " + args[0] + " en cours de traitement") ;
			new GraphConvertDistributedPolicy1(args) ;
			System.out.println("Fichier source traité - génération du fichier " + args[1]) ;
		}
	}

	private GraphConvertDistributedPolicy1(String[] args)	{
		try
		{
			System.out.println("initialisation ... (1/2) ");
			init(args);
			System.out.println("conversion ... 2/2");
			convert( fileSrc, fileDest );
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


	public static void init(String[] args) {
		//inputs
		fileSrc=args[0] ;
		fileDest=args[1];
		distDegree=(args.length-2);

		//instanciation
		edgeIndex = new HashMap<String,GraphEdgeInfo>() ;
		nodeIndex = new HashMap<String,String>() ;
		nbNodeAdded = 0 ;
		steps = new int[distDegree+1] ;
		tags = new String[distDegree] ;

		//getMetaData
		getDGSMetadata(fileSrc);

		// init tags Tab
		for(int j = 2 ; j < args.length ; j++) {
			tags[j-2] = args[j];
		}

		// init steps
		for(int i = 0 ; i <= distDegree ; i++) {
			steps[i] = (int)((nbNodeMax*i/distDegree)) ;
		}
	}

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

	public void edgeAdded( String id, String from, String to, boolean directed, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			if(!nodeIndex.containsKey(from)) {
				nodeIndex.put(from, modifyId(from));
			}
			if(!nodeIndex.containsKey(to)) {
				nodeIndex.put(from, modifyId(to));
			}
			GraphEdgeInfo edgeInfo = new GraphEdgeInfo(getNodeNewId(from),getNodeNewId(to)) ;
			edgeIndex.put(id, edgeInfo);
			if(edgeInfo.isIntraEdge()) {
				writer.addEdge( edgeInfo.getGraphTag1()+"/"+id, getNodeNewId(from), getNodeNewId(to), directed, attributes );
			}
			else {
				writer.addEdge( id, getNodeNewId(from), getNodeNewId(to), directed, attributes );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void edgeChanged( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			GraphEdgeInfo edgeInfo = edgeIndex.get(id) ;
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

	public void edgeRemoved( String id ) throws GraphParseException
	{
		try
		{
			GraphEdgeInfo edgeInfo = edgeIndex.get(id) ;
			if(edgeInfo.isIntraEdge()) {
				edgeIndex.remove(edgeInfo.getGraphTag1()+"/"+id);
				writer.delEdge( edgeInfo.getGraphTag1()+"/"+id );
			}
			else {
				edgeIndex.remove(id);
				writer.delEdge( id );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

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

	public void nodeAdded( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try	{
			nbNodeAdded++;
			String newId = modifyId(id) ;
			nodeIndex.put(id, newId);
			writer.addNode( newId, attributes );
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void nodeChanged( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try
		{
			writer.changeNode( getNodeNewId(id), attributes );
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void nodeRemoved( String id ) throws GraphParseException
	{
		try
		{
			nodeIndex.remove(id);
			writer.delNode( getNodeNewId(id) );
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
		String res = "g/" ;
		for(int i = 0 ; i < distDegree ; i++) {
			if((steps[i]<nbNodeAdded) & (nbNodeAdded<=steps[i+1])) {
				res = tags[i]+"/" ;
			}
		}
		return res+id ;
	}

	private String getNodeNewId(String id) {
		return nodeIndex.get(id) ;
	}

	@SuppressWarnings("unused")
    private String getEdgeNewId(String id) {
		return "" ;
		//return this.edgeIndex.get(id). ;
	}

	private static void getDGSMetadata(String inputFile) {
	    try {
		    String ligne ;
		    String action ;
		    BufferedReader lecteurAvecBuffer = new BufferedReader(new FileReader(inputFile));
	    	 while ((ligne = lecteurAvecBuffer.readLine()) != null) {
	 	    	action = ligne.substring(0, 2) ;
	 	    	if(action.equals("an")) {
	 	    		nbNodeMax++ ;
	 	    	}
	    	 }
	 	    lecteurAvecBuffer.close();
	    }
	    catch(FileNotFoundException exc) {
	    	System.out.println("Erreur d'ouverture");
	    }
	    catch(IOException e) {
	    	System.out.println("EIOException " + e.getMessage());
	    }
	}

	// initialisation des paliers pour la segmentation du graph
	@SuppressWarnings("unused")
    private static void initStep() {
		steps = new int[distDegree+1] ;
		for(int i = 0 ; i <= distDegree ; i++) {
			steps[i] = (int)((nbNodeMax*i/distDegree)) ;
		}
	}

}