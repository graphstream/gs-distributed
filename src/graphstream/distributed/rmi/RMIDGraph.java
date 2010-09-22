package graphstream.distributed.rmi;

import graphstream.distributed.common.DynamicHelper;
import graphstream.distributed.common.EnumReg;
import graphstream.distributed.graph.DGraph;
import graphstream.distributed.graph.DGraphNetwork;
import graphstream.distributed.graph.DGraphRequestManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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
		this.Registry.put(EnumReg.DGraphRequestManager, new DGraphRequestManager());
	}

	
	/**
	 * instantiate DGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		//this.Registry.put(this.Id, new DGraph(this.Id, this.Registry));
		this.Registry.put(EnumReg.DGraph, new DGraph(this.Id, this.Registry));
		//((DGraph)this.Registry.get(this.Id)).init(graphClass);
		((DGraph)this.Registry.get(EnumReg.DGraph)).init(graphClass);
		//this.Registry.put(EnumReg.Graph(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraph());
		//this.Registry.put(EnumReg.GraphV(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraphV());
		this.Registry.put(EnumReg.Graph, ((DGraph)this.Registry.get(EnumReg.DGraph)).getGraph());
		this.Registry.put(EnumReg.GraphV, ((DGraph)this.Registry.get(EnumReg.DGraph)).getGraphV());
	}
	
	/*public void init2(String graphClass, String[] params) throws java.rmi.RemoteException {
		this.Registry.put(this.Id, new DGraph2(this.Id, this.Registry));		
		((DGraph2)this.Registry.get(this.Id)).init(graphClass);
		this.Registry.put(EnumReg.Graph(this.Id), ((DGraph2)this.Registry.get(this.Id)).getGraph());
		this.Registry.put(EnumReg.GraphV(this.Id), ((DGraph2)this.Registry.get(this.Id)).getGraphV());
	}*/

	/**
	 * 
	 */
	public Object exec(String objectName, String methodName, Object[] params)
	throws RemoteException {
		return DynamicHelper.call2(this.Registry.get(objectName), methodName, true, params);
	}

	/**
	 * 
	 * @param objectName
	 * @param methodName
	 * @param params
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<Object> exec(String objectName, String methodName, Object[][] params)
			throws RemoteException {
		ArrayList<Object> res = new ArrayList<Object>();
		for(Object[] o : params) {
			res.add(DynamicHelper.call2(this.Registry.get(objectName), methodName, true, o));
		}
		return res ;
	}

	
	/**
	 * 
	 */
	public ArrayList<Object> exec(String objectName, String methodName,
			ArrayList<Object[]> params) throws RemoteException {
		ArrayList<Object> res = new ArrayList<Object>();
		for(Object[] o : params) {
			res.add(DynamicHelper.call2(this.Registry.get(objectName), methodName, true, o));
		}
		return res ;
	}

	
	/**
	 * 
	 */
	public ArrayList<Object> exec(String requestId, ArrayList<Object[]> params)
			throws RemoteException {
		ArrayList<Object> res = new ArrayList<Object>();
		HashMap<String, String> v = ((DGraphRequestManager)this.Registry.get(EnumReg.DGraphRequestManager)).getRequest(requestId);
		
		for(Object[] o : params) {
			res.add(DynamicHelper.call2(this.Registry.get(v.get(EnumReg.ObjectName)), v.get(EnumReg.MethodName), true, o));
		}
		return res;
	}


	/**
	 * 
	 */
	public Object exec(String ObjectName, String MethodName)
			throws RemoteException {
		return DynamicHelper.call2(this.Registry.get(ObjectName), MethodName, true, (Object[])null);
	}
	
	/**
	 * Request imbriquée
	 */
	public Object exec(String objectName, ArrayList<String> methodNames,
			ArrayList<Object[]> params) throws RemoteException {
		
		Object res = this.Registry.get(objectName);		
		for(int i = 0 ; i < methodNames.size() ; i++) {
			res = DynamicHelper.call2(res, methodNames.get(i), true, params.get(i));
		}
		return res ;
	}
	
	/**
	 * fonction de test
	 */
	/*public String hello(String name) throws java.rmi.RemoteException {
		System.out.println("hello");
		return "hello " + name;
	}*/



	


}
