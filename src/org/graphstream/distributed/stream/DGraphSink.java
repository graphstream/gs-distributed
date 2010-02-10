package org.graphstream.distributed.stream;

import org.graphstream.distributed.graph.DGraphManager;
import org.graphstream.stream.Sink;

public class DGraphSink implements Sink {
	
	DGraphManager m ;

	public void edgeAttributeAdded(String sourceId, long timeId, String edgeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		System.out.println("edgeAttributeAdded");
		//rules
	}

	public void edgeAttributeChanged(String sourceId, long timeId,
			String edgeId, String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		System.out.println("edgeAttributeAdded");
	}

	public void edgeAttributeRemoved(String sourceId, long timeId,
			String edgeId, String attribute) {
		// TODO Auto-generated method stub
		
	}

	public void graphAttributeAdded(String sourceId, long timeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		
	}

	public void graphAttributeChanged(String sourceId, long timeId,
			String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	public void graphAttributeRemoved(String sourceId, long timeId,
			String attribute) {
		// TODO Auto-generated method stub
		
	}

	public void nodeAttributeAdded(String sourceId, long timeId, String nodeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		
	}

	public void nodeAttributeChanged(String sourceId, long timeId,
			String nodeId, String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	public void nodeAttributeRemoved(String sourceId, long timeId,
			String nodeId, String attribute) {
		// TODO Auto-generated method stub
		
	}

	public void edgeAdded(String sourceId, long timeId, String edgeId,
			String fromNodeId, String toNodeId, boolean directed) {
		// TODO Auto-generated method stub
		System.out.println("edgeAdded");
	}

	public void edgeRemoved(String sourceId, long timeId, String edgeId) {
		// TODO Auto-generated method stub
		
	}

	public void graphCleared(String sourceId, long timeId) {
		// TODO Auto-generated method stub
		System.out.println("graphCleared");
	}

	public void nodeAdded(String sourceId, long timeId, String nodeId) {
		// TODO Auto-generated method stub
		System.out.println("nodeAdded");
		//c.getDistGraphServer(sourceId).
	}

	public void nodeRemoved(String sourceId, long timeId, String nodeId) {
		// TODO Auto-generated method stub
		System.out.println("nodeRemoved");
	}

	public void stepBegins(String sourceId, long timeId, double step) {
		// TODO Auto-generated method stub
		System.out.println("stepBegins");
	}

	
	
}
