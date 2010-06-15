package org.graphstream.distributed.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.graphstream.distributed.common.EnumReg;

public class RMIHelper {
	
	private RMIHelper() {
	}
	
	public static void bind(String id, String host) {
		try	{
			Naming.rebind( String.format( "//"+host+"/%s", id ), new RMIDGraph(id) );
			System.out.println("binding " + id + " on " + host + " done");
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Register
	 * uri : rmi:<host>:<port>/<id>
	 */
	public static RMIDGraphAdapter register(String uri) {
		try {
			return (RMIDGraphAdapter)Naming.lookup(uri) ;
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
	
	/**
	 * 
	 * @return
	 */
	public static Object RMICall(RMIDGraphAdapter DGraph, String method, Object[] params) {
		try {
			return DGraph.exec(EnumReg.DGraph, method, params);
		}
		catch(RemoteException e) {
			System.out.println("Error rmiCall : " + e.getMessage() + "parametres " + method + " " + DGraph);
			return null ;
		}
	}
	
}
