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
 * @author Yoann Pign�
 * @since 20020709
 */

public class GraphResource {

	// Fields

	String GraphId ;
	String Protocole ;
	String Host ;
	String Port ;
	HashMap<String,String> Params = new HashMap<String,String>() ;
	String GraphClass ;
	boolean Display ;
	String Uri ;


	// Constructor

	public GraphResource(String uri) {
		this.Uri = uri ;
		uriParser(uri);
	}

	public GraphResource(String aGraphId, String aProtocol, String aHost, String aPort, String aGraphClass, boolean aDisplay) {
		this.GraphId = aGraphId ;
		this.Host = aHost ;
		this.Protocole = aProtocol ;
		this.Port = aPort ;
		this.GraphClass = aGraphClass ;
		this.Display = aDisplay ;
	}


	// rmi:<host>:<port>/<id>:<graphClass>
	private void uriParser(String uri) {
		this.Protocole = uri.split(":")[0] ;
		this.Port = "1099";
		if(uri.split(":").length > 3) { // avec port specifie
			this.Port = uri.split(":")[2] ;
		}
		else { // sans port specif
			this.Host = uri.split(":")[1].split("/")[0] ;
			System.out.println("----> "+this.Host + " " + uri);
		}
		this.GraphId = uri.split("/")[1].split(":")[0];
		this.GraphClass = uri.split("/")[1].split(":")[1];
	}

	/*
	private GraphConnector GraphConnectorFactory(String anId, String aHost, String aGraphClass) {
		try {
			GraphClient aGraphClient =  null ;
			if(aGraphClass.equals("rmi")) {
				aGraphClient = new GraphClientRMI(aHost) ;
			}
			aGraphClient.getGraphHandler().createGraph(anId, aGraphClass);
			return aGraphClient.getGraphHandler();
		}
		catch(RemoteException e) {
			System.out.println("Remote Exception in ConnectionRmi " + e.getMessage()) ;
			return null ;
		}
	}
	*/


	// Modifiers


	// set the id of the graph
	public void setGraphId(String value) {
		this.GraphId = value ;
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
	public void setGraphClass(String value) {
		this.GraphClass = value ;
	}


	//
	public void setDisplay(boolean value) {
		this.Display = value ;
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
	public String getGraphId() {
		return this.GraphId ;
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
	public String getGraphClass() {
		return this.GraphClass ;
	}

	//
	public boolean getDisplay() {
		return this.Display ;
	}

	// Method getParameter
	public String getParameter(String key) {
		return this.Params.get(key);
	}

	public String getUri() {
		return this.Uri ;
	}

}
