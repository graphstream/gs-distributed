package org.miv.graphstream.distributed.req;


public class GraphRemoteObjectFactory {

	/***
	 * A  simple way to create dynamically object through rmi
	 */

	public GraphRemoteObjectFactory() {
	}

	public Object newInstance(String className) {
		try {
			return Class.forName(className).newInstance();
		}
		catch(InstantiationException e) {
			System.out.println("GraphRemoteObjectFactory newInstance InstantiationException : " + e.getMessage()) ;
			return null;
		}
		catch(ClassNotFoundException e) {
			System.out.println("GraphRemoteObjectFactory newInstance ClassNotFoundException : " + e.getMessage()) ;
			return null;
		}
		catch(IllegalAccessException e) {
			System.out.println("GraphRemoteObjectFactory newInstance IllegalAccessException : " + e.getMessage()) ;
			return null;
		}

	}

}
