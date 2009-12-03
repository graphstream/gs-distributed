package org.miv.graphstream.distributed.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.UnicastRemoteObject;

import org.miv.graphstream.distributed.graph.DistGraph;

public class DistGraphServerImpl extends UnicastRemoteObject implements DistGraphServer {

	/*
	 * Variables
	 */
	private String Id ;
	private DistGraph aDistGraph ;

	private static final long serialVersionUID = 0001;

	/*
	 * Constructor
	 */

	public DistGraphServerImpl(String id) throws java.rmi.RemoteException {
		this.Id = id ;
	}

	/*
	 * instantiate a distGraph
	 */
	public void newDistGraph(String graphClass) throws java.rmi.RemoteException {
		this.aDistGraph = new DistGraph(graphClass);
	}

	/*
	 * (non-Javadoc)
	 * @see org.miv.graphstream.distributed.rmi.DistGraphServer2#clearGraph()
	 */
	public void delDistGraph() throws java.rmi.RemoteException {
		this.aDistGraph = null ;
	}

	/**
	 * notification
	 */
	public void notifyDistGraphCreation(String uri) throws java.rmi.RemoteException {
		this.aDistGraph.getClient().addLocal(uri);
	}

	/**
	 *
	 * @param graphId
	 * @throws java.rmi.RemoteException
	 */
	public void notifyDistGraphDeletion(String graphId) throws java.rmi.RemoteException {
		this.aDistGraph.getClient().delLocal(graphId);
	}

	/*
	 * Default request
	 */
	public Object exec(String methode, Object[] params) throws java.rmi.RemoteException {
		return dynamicInvocation(this.aDistGraph.getGraph(), methode, params);
	}

	/*
	 * Default request without parameter
	 */
	public Object exec(String methode) throws java.rmi.RemoteException {
		return dynamicInvocation(this.aDistGraph.getGraph(), methode, new Object[] {});
	}

	/*
	 * Generic request
	 */
	public Object exec(String objectId, String methode, Object[] params) throws java.rmi.RemoteException {
		return dynamicInvocation(this.aDistGraph.getObjects().get(objectId), methode, params);
	}

	/*
	 * Generic request without parameter
	 */
	public Object exec(String objectId, String methode) throws java.rmi.RemoteException {
		return dynamicInvocation(this.aDistGraph.getObjects().get(objectId), methode, new Object[] {});
	}


	/*
	 * Generic request multi
	 */
	public Object[] exec(String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException {
		Object[] res = new Object[objectIds.length];
		for(int i = 0 ; i < objectIds.length ; i++ ) {
			res[i] = dynamicInvocation(this.aDistGraph.getObjects().get(objectIds[i]), methods[i], params[i]);
		}
		return res ;
	}

	/*
	 * (non-Javadoc)
	 * Default multi generic request
	 */
	public Object[] exec(String[] methods, Object[][] params) throws java.rmi.RemoteException {
		Object[] res = new Object[methods.length];
		for(int i = 0 ; i < methods.length ; i++ ) {
			res[i] = dynamicInvocation(this.aDistGraph.getGraph(), methods[i], params[i]);
		}
		return res ;
	}


	/*
	 * Dynamique invocation
	 */
	private Object dynamicInvocation(Object anObject, String methode, Object[] params) {
		try {
			Class[] argType = new Class[params.length] ;
		  	for(int i = 0 ; i < argType.length ; i++) {
		  		argType[i] = params[i].getClass();
		  	}
		  	Method m = anObject.getClass().getMethod(methode, argType);
			Object res = m.invoke(anObject, params);

			// bidouille ! pour savoir si une classe est serializable ou pas (je regarde si c'est une classe du package java ou pas)
			// a modifier
			System.out.println("res : " + res);
			if(res != null && !res.getClass().getName().startsWith("org.miv")) {
				return res ;
			}
			else {
				return "no result" ;
			}
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return null ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return  null ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception : " + e.getMessage() + e.getCause());
			return null ;
		}
	}


}
