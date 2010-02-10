package org.graphstream.distributed.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.graphstream.distributed.common.DGraphUri;

public class RMIDGraphHelper {

	public static void main(String[] args) {
		
	}
	
	private RMIDGraphHelper() {
	}
	
	public static void bind() {
		System.out.println("bind");
	}
	
	/*
	 * register
	 */
	public static RMIDGraphAdapter register(DGraphUri uri) {
		try {
			return (RMIDGraphAdapter)Naming.lookup("rmi://"+uri.getHost()+"/"+uri.getDGraphId()) ;
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
