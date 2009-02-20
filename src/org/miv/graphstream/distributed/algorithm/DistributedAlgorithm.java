package org.miv.graphstream.distributed.algorithm;

import org.miv.graphstream.distributed.rmi.GraphRegistry;

	/**
	 * Base for algorithms operating on a graph.
	 *
	 * @author Antoine Dutot
	 * @author Yoann Pigné
	 * @since 2007
	 */
	public interface DistributedAlgorithm	{

		/**
		 * The graph the algorithms operates upon.
		 * @return A graph.
		 */
		GraphRegistry getGraphRegistry();

		/**
		 * Set the graph the algorithm will operate upon.
		 * @param graph The graph to use.
		 */
		public void setGraphRegistry( GraphRegistry aGraphRegistry );


	}
