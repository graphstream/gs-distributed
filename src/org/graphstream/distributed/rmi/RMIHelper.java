package org.graphstream.distributed.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumUri;

public class RMIHelper {
	
	private RMIHelper() {
	}
	
	public static void bind() {
		System.out.println("bind");
	}
	
	/*
	 * register
	 * rmi:<host>:<port>/<id>:<graphClass>
	 */
	public static RMIDGraphAdapter register(String uri) {
		Map<String, Object> d = DGraphParser.uri(uri);
		try {
			return (RMIDGraphAdapter)Naming.lookup("rmi://"+d.get(EnumUri.Host)+"/"+d.get(EnumUri.DGraphName)) ;
		}
		catch (RemoteException exp) {
				System.out.println("RemoteException dans register : " +	exp);
				return null ;
		}
		catch (NotBoundException exp) {
				System.out.println("NotBoundException dans register : "	+ exp);
				return null ;
		}
		catch (MalformedURLException exp) {
				System.out.println("MalformedURLException dans register : " + exp);
				return null ;
		}
	}
	
}
