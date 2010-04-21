package org.graphstream.distributed.rmi;

import java.rmi.Remote;

public interface RMIDGraphAdapter extends Remote {
	
	public void bind(String name) throws java.rmi.RemoteException ;

	public void init(String graphClass, String[] params) throws java.rmi.RemoteException ;

	public void clear() throws java.rmi.RemoteException ;

	public void registerNotify(String uri) throws java.rmi.RemoteException ;

	public void unregisterNotify(String id) throws java.rmi.RemoteException ;

	
	
	public Object exec(String requestId, String objectId, String method, Object[] params) throws java.rmi.RemoteException ;

	public Object[] exec(String[] requestIds, String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException ;
	
	public String hello(String name) throws java.rmi.RemoteException ;

	
	
}
