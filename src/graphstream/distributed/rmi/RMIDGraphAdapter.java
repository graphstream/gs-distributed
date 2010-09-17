package graphstream.distributed.rmi;

import java.rmi.Remote;
import java.util.ArrayList;

public interface RMIDGraphAdapter extends Remote {
	
	/*
	 * exec
	 */
	
	/**
	 * 
	 */
	public Object exec(String objectName, String methodName, Object[] params) throws java.rmi.RemoteException ;
	
	/**
	 * 
	 * @param objectName
	 * @param methodName
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public Object exec(String objectName, String methodName) throws java.rmi.RemoteException ;
	
	/**
	 * 
	 * @param objectName
	 * @param methodName
	 * @param params
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public ArrayList<Object> exec(String objectName, String methodName, ArrayList<Object[]> params) throws java.rmi.RemoteException ;

	/**
	 * 
	 * @param requestId
	 * @param params
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public ArrayList<Object> exec(String requestId, ArrayList<Object[]> params) throws java.rmi.RemoteException ; 
	
}
