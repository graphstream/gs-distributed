/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.graphstream.distributed.common;

import java.util.HashMap;


/**
 * Base elements of a graph.
 *
 * <p>
 *
 * </p>
 *
 * @author Julien Baudry
 * @author Antoine Dutot
 * @author Yoann Pigne
 * @since 20020709
 */

public class DGraphUri {

	// Fields
	String Uri ;
	
	String DGraphId ;
	String Protocole ;
	String Host ;
	String Port ;
	HashMap<String,String> Params ;
	String DGraphClass ;
	


	// Constructor

	public DGraphUri(String uri) {
		init();
		this.Uri = uri ;
		uriParser(uri);
	}

	private void init() {
		this.Params = new HashMap<String,String>() ;
	}

	// rmi:<host>:<port>/<id>:<graphClass>
	private void uriParser(String uri) {
		this.Protocole = uri.split(":")[0] ;
		this.Port = "1099";
		if(uri.split(":").length > 3) { // avec port specifie
			this.Port = uri.split(":")[2].split("/")[0] ;
			this.Host = uri.split(":")[1] ;
		}
		else { // sans port specif
			this.Host = uri.split(":")[1].split("/")[0] ;
		}
		this.DGraphId = uri.split("/")[1].split(":")[0];
		this.DGraphClass = uri.split("/")[1].split(":")[1];
	}


	// Modifiers


	// set the id of the graph
	public void setDGraphId(String value) {
		this.DGraphId = value ;
	}

	//
	public void setHost(String value) {
		this.Host = value ;
	}

	//
	public void setProtocole(String value) {
		this.Protocole = value ;
	}

	//
	public void setPort(String value) {
		this.Port = value ;
	}

	//
	public void setDGraphClass(String value) {
		this.DGraphClass = value ;
	}

	//
	public void addParameter(String key, String value) {
		this.Params.put(key, value);
	}


	// Accessors


	//
	public String getHost() {
		return this.Host ;
	}

	//
	public String getDGraphId() {
		return this.DGraphId ;
	}

	//
	public String getProtocole() {
		return this.Protocole ;
	}

	//
	public String getPort() {
		return this.Port ;
	}

	//
	public String getDGraphClass() {
		return this.DGraphClass ;
	}

	// Method getParameter
	public String getParameter(String key) {
		return this.Params.get(key);
	}

	public String getUri() {
		return this.Uri ;
	}

}
