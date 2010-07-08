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
		this.Id = id ;
		this.Registry = new ConcurrentHashMap<String, Object>() ;
		this.Registry.put("", this);
		this.Registry.put(EnumReg.Registry, this.Registry);		
		this.Registry.put(EnumReg.DGraphNetwork, new DGraphNetwork());
		this.Registry.put(this.Id, new DGraph(id, this.Registry));		
	}

	
	/**
	 * instantiate DGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		((DGraph)this.Registry.get(this.Id)).init(graphClass);
		this.Registry.put(EnumReg.Graph(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraph());
		this.Registry.put(EnumReg.GraphV(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraphV());
	}
	
	/**
	 * exec
	 */
	public Object exec(String functionCall, Object ... params) throws java.rmi.RemoteException  {
		String[][] f2 = DGraphParser.functionSpliter(functionCall);
		
		Object obj = this.Registry.get(f2[0][0]);
		
		if(f2.length>2) {
			for(int i = 1 ; i < (f2.length-1) ; i++) {
				obj = DynamicHelper.call2(obj, f2[i][0], true, f2[i][1]);
			}
			obj = DynamicHelper.call2(obj, f2[f2.length-1][0], true, params);
		} else {
			obj = DynamicHelper.call2(obj, f2[f2.length-1][0], false, params);
		}
		return obj ;
	}
	
	/**
	 * exec multi
	 */
	public Object[] exec(String[] functionCalls, Object[][] params)	throws RemoteException {
		// TODO Auto-generated method stub
		Object[] res  = new Object[functionCalls.length];
		for(int i = 0 ; i < res.length ; i++) {
			res[i] = exec(functionCalls[i], params[i]);
		}
		return res ;
	}
	
	/**
	 * fonction de test
	 */
	public String hello(String name) throws java.rmi.RemoteException {
		System.out.println("hello");
		return "hello " + name;
	}



	


}
