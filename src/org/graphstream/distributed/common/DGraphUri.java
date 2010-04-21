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
import java.util.Map;


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
	HashMap<String, String> data ;

	// Constructor

	public DGraphUri(String uri) {
		this.data.put("uri",uri);
		uriParser(uri);
	}

	// rmi:<host>:<port>/<id>:<graphClass>
	private void uriParser(String uri) {
		this.data.put("protocole", uri.split(":")[0]) ;
		DGraphParser.uri(uri);
		this.data.put("port","1099");
		if(uri.split(":").length > 3) { // avec port specifie
			this.data.put("port",uri.split(":")[2].split("/")[0]);
			this.data.put("host",uri.split(":")[1]);
		}
		else { // sans port specif
			this.data.put("host",uri.split(":")[1].split("/")[0]) ;
		}
		this.data.put("DGraphName", uri.split("/")[1].split(":")[0]);
		this.data.put("DGraphClass", uri.split("/")[1].split(":")[1]);
	}


	// Accessors

	public String getElement(String key) {
		return this.data.get(key) ;
	}

	public Map<String, String> getMap() {
		return this.data ;
	}

}
