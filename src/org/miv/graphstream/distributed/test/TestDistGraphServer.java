package org.miv.graphstream.distributed.test;

import java.rmi.Remote;

public interface TestDistGraphServer extends Remote {
	
	public String hello(String mess) throws java.rmi.RemoteException ;

}
