package org.graphstream.distributed.graph;

import java.util.HashMap;

public class DistGraphObjects {


	/**
	 * Variables
	 */
	HashMap<String, Object> distGraphObjectsList ;


	/**
	 * Constructor
	 */
	public DistGraphObjects() {
		distGraphObjectsList = new HashMap<String, Object>();

	}

	/**
	 *
	 * @param id
	 * @param anObject
	 */
	public void addObject(String id, Object anObject) {
		this.distGraphObjectsList.put(id, anObject);
	}

	/**
	 *
	 * @param id
	 */
	public void delObject(String id) {
		this.distGraphObjectsList.remove(id);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Object get(String id) {
		return this.distGraphObjectsList.get(id);
	}




}
