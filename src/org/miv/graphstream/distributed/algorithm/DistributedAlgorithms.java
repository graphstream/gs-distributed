package org.miv.graphstream.distributed.algorithm;

import org.miv.graphstream.distributed.req.GraphAsyncReq;
import org.miv.graphstream.distributed.req.GraphGenericReq;
import org.miv.graphstream.distributed.req.GraphReqContainer;
import org.miv.graphstream.distributed.req.GraphRespContainer;
import org.miv.graphstream.distributed.rmi.GraphRegistry;

public class DistributedAlgorithms implements DistributedAlgorithm {

	GraphRegistry Registry ;
	GraphReqContainer Req ;
	GraphRespContainer Res ;

	public DistributedAlgorithms() {
		this.Req = new GraphReqContainer() ;
	}

	/**
	 * Set the graph the algorithm will operate upon.
	 * @param graph The graph to use.
	 */
	public void setGraphRegistry( GraphRegistry aRegistry ) {
		this.Registry = aRegistry ;
	}

	public GraphRegistry getGraphRegistry() {
		return this.Registry ;
	}

	public int getNodeCount() {
		this.Res = new GraphRespContainer() ;
		GraphAsyncReq g = new GraphAsyncReq(this.Registry, this.Res);
		g.exec(new GraphGenericReq("getNodeCount"));
		int result = 0 ;
		for(int i = 0 ; i < this.Res.size() ; i++) {
			result += ((Integer)this.Res.getRes(i)).intValue();
		}
		return result ;
	 }

	public int getEdgeCount() {
		this.Res = new GraphRespContainer() ;
		GraphAsyncReq g = new GraphAsyncReq(this.Registry, this.Res);
		g.exec(new GraphGenericReq("getEdgeCount"));
		int result = 0 ;
		int tmp1 = 0 ;
		int tmp2 = 0 ;
		for(int i = 0 ; i < this.Res.size() ; i++) {
			tmp1 += ((int[])(this.Res.getRes(i)))[0];
			tmp2 +=((int[])(this.Res.getRes(i)))[1];
		}
		result=tmp1+(int)(tmp2/2);

		return (result) ;
	 }



}
