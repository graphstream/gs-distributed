package org.miv.graphstream.distributed.rmi;

import java.rmi.Remote;

public interface DistGraphServer extends Remote {

	public void newDistGraph(String graphClass) throws java.rmi.RemoteException ;

	public void delDistGraph() throws java.rmi.RemoteException ;

	public void notifyDistGraphCreation(String uri) throws java.rmi.RemoteException ;

	public void notifyDistGraphDeletion(String id) throws java.rmi.RemoteException ;

	public Object exec(String objectInstanceName, String methode, Object[] params) throws java.rmi.RemoteException ;

	public Object exec(String methode, Object[] params) throws java.rmi.RemoteException ;

	public Object exec(String methode) throws java.rmi.RemoteException ;

	public Object exec(String objectId, String methode) throws java.rmi.RemoteException ;

	public Object[] exec(String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException ;

	public Object[] exec(String[] methods, Object[][] params) throws java.rmi.RemoteException ;

}
