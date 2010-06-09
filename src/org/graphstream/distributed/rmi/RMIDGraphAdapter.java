package org.graphstream.distributed.rmi;

import java.rmi.Remote;

public interface RMIDGraphAdapter extends Remote {
	
	/*
	 * exec simple
	 */
	public Object exec(String requestId, String objectId, String method, Object[] params) throws java.rmi.RemoteException ;

	/*
	 * exec multiple
	 */
	public Object[] exec(String[] requestIds, String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException ;
	
	/*
	 * function test
	 */
	public String hello(String name) throws java.rmi.RemoteException ;

	
}
