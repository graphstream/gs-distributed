package org.miv.graphstream.distributed.req;

import java.util.ArrayList;
import java.util.List;

public class GraphRespContainer {

	List<Object> Res ;
	//HashTable Res2 ;

	public GraphRespContainer() {
		this.Res = new ArrayList<Object>();
	}

	public void addRes(Object anObject) {
		this.Res.add(anObject);
	}

	public int size() {
		return this.Res.size() ;
	}

	public Object getRes(int index) {
		return this.Res.get(index);
	}

	public void setRes(int index, Object anObject) {
		//this.Res2.
	}

}
