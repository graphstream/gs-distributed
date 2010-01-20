package org.graphstream.distributed.graph;


public class DistGraph {

	/*
	 * Variables
	 */
	private DGraphCore DistGraphCore ;
	private DGraphClientOld DistGraphClient ;
	private DGraphObjects DistGraphObjects ;

	/**
	 *
	 * @param graphClass
	 */
	public DistGraph(String graphClass) {
		this.DistGraphClient = new DGraphClientOld();
		this.DistGraphCore = new DGraphCoreImpl(graphClass);
	}

	/**
	 * @return
	 */
	public DGraphObjects getObjects() {
		return this.DistGraphObjects;
	}

	/**
	 * @return
	 */
	public DGraphClientOld getClient() {
		return this.DistGraphClient ;
	}

	/**
	 *
	 * @return
	 */
	public DGraphCore getGraph() {
		return this.DistGraphCore ;
	}



}
