package org.miv.graphstream.distributed.req;


public class GraphReqFactory {

	public GraphReqFactory() {
	}

	public GraphReq newInstance(String graphClass) {
		try {
			GraphReq res = (GraphReq)Class.forName(graphClass).newInstance();
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
