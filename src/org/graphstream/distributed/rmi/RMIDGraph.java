package org.graphstream.distributed.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import org.graphstream.distributed.common.DGraphUri;
import org.graphstream.distributed.common.EnumRegistry;
import org.graphstream.distributed.common.EnumUri;
import org.graphstream.distributed.graph.DGraph;
import org.graphstream.distributed.graph.DGraphManager;

public class RMIDGraph extends UnicastRemoteObject implements RMIDGraphAdapter {

	/*
	 * Variables
	 */
	private String Id ;
	
	private ConcurrentHashMap<String, Object> Registry ;
	
	private static final long serialVersionUID = 0001234543456;

	/*
	 * Constructor
	 */
	public RMIDGraph() throws java.rmi.RemoteException {	
		this.Registry = new ConcurrentHashMap<String, Object>() ;
		this.Registry.put(EnumRegistry.RmiDGraph, this);
		this.Registry.put(EnumRegistry.Registry, this.Registry);		
		this.Registry.put(EnumRegistry.DGraph, new DGraph());
		this.Registry.put(EnumRegistry.Manager, new DGraphManager(""));		
	}

	
	/*
	 * instantiate a distGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		((DGraph)this.Registry.get(EnumRegistry.DGraph)).init(graphClass);
	}
	
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void bind(String id) throws RemoteException {
		try	{
			this.Id = id ;
			System.out.println("binding begin for " + id + "on " + System.getenv("hostname"));
			Naming.rebind( String.format( "//localhost/%s", this.Id ), this );
			System.out.println("binding done");
			System.out.println("-------------");
		}
		catch( Exception e ) {
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * 
	 */
	public void clear() throws java.rmi.RemoteException {
		this.Registry.clear();
	}

	/**
	 * notification
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	public void registerNotify(String uri) {
		// TODO Auto-generated method stub
		DGraphUri u = new DGraphUri(uri);
		try {
			this.Registry.put(u.getElement(EnumUri.DGraphName), (RMIDGraphAdapter)Naming.lookup("rmi://"+u.getElement(EnumUri.Host)+"/"+u.getElement(EnumUri.DGraphName)));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void unregisterNotify(String id) throws RemoteException {
		// TODO Auto-generated method stub
	}


	/**
	 * Generic request
	 */
	public Object exec(String requestId, String objectId, String methode, Object[] params) throws java.rmi.RemoteException {
		return dynamicCall(this.Registry.get(objectId), methode, params);
	}

	/**
	 * Generic request multi
	 */
	public Object[] exec(String[] requestId, String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException {
		Object[] res = new Object[objectIds.length];
		for(int i = 0 ; i < objectIds.length ; i++ ) {
			res[i] = null ;
			//res[i] = dynamicCall(this.DGraph.getObjects().get(objectIds[i]), methods[i], params[i]);
		}
		return res ;
	}


	/*
	 * Dynamique invocation
	 */
	private Object dynamicCall(Object anObject, String aMethod, Object[] params) {
		try {
			Class[] argType ;
			if(params!=null) {
				argType = new Class[params.length] ;
			} else {
				argType = new Class[0];
			}
		  	for(int i = 0 ; i < argType.length ; i++) {
		  		argType[i] = params[i].getClass();
		  	}
		  	Method m = anObject.getClass().getMethod(aMethod, argType);
			Object res = m.invoke(anObject, params);

			// pour savoir si une classe est serializable ou pas (je regarde si c'est une classe du package java ou pas)
			// a modifier
			if(res != null && !res.getClass().getName().startsWith("org.graphstream")) {
				return res ;
			}
			else {
				return null ;
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
	
	/**
	 * fonction de test
	 */
	public String hello(String name) throws java.rmi.RemoteException {
		//return ("Hello " + name + ((DGraph)this.Registry.get("dg")).getNodeCount()) ;
		System.out.println("coucou");
		return "hello " + name;
	}

}
