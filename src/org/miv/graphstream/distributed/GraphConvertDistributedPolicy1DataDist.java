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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.miv.graphstream.distributed.utile.GraphEdgeInfo;
import org.miv.graphstream.distributed.utile.GraphNodeInfo;
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
public class GraphConvertDistributedPolicy1DataDist implements GraphReaderListener
{
	// reader writer
	protected GraphReader reader;
	protected GraphWriter writer;

	// index
	static private HashMap<String,GraphEdgeInfo> edgeIndex ;
	static private HashMap<String,GraphNodeInfo> nodeIndex ;
	static private HashMap<String,GraphWriter> writerIndex ;
	static private PrintWriter indexFile ;
	static private int stepIndex ;

	// inputs
	static String fileSrc ;

	// outputs
	static String fileId ;
	static String fileIndex ;
	static String[] fileDest ;
	static String fileSrcName ;
	static String folder ;

	// metadata
	static int distDegree ;
	static int nbNodeAdded ;
	static int nbNodeMax ;
	static int[] steps ;
	static String[] tags ;


	// Constructor

	public GraphConvertDistributedPolicy1DataDist() {
	}

	public void run(String[] args) {
		try	{
			System.out.println("initialisation ... (1/2) ");
			init(args);
			System.out.println("tag ... (2/2)");
			convert( fileSrc, fileId );
			System.out.println("done ...");
		}
		catch( GraphParseException e ) {
			e.printStackTrace();
			System.exit( 1 );
		}
		catch( IOException e ) {
			e.printStackTrace();
			System.exit( 1 );
		}
	}


	// Initialisation
	public static void init(String[] args) {
		//inputs
		fileSrc = args[0] ;
		fileId = args[1] ;
		distDegree = (args.length-2) ;
		fileIndex = "index";
		stepIndex = 0 ;
		folder = (new File(fileSrc)).getParent();

		//instanciation
		edgeIndex = new HashMap<String,GraphEdgeInfo>() ;
		nodeIndex = new HashMap<String,GraphNodeInfo>() ;
		writerIndex = new HashMap<String,GraphWriter>() ;

		nbNodeAdded = 0 ;
		steps = new int[distDegree+1] ;
		tags = new String[distDegree] ;
		fileDest = new String[distDegree] ;


		//getMetaData
		getDGSMetadata(fileSrc);

		// init tags Tab
		indexFile =  newIndexFile(	folder + "/" +
									fileId + "-" +
									"d" + distDegree 	+ "-" +
									fileIndex 			+ "." + ext(fileSrc)) ;
									//fileSrc);

		for(int j = 2 ; j < args.length ; j++) {
			tags[j-2] = args[j];
		}

		for(int i = 0 ; i < tags.length ; i++) {
			fileDest[i] = 	folder + "/" +
							fileId + "-" +
							"d"+distDegree 	+ "-" +
							tags[i]  		+ "." + ext(fileSrc) ;
							//fileSrc ;
		}

		// init steps
		for(int i = 0 ; i <= distDegree ; i++) {
			steps[i] = (int)((nbNodeMax*i/distDegree)) ;
		}
	}

	public static String ext(String file) {
		return file.substring(file.length()-3, file.length());
	}

	public void convert( String fromFile, String toFileId )
		throws IOException, GraphParseException
	{
		reader = GraphReaderFactory.readerFor( fromFile );

		for(int i = 0 ; i < distDegree ; i++) {
			writerIndex.put(tags[i], GraphWriterFactory.writerFor( fileDest[i] ));
			writerIndex.get(tags[i]).begin(fileDest[i], fileDest[i]);
		}

		reader.addGraphReaderListener( this );
		reader.begin( fromFile );

		while( reader.nextStep() );
		reader.end();

		for(int i = 0 ; i < distDegree ; i++) {
			writerIndex.get(tags[i]).end();
		}
		closeIndexFile() ;
	}

// GraphReaderListener

	public void edgeAdded( String id, String from, String to, boolean directed, Map<String, Object> attributes ) throws GraphParseException	{
		try	{
			wIndexFile(nodeIndex.get(from).getTag());
			if(!nodeIndex.containsKey(from)) {
				nodeIndex.put(from, new GraphNodeInfo(from));
			}
			if(!nodeIndex.containsKey(to)) {
				nodeIndex.put(from, new GraphNodeInfo(to));
			}
			GraphEdgeInfo edgeInfo = new GraphEdgeInfo(getNodeNewId(from),getNodeNewId(to)) ;
			edgeIndex.put(id, edgeInfo);
			if(edgeInfo.isIntraEdge()) {
				writerIndex.get(edgeInfo.getGraphTag1()).addEdge( edgeInfo.getGraphTag1()+"/"+id, getNodeNewId(from), getNodeNewId(to), directed, attributes );
			}
			else {
				writerIndex.get(edgeInfo.getGraphTag1()).addEdge( id, getNodeNewId(from), getNodeNewId(to), directed, attributes );
			}
		}
		catch( IOException e ) {
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void edgeChanged( String id, Map<String, Object> attributes ) throws GraphParseException	{
		try	{
			GraphEdgeInfo edgeInfo = edgeIndex.get(id) ;
			wIndexFile(edgeInfo.getGraphTag1());

			if(edgeInfo.isIntraEdge()) {
				writerIndex.get(edgeInfo.getGraphTag1()).changeEdge( edgeInfo.getGraphTag1()+"/"+id, attributes );
			}
			else {
				writerIndex.get(edgeInfo.getGraphTag1()).changeEdge( id, attributes );
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
			wIndexFile(edgeInfo.getGraphTag1());
			if(edgeInfo.isIntraEdge()) {
				edgeIndex.remove(edgeInfo.getGraphTag1()+"/"+id);
				writerIndex.get(edgeInfo.getGraphTag1()).delEdge( edgeInfo.getGraphTag1()+"/"+id );
			}
			else {
				edgeIndex.remove(id);
				writerIndex.get(edgeInfo.getGraphTag1()).delEdge( id );
			}
		}
		catch( IOException e )
		{
			throw new GraphParseException( "I/O error while writing (" + e.getClass().getSimpleName() + "): " + e.getMessage() );
		}
	}

	public void graphChanged( Map<String, Object> attributes ) throws GraphParseException
	{
		// to implement ????
	}

	public void nodeAdded( String id, Map<String, Object> attributes ) throws GraphParseException
	{
		try	{
			nbNodeAdded++;
			GraphNodeInfo nodeInf = modifyId(id);
			nodeIndex.put(id, nodeInf);
			wIndexFile(nodeIndex.get(id).getTag());
			writerIndex.get(nodeInf.getTag()).addNode( nodeInf.getNodeId(), attributes );
			if(id.equalsIgnoreCase("0895032953")) {
				System.out.println("0895032953" + nodeIndex.get(id).getTag());
			}
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
			if(id.equalsIgnoreCase("0895032953")) {
				System.out.println("0895032953" + nodeIndex.get(id).getTag() + "-" + nodeIndex.get(id).getNodeId());
			}
			//GraphNodeInfo nodeInf = modifyId(id);
			wIndexFile(nodeIndex.get(id).getTag());
			writerIndex.get(nodeIndex.get(id).getTag()).changeNode( nodeIndex.get(id).getNodeId(), attributes );
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
			wIndexFile(nodeIndex.get(id).getTag());
			writerIndex.get(nodeIndex.get(id).getTag()).delNode( nodeIndex.get(id).getNodeId() );
			nodeIndex.remove(id);
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
			//writer.step(stepIndex);
			for(int i = 0 ; i< tags.length; i++) {
				writerIndex.get(tags[i]).step(stepIndex);
			}
			stepIndex++;
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

	private GraphNodeInfo modifyId(String id) {
		GraphNodeInfo nodeInf = new GraphNodeInfo() ;
		for(int i = 0 ; i < distDegree ; i++) {
			if((steps[i]<nbNodeAdded) & (nbNodeAdded<=steps[i+1])) {
				nodeInf.newNode(tags[i], id);
			}
		}
		return nodeInf ;
	}

	private String getNodeNewId(String id) {
		return nodeIndex.get(id).getNodeId() ;
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


	// create new indexFile and open it for writing
	private static PrintWriter newIndexFile(String fileName) {
		try {
			return (new PrintWriter(new BufferedWriter(new FileWriter(fileName))));
		}
		catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			return null ;
		}
	}

	private static void wIndexFile(String data) {
		indexFile.println(data);
	}

	private static void closeIndexFile() {
		indexFile.close();
	}

}