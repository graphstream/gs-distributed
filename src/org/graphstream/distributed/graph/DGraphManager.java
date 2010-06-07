	package org.graphstream.distributed.graph;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.DGraphUri;
import org.graphstream.distributed.common.EnumUri;
import org.graphstream.distributed.rmi.RMIDGraph;
import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

public class DGraphManager {

	// Variables
	HashMap<String, RMIDGraphAdapter> DGraphClients ;
	HashMap<String, DGraphUri> DGraphUriIndex ;
	
	Graph meta ;
	
	// Constructor
	public DGraphManager(String name) {
		this.DGraphClients = new HashMap<String, RMIDGraphAdapter>() ;
		this.DGraphUriIndex = new HashMap<String, DGraphUri>() ;
		Graph meta = new DefaultGraph(name);
	}
	
	
	// Methods (public)
	
	/**
	 * register 
	 * si une classe est définie alors 
	 */
	/*public void register(String uri) {
		try {
			Map<String,Object> data = DGraphParser.uri(uri);
			meta.addNode((String)data.get(EnumUri.DGraphName)).addAttributes(data);

			//creation de la reference en local
			this.DGraphClients.put(uri.getDGraphId(), RMIRegister(uri));
			this.DGraphUriIndex.put(uri.getDGraphId(), uri);
			
			//création/initialisation de la references distante
			this.DGraphClients.get(uri.getDGraphId()).bind(uri.getDGraphId());
			this.DGraphClients.get(uri.getDGraphId()).init(uri.getDGraphClass(), null);
			
			//propagation aux autres serveur
			for(RMIDGraphAdapter v : this.DGraphClients.values()) {
				v.registerNotify(anUri);
			}
			
		}
		catch(Exception e) {
			System.out.println("Register error" + e.getMessage());
		}
	}*/
	
	/**
	 * unregister
	 * @param anId
	 */
	public void unregister(String anId) {
		//suppresion des references distantes
		if(this.DGraphClients.containsKey(anId)) {
			for(String k : this.DGraphClients.keySet()) {
				try {
					this.DGraphClients.get(k).unregisterNotify(anId);
				}
				catch(RemoteException e) {
					System.out.println(e.getMessage());
				}
			}
			//suppression de la reference du client en local
			this.DGraphClients.remove(anId);
		}
		
	}
	
	public RMIDGraphAdapter getDGraph(String id) {
		return this.DGraphClients.get(id);
	}
	
	
	// Methods (private)
	
	/*private RMIDGraphAdapter RMIRegister(DGraphUri anUri) {
		try {
			return (RMIDGraphAdapter)Naming.lookup("rmi://"+anUri.getHost()+"/"+anUri.getDGraphId()) ;
		}
		catch (RemoteException exp) {
				System.out.println("RemoteException dans RMIRegister : " +	exp);
				return null ;
		}
		catch (NotBoundException exp) {
				System.out.println("NotBoundException dans RMIRegister : "	+ exp);
				return null ;
		}
		catch (MalformedURLException exp) {
				System.out.println("MalformedURLException dans RMIRegister : " + exp);
				return null ;
		}
	}*/
	
	/**
	 * 
	 * @throws RemoteException
	 */
	/*public void bind(String host, String id) {
		try	{
			this.Id = id ;
			RMIDGraph dg = new RMIDGraph();
			Naming.rebind( String.format( "//"+host+"/%s", this.Id ), dg );
		}
		catch( RemoteException e ) {
			e.printStackTrace();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}*/

}
