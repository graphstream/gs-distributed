package org.graphstream.distributed.test;

import java.rmi.Naming;

public class TestDistGraphClient {

	public static void main( String [] args )  {
		try {
			TestDistGraphServer s = (TestDistGraphServer) Naming.lookup("rmi://"+args[0]+"/"+args[1]);
	    	s.hello(args[2]);
	    }
	    catch( Exception e ) {
	      e.printStackTrace();
	    }
	  }
	
}
