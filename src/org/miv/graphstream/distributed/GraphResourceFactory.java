package org.miv.graphstream.distributed;

public class GraphResourceFactory {

	public GraphResourceFactory() {

	}

	public GraphResource newInstance(String className, String aGraphId, String aProtocol, String aHost, String aPort, String aGraphClass, boolean aDisplay) {
		try {
			GraphResource res = (GraphResource)Class.forName(className).newInstance() ;
			res.setDisplay(aDisplay);
			res.setGraphClass(aGraphClass);
			res.setGraphId(aGraphId);
			res.setHost(aHost);
			res.setPort(aPort);
			res.setProtocole(aProtocol);
			return res ;
		}
		catch(InstantiationException e) {
			System.out.println("GraphFactory newInstance InstantiationException : " + e.getMessage()) ;
			return null;
		}
		catch(ClassNotFoundException e) {
			System.out.println("GraphFactory newInstance ClassNotFoundException : " + e.getMessage()) ;
			return null;
		}
		catch(IllegalAccessException e) {
			System.out.println("GraphFactory newInstance IllegalAccessException : " + e.getMessage()) ;
			return null;
		}
	}

}
