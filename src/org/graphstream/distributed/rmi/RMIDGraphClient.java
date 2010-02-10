package org.graphstream.distributed.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.graphstream.distributed.common.DGraphUri;

public class RMIDGraphClient {

	// Fields

	private RMIDGraphAdapter DGraph ;
	private DGraphUri Uri ;


	// Constructor

	public RMIDGraphClient() {
	}
	
	
	public RMIDGraphAdapter register(DGraphUri anUri) {
		try {
			//System.out.println("" + this.Uri.getHost() + this.Uri.getDGraphId());
			this.DGraph = (RMIDGraphAdapter)Naming.lookup("rmi://" + anUri.getHost()+ "/" + anUri.getDGraphId()) ;
			return this.DGraph ;
		}
		catch (RemoteException exp) {
				System.out.println("RemoteException dans DistGraphServerRmiHandler : " +	exp);
				return null ;
		}
		catch (NotBoundException exp) {
				System.out.println("NotBoundException dans DistGraphServerRmiHandler : "	+ exp);
				return null ;
		}
		catch (MalformedURLException exp) {
				System.out.println("MalformedURLException dans DistGraphServerRmiHandler : " + exp);
				return null ;
		}
	}


	// getGraphConnector
	public RMIDGraphAdapter getDGraph() {
		return this.DGraph ;
	}

	// getGraphResourceIdentifier
	public DGraphUri getDGraphUri() {
		return this.Uri ;
	}

}
