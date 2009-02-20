package org.miv.graphstream.distributed.req;

public class GraphGenericReq {

	String GraphId;
	Object ObjInst ;
	String MethodName ;
	Object[] Parameters ;

	/**
	 *
	 * @param aGraphId
	 * @param anObjInst
	 * @param aMethodeName
	 * @param theParameters
	 */
	public GraphGenericReq(String aGraphId, Object anObjInst, String aMethodeName, Object[] theParameters) {
		this.GraphId = aGraphId ;
		this.ObjInst = anObjInst ;
		this.MethodName = aMethodeName ;
		this.Parameters = theParameters ;
	}

	public GraphGenericReq(String id, String aMethodeName, Object[] theParameters) {
		this.GraphId = id ;
		this.ObjInst = id ;
		this.MethodName = aMethodeName ;
		this.Parameters = theParameters ;
	}

	public GraphGenericReq(String aMethodeName, Object[] theParameters) {
		this.MethodName = aMethodeName ;
		this.Parameters = theParameters ;
	}

	public GraphGenericReq(String aMethodeName) {
		this.MethodName = aMethodeName ;
		this.Parameters = null ;
	}

	public String getGraphId() {
		return this.GraphId ;
	}

	public void setGraphId(String anId) {
		this.GraphId = anId ;
	}

	public Object getObjInst() {
		return this.ObjInst ;
	}

	public void setObjInst(Object anObject) {
		this.ObjInst = anObject ;
	}

	public String getMethodName() {
		return this.MethodName ;
	}

	public Object[] getParameters() {
		return this.Parameters ;
	}



}
