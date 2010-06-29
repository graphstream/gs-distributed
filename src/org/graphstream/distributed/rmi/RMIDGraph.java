package org.graphstream.distributed.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.DynamicHelper;
import org.graphstream.distributed.common.EnumReg;
import org.graphstream.distributed.graph.DGraph;
import org.graphstream.distributed.graph.DGraphNetwork;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

public class RMIDGraph extends UnicastRemoteObject implements RMIDGraphAdapter {

	/**
	 * Variables
	 */
	private String Id ;
	
	private ConcurrentHashMap<String, Object> Registry ;
	
	private static final long serialVersionUID = 0001234543456;

	/**
	 * Constructor
	 */
	public RMIDGraph(String id) throws java.rmi.RemoteException {
		this.Registry = new ConcurrentHashMap<String, Object>() ;
		this.Registry.put("", this);
		this.Registry.put(EnumReg.Registry, this.Registry);		
		this.Registry.put(EnumReg.DGraphNetwork, new DGraphNetwork());
		this.Registry.put(EnumReg.DGraph, new DGraph(id, this.Registry));
		//System.out.println("dgraph : " + ((DGraph)this.Registry.get(EnumReg.DGraph)).getGraph());
		
	}

	
	/**
	 * instantiate DGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		((DGraph)this.Registry.get(EnumReg.DGraph)).init(graphClass);
		this.Registry.put(EnumReg.GraphInDGraph, ((DGraph)this.Registry.get(EnumReg.DGraph)).getGraph());
		this.Registry.put(EnumReg.GraphVInDGraph, ((DGraph)this.Registry.get(EnumReg.DGraph)).getGraphV());
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
	 * Clear the registry
	 */
	public void clear() throws java.rmi.RemoteException {
		this.Registry.clear();
	}

	/**
	 * Generic request
	 */
	public Object exec(String requestId, String objectId, String methode, Object[] params) throws java.rmi.RemoteException {
		return exec(objectId, methode, params);
	}
	
	public Object exec(String objectId, String methode, Object[] params) throws java.rmi.RemoteException {
		return dynamicCall(this.Registry.get(objectId), methode, params);
	}

	/**
	 * Generic request multi
	 */
	public Object[] exec(String[] requestId, String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException {
		return exec(objectIds, methods, params);
	}
	
	public Object[] exec(String[] objectIds, String[] methods, Object[][] params) throws java.rmi.RemoteException {
		Object[] res = new Object[objectIds.length];
		for(int i = 0 ; i < objectIds.length ; i++ ) {
			res[i] = dynamicCall(this.Registry.get(objectIds[i]), methods[i], params[i]);
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
				return false ;
			}
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return false ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return false ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception : " + e.getMessage() + e.getCause());
			return null ;
		}
	}
	
	public Object exec(String functionCall, Object ... params) throws java.rmi.RemoteException  {
		//DGraphParser.function_simple("function");
		//Object obj = DynamicHelper.call(DGraphParser.functionCall((Graph)this.Registry.get(EnumReg.GraphInDGraph), functionCall), DGraphParser.functionLast(functionCall), params);
		String f = DGraphParser.functionLast(functionCall) ;
		String[][] f2 = DGraphParser.functionSpliter(functionCall);
		
		//n.addAttributes(attributes)
		//Object obj = DynamicHelper.call(this.Registry.get(EnumReg.DGraph), f, params);
		//Object obj = DynamicHelper.call(EnumReg.GraphInDGraph, f2[f2.length-1][0], params);
		//Object obj = DynamicHelper.call(EnumReg.GraphInDGraph, "addAttribute", new Object[] {"aa", "qq"});
		//Object obj = DynamicHelper.call2(n, "addAttribute", "n", new Object[] {"v"});
		//Object obj = DynamicHelper.call2(n, "addAttribute", new Object[] {"n", new Object[] {"v"}});
		//g1.addNode
		Object obj = this.Registry.get(f2[0][0]);
		//obj = DynamicHelper.call2(obj, f2[1][0], params);	
		
		if(f2.length>2) {
			for(int i = 1 ; i < (f2.length-1) ; i++) {
				System.out.println("boucle : " + i + obj.getClass() + " method : " + f2[i][0] + " [] " + f2[i][1]);
				obj = DynamicHelper.call2(obj, f2[i][0], f2[i][1]);
			}
			//obj = DynamicHelper.call2(obj, f2[f2.length-1][0], params);
		} else {
			System.out.println("f2[i][1] : " + obj + "___"+ f2[f2.length-1][0]);
			//obj = DynamicHelper.call2(obj, f2[f2.length-1][0], params);
		}
		//System.out.println("f2[i][1] : " + obj + "___"+ f2[f2.length-1][0]);
		//obj = DynamicHelper.call2(obj, f2[f2.length-1][0], new Object[] {"n", new Object[] {"v"}});
		
		
		System.out.println("GRAAAAAAPH : " + (Graph)this.Registry.get(EnumReg.GraphInDGraph));
		
		//Graph g = (Graph)obj ;
		return 0 ;
	}
	
	/**
	 * fonction de test
	 */
	public String hello(String name) throws java.rmi.RemoteException {
		System.out.println("hello");
		return "hello " + name;
	}

}
