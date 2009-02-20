package org.miv.graphstream.distributed.req;




public interface GraphReq {

	public GraphReqContainer parse(String value) ;

	public String serialize(GraphReqContainer value) ;

}
