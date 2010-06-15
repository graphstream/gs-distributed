package org.graphstream.distributed.stream;

import java.rmi.RemoteException;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumNode;
import org.graphstream.distributed.common.EnumReg;
import org.graphstream.distributed.graph.DGraphNetwork;
import org.graphstream.stream.Sink;

public class DGraphSink implements Sink {
	
	DGraphNetwork m ;
	
	public void setDGraphNetwork(DGraphNetwork value) {
		m = value ;
	}

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
		System.out.println("nodeAdded " + sourceId + " " + timeId);
		try {
			m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)).exec(EnumReg.DGraph, "addNode", new String[] {nodeId});
		}
		catch(RemoteException e) {
			System.out.println("Error nodeAdded : " + e.getMessage());
		}
	}

	public void nodeRemoved(String sourceId, long timeId, String nodeId) {
		// TODO Auto-generated method stub
		System.out.println("nodeRemoved");
		rmiCall(DGraphParser.node(nodeId).get(EnumNode.DGraphName), "removeNode", new String[] {nodeId});
	}

	public void stepBegins(String sourceId, long timeId, double step) {
		// TODO Auto-generated method stub
		System.out.println("stepBegins");
	}

	private void rmiCall(String DGraphName, String method, Object[] params) {
		try {
			m.getDGraph(DGraphName).exec(EnumReg.DGraph, method, params);
		}
		catch(RemoteException e) {
			System.out.println("Error rmiCall : " + e.getMessage() + "parametres " + method + " " + DGraphName);
		}
	}
	
	
}
