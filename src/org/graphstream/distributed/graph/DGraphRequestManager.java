package org.graphstream.distributed.graph;

import java.util.HashMap;

public class DGraphRequestManager {

	HashMap<String, HashMap<String,String>> R ;
	
	/**
	 * 
	 */
	public DGraphRequestManager() {
		this.R = new HashMap<String,HashMap<String,String>>() ;
	}
	
	/**
	 * 
	 * @param key
	 * @param v
	 */
	public void addReq(String key, HashMap<String, String> v) {
		this.R.put(key, v);		
	}
	
	/**
	 * 
	 * @param key
	 */
	public void delReq(String key) {
		this.R.remove(key);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public HashMap<String,String> getRequest(String key) {
		return this.R.get(key);
	}
	
}
