package org.miv.graphstream.distributed.test;

import java.rmi.server.UnicastRemoteObject;

public class TestDistGraphServerImpl extends UnicastRemoteObject implements TestDistGraphServer {
	
	private static final long serialVersionUID = 0001;
	
	public TestDistGraphServerImpl() throws java.rmi.RemoteException {
		
	}
	
	public String hello(String mess) throws java.rmi.RemoteException {
		System.out.println("hello voici le message " + mess);
		return mess ;
	}

}
