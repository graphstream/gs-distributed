package org.graphstream.distributed.stream.old;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.zip.GZIPInputStream;

public class DistributedGraphRunDgsMessage {

	public DistributedGraphRunDgsMessage() {
		//lecture() ;
	}

	public StreamTokenizer lecture(String file) throws IOException	{
		InputStream is = null;
		try	{
			is = new GZIPInputStream( new FileInputStream( file ) );
		}
		catch( IOException e ) {
			is = new FileInputStream( file );
		}
		return new StreamTokenizer( new BufferedReader( new InputStreamReader( is ) ) );
	}

}
