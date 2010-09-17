package graphstream.distributed.graph;

import java.util.HashMap;

public class DGraphRequestManager {

	HashMap<String, HashMap<String,String>> R ;
	
	
	public DGraphRequestManager() {
		this.R = new HashMap<String,HashMap<String,String>>() ;
	}
	
	public void addReq(String key, HashMap<String, String> v) {
		this.R.put(key, v);		
	}
	
	public void delReq(String key) {
		this.R.remove(key);
	}
	
	public HashMap<String,String> getRequest(String key) {
		return this.R.get(key);
	}
	
}
