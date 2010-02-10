package org.graphstream.distributed.rmi;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.graphstream.distributed.common.DGraphUri;
import org.graphstream.distributed.graph.DGraphCore;
import org.graphstream.distributed.graph.DGraphManager;

public class RMIDGraph extends UnicastRemoteObject implements RMIDGraphAdapter {

	/*
	 * Variables
	 */
	private String Id ;
	
	//private DGraphCoreAdapter DGraphCore ;
	private ConcurrentHashMap<String, Object> Registry ;
	private HashMap<String, RMIDGraphAdapter> DGraphClients ;
	private HashMap<String, DGraphUri> DGraphUriIndex ;
	
	private static final long serialVersionUID = 0001234543456;

	/*
	 * Constructor
	 */
	
	public RMIDGraph() throws java.rmi.RemoteException {	
		//registration
		this.Registry = new ConcurrentHashMap<String, Object>() ;
		this.Registry.put("dg", new DGraphCore());
		this.Registry.put("manager", new DGraphManager("messenger"));
	}

	
	/*
	 * instantiate a distGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		/*GraphFactory graphFactory = new GraphFactory() ;
		Graph g = graphFactory.newInstance("", graphClass) ;
		g.setAutoCreate(true);
		g.setStrict(false);
		this.Registry.put("g", g);*/
		((DGraphCore)this.Registry.get("dg")).init(graphClass);
	}
	
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void bind(String id) throws RemoteException {
		try	{
			this.Id = id ;
			System.out.println("binding begin");
			Naming.rebind( String.format( "//localhost/%s", this.Id ), this );
			System.out.println("binding done");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * 
	 */
	public void clear() throws java.rmi.RemoteException {
		//this.DGraphCore = null ;
		//this.DGraphObjects = null ;
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
			this.Registry.put(u.getDGraphId(), (RMIDGraphAdapter)Naming.lookup("rmi://"+u.getHost()+"/"+u.getDGraphId()));
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


	/*
	 * Generic request
	 */
	public Object exec(String objectId, String methode, Object[] params) throws java.rmi.RemoteException {
		//this.
		//return null ;
		return dynamicCall(this.Registry.get(objectId), methode, params);
	}

	/*
	 * Generic request multi
	 */
	public Object[] exec(String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException {
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
			//if(Serializable.class.isAssignableFrom(res.getClass())) {
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
	 * 
	 */
	public String hello(String name) throws java.rmi.RemoteException {
		return ("Hello " + name + ((DGraphCore)this.Registry.get("dg")).getNodeCount()) ;
	}

}
