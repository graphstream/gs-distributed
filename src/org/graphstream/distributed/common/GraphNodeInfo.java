package org.graphstream.distributed.common;

public class GraphNodeInfo {

	private String tag ;
	private String elementId ;

	private String sep ;


	// Constructors

	public GraphNodeInfo() {
		this.sep = "/" ;
	}

	public GraphNodeInfo(String aSep) {
		this.sep = aSep ;
	}


	// Modifier

	public void newNode(String aTag, String anElementId) {
		this.tag = aTag ;
		this.elementId = anElementId ;
	}


	// Accessors

	public String getTag() {
		return this.tag ;
	}

	public String getElementId() {
		return this.elementId ;
	}

	public String getNodeId() {
		return (this.tag+this.sep+this.elementId) ;
	}


}
