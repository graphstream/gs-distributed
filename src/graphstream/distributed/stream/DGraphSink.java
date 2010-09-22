package graphstream.distributed.stream;

import graphstream.distributed.common.DGraphParser;
import graphstream.distributed.common.EnumNode;
import graphstream.distributed.graph.DGraphNetwork;
import graphstream.distributed.rmi.RMIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.stream.Sink;

public class DGraphSink implements Sink {
	
	DGraphNetwork m ;
	
	ArrayList<String> methods = new ArrayList<String>();
	ArrayList<Object[]> params = new ArrayList<Object[]>();
	
	public void setDGraphNetwork(DGraphNetwork value) {
		m = value ;
	}

	public void edgeAttributeAdded(String sourceId, long timeId, String edgeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		methods.clear();
		methods.add("getEdge");
		methods.add("addAttribute");
		
		params.clear();
		params.add(new Object[] {edgeId});
		params.add(new Object[] {attribute, value});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(edgeId).get(EnumNode.DGraphName)),"Graph", methods, params);

	}

	public void edgeAttributeChanged(String sourceId, long timeId,
			String edgeId, String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		methods.clear();
		methods.add("getEdge");
		methods.add("changeAttribute");
		
		params.clear();
		params.add(new Object[] {edgeId});
		params.add(new Object[] {attribute, oldValue, newValue});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(edgeId).get(EnumNode.DGraphName)),"Graph", methods, params);
	}

	public void edgeAttributeRemoved(String sourceId, long timeId,
			String edgeId, String attribute) {
		methods.clear();
		methods.add("getEdge");
		methods.add("removeAttribute");
		
		params.clear();
		params.add(new Object[] {edgeId});
		params.add(new Object[] {attribute});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(edgeId).get(EnumNode.DGraphName)),"Graph", methods, params);
		
	}

	public void graphAttributeAdded(String sourceId, long timeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		System.out.println("graphAttributeAdded not yet implemented");
		
	}

	public void graphAttributeChanged(String sourceId, long timeId,
			String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		System.out.println("graphAttributeChanged not yet implemented");
	}

	public void graphAttributeRemoved(String sourceId, long timeId,
			String attribute) {
		// TODO Auto-generated method stub
		System.out.println("graphAttributeRemoved not yet implemented");
	}

	public void nodeAttributeAdded(String sourceId, long timeId, String nodeId,
			String attribute, Object value) {
		// TODO Auto-generated method stub
		System.out.println("AttributeAdded");
		methods.clear();
		methods.add("getNode");
		methods.add("addAttribute");
		
		params.clear();
		params.add(new Object[] {nodeId});
		
		//Map<String, Object> m2 = new HashMap<String, Object>();
		//attributem2.put(attribute, value);
		Object[] mm = new Object[] {value};
		//ps.add(new Object[] {"cc", mm});
		params.add(new Object[] {attribute, mm});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)),"Graph", methods, params);
	}

	public void nodeAttributeChanged(String sourceId, long timeId,
			String nodeId, String attribute, Object oldValue, Object newValue) {
		// TODO Auto-generated method stub
		methods.clear();
		methods.add("getNode");
		methods.add("changeAttribute");
		
		params.clear();
		params.add(new Object[] {nodeId});
		params.add(new Object[] {attribute, newValue});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)),"Graph", methods, params);
	}

	public void nodeAttributeRemoved(String sourceId, long timeId,
			String nodeId, String attribute) {
		methods.clear();
		methods.add("getNode");
		methods.add("getAttribute");

		params.clear();
		params.add(new Object[] {nodeId});
		params.add(new Object[] {attribute});
		
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)),"Graph", methods, params);		
	}

	public void edgeAdded(String sourceId, long timeId, String edgeId,
			String fromNodeId, String toNodeId, boolean directed) {
		RMIHelper.RMICall(m.getDGraph(DGraphParser.edge(edgeId).get(EnumNode.DGraphName)), "DGraph", "addEdge", new String[] {edgeId, fromNodeId, toNodeId});
	}

	public void edgeRemoved(String sourceId, long timeId, String edgeId) {
		RMIHelper.RMICall(m.getDGraph(DGraphParser.edge(edgeId).get(EnumNode.DGraphName)), "DGraph", "removeEdge", new String[] {edgeId});
	}

	public void graphCleared(String sourceId, long timeId) {
		System.out.println("graphCleared (not yet implemented)");
	}

	public void nodeAdded(String sourceId, long timeId, String nodeId) {
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)), "DGraph", "addNode", new String[] {nodeId});
	}

	public void nodeRemoved(String sourceId, long timeId, String nodeId) {
		RMIHelper.RMICall(m.getDGraph(DGraphParser.node(nodeId).get(EnumNode.DGraphName)), "DGraph", "removeNode", new String[] {nodeId});
	}

	public void stepBegins(String sourceId, long timeId, double step) {
		System.out.println("stepBegins not yet implemented");
	}
	
	
}
