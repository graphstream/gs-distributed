package graphstream.distributed.rmi;

import java.rmi.Remote;

import org.json.JSONObject;

public interface RMIDGraphAdapter extends Remote {
	
	/*
	 * exec
	 */
	
	//exec(method, params[])
	public Object exec(String functionCall, Object ... params) throws java.rmi.RemoteException ;
	
	//exec(methods[], params[][])
	public Object[] exec(String[] functionCalls, Object[][] params) throws java.rmi.RemoteException ;
	
	//exec(method, params[][])
	public Object[] exec(String functionCalls, Object[][] params) throws java.rmi.RemoteException ;

	//execJson
	public String execJson(String jsonRequest) throws java.rmi.RemoteException ;

	public JSONObject execJson(JSONObject jsonRequest) throws java.rmi.RemoteException ;
	
}
