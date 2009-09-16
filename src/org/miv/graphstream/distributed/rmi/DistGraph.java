package org.miv.graphstream.distributed.rmi;

public class DistGraph {

	private DistGraphCore DistGraphCore ;
	private DistGraphClient DistGraphClient ;
	private DistGraphObjects DistGraphObjects ;

	/**
	 *
	 * @param graphClass
	 */
	public DistGraph(String graphClass) {
		this.DistGraphClient = new DistGraphClient();
		this.DistGraphCore = new DistGraphCoreImpl(graphClass);
	}

	/**
	 *
	 * @return
	 */
	public DistGraphObjects getObjects() {
		return this.DistGraphObjects;
	}

	/**
	 *
	 * @return
	 */
	public DistGraphClient getClient() {
		return this.DistGraphClient ;
	}

	/**
	 *
	 * @return
	 */
	public DistGraphCore getGraph() {
		return this.DistGraphCore ;
	}




}
