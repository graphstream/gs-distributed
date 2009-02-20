package org.miv.graphstream.distributed.req;

import org.miv.graphstream.distributed.json.JSONArray;
import org.miv.graphstream.distributed.json.JSONException;
import org.miv.graphstream.distributed.json.JSONObject;

public class GraphReqJson implements GraphReq {

	private JSONArray jsonReq ;
	private GraphReqContainer aGraphReqContainer ;

	// Constructor
	public GraphReqJson() {
		jsonReq = new JSONArray() ;
		aGraphReqContainer = new GraphReqContainer() ;
	}

	// Accessors

	// serialize
	public String serialize(GraphReqContainer value) {
		try {
			return serializeToJson(value).toString(1);
		}
		catch(JSONException e) {
			return null ;
		}
	}


	// parse
	public GraphReqContainer parse(String value) {
		return this.parseFromJson(value) ;
	}


	// utilities

	private GraphReqContainer parseFromJson(String value) {
		try {
			//this.aGraphReqsContainer.clear();
			JSONObject res = new JSONObject(value);
			this.aGraphReqContainer = new GraphReqContainer() ;
			this.aGraphReqContainer.setGraphId(res.getString("graphId")) ;
			this.aGraphReqContainer.setClassName(res.getString("className")) ;
			this.aGraphReqContainer.setMethodName(res.getString("methodeName")) ;
			JSONArray params = res.getJSONArray("parameters") ;
			for(int i = 0 ; i < params.length() ; i++) {
				this.aGraphReqContainer.addParameter(params.get(i));
			}
			return this.aGraphReqContainer ;
		}
		catch(JSONException e) {
			System.out.println("GraphReqParserJson parse error :" + e.getMessage());
			return null ;
		}
	}




	private JSONObject serializeToJson(GraphReqContainer req) {
		try {
			JSONObject reqJ = new JSONObject() ;
			JSONArray parameters = new JSONArray();
			if(req.getNbParameters() > 0) {
				for(int i = 0 ; i < req.getNbParameters() ; i++) {
					parameters.put(i, req.getParametersValue(i));
				}
			}
			reqJ.put("parameters", parameters);
			reqJ.put("methodeName", req.getMethodName()) ;
			reqJ.put("graphId", req.getGraphId());
			return reqJ ;
		}
		catch(Exception e) {
			System.out.println("serializeToJson error : " + e.getMessage()) ;
			return null ;
		}
	}

}
