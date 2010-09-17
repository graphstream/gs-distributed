package graphstream.distributed.rmi;

import graphstream.distributed.common.DynamicHelper;
import graphstream.distributed.common.EnumReg;
import graphstream.distributed.graph.DGraph;
import graphstream.distributed.graph.DGraphNetwork;
import graphstream.distributed.graph.DGraphRequestManager;
import graphstream.distributed.test.DGraph2;

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
	private DGraphRequestManager R ;
	
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
		this.R = new DGraphRequestManager();
		//this.Registry.put(this.Id, new DGraph(id, this.Registry));		
	}

	
	/**
	 * instantiate DGraph
	 */
	public void init(String graphClass, String[] params) throws java.rmi.RemoteException {
		this.Registry.put(this.Id, new DGraph(this.Id, this.Registry));		
		((DGraph)this.Registry.get(this.Id)).init(graphClass);
		this.Registry.put(EnumReg.Graph(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraph());
		this.Registry.put(EnumReg.GraphV(this.Id), ((DGraph)this.Registry.get(this.Id)).getGraphV());
	}
	
	public void init2(String graphClass, String[] params) throws java.rmi.RemoteException {
		this.Registry.put(this.Id, new DGraph2(this.Id, this.Registry));		
		((DGraph2)this.Registry.get(this.Id)).init(graphClass);
		this.Registry.put(EnumReg.Graph(this.Id), ((DGraph2)this.Registry.get(this.Id)).getGraph());
		this.Registry.put(EnumReg.GraphV(this.Id), ((DGraph2)this.Registry.get(this.Id)).getGraphV());
	}
	
	/**
	 * exec
	 */
	/*public Object exec(String functionCall, Object ... params) throws java.rmi.RemoteException  {
		String[][] f2 = DGraphParser.functionSpliter(functionCall);
		Object obj = this.Registry.get(f2[0][0]);
		if(f2.length>2) {
			for(int i = 1 ; i < (f2.length-1) ; i++) {
				obj = DynamicHelper.call2(obj, f2[i][0], true, f2[i][1]);
			}
			obj = DynamicHelper.call2(obj, f2[f2.length-1][0], true, params);
		} else {
			obj = DynamicHelper.call2(obj, f2[f2.length-1][0], true, params);
		}
		return obj ;
	}*/
	
	/**
	 * exec multi
	 */
	/*public Object[] exec(String[] functionCalls, Object[][] params)	throws RemoteException {
		// TODO Auto-generated method stub
		Object[] res  = new Object[functionCalls.length];
		for(int i = 0 ; i < res.length ; i++) {
			res[i] = exec(functionCalls[i], params[i]);
		}
		return res ;
	}*/


	public Object exec(String objectName, String methodName, Object[] params)
	throws RemoteException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		ArrayList<Object> res = new ArrayList<Object>();
		HashMap<String, String> v = ((DGraphRequestManager)this.Registry.get(EnumReg.DGraphRequestManager)).getRequest(requestId);
		for(Object[] o : params) {
			res.add(DynamicHelper.call2(this.Registry.get(v.get("objectName")), v.get("methodName"), true, o));
		}
		return res;
	}


	public Object exec(String objectName, String methodName)
			throws RemoteException {
		// TODO Auto-generated method stub
		return DynamicHelper.call2(this.Registry.get(objectName), methodName, true, (Object[])null);
	}
	
	
	/**
	 * fonction de test
	 */
	/*public String hello(String name) throws java.rmi.RemoteException {
		System.out.println("hello");
		return "hello " + name;
	}*/



	


}
