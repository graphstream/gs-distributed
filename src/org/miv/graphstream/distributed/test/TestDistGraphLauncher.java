package org.miv.graphstream.distributed.test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class TestDistGraphLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		binding(args[0], args[1]);
	}
	
	public static void binding(String host, String name) {
		try {
			TestDistGraphServer s = new TestDistGraphServerImpl();
			System.out.println("l'objet RMI DistGraphServerTest est construit");
			Naming.rebind("rmi://"+host+"/"+name, s);
			System.out.println("l'objet RMI DistGraphServerTest est inscrit au service de nommage");
		} catch (RemoteException exp) {
			System.out.println("Pb de RemoteException (DistGraphLauncherTest) : " + exp);
		} catch (MalformedURLException exp) {
			System.out.println("Pb de NotBoundException (DistGraphLauncherTest) : " + exp);
		}
	}

}
