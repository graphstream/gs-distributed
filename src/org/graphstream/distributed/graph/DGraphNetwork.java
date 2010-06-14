	package org.graphstream.distributed.graph;

import java.util.HashMap;

import org.graphstream.distributed.common.DGraphParser;
import org.graphstream.distributed.common.EnumUri;
import org.graphstream.distributed.rmi.RMIDGraphAdapter;
import org.graphstream.distributed.rmi.RMIHelper;

public class DGraphNetwork {

	// Variables
	private HashMap<String, RMIDGraphAdapter> RmiHandler ;
	private HashMap<String, String> Uri ;
	
	// Constructor
	public DGraphNetwork() {
		this.RmiHandler = new HashMap<String, RMIDGraphAdapter>() ;
		this.Uri = new HashMap<String, String>() ;
	}
	
	
	// Methods (public)
	
	
	public void add(String uri) {
		String name = DGraphParser.uri(uri).get(EnumUri.DGraphName) ;
		this.RmiHandler.put(name, RMIHelper.register(uri));
		this.Uri.put(name, uri);
	}
	
	public void del(String name) {
		this.RmiHandler.remove(name);
		this.Uri.remove(name);
	}
	
	/**
	 * register 
	 * si une classe est définie alors 
	 */
	/*public void add(String uri) {
		try {	
			//creation de la reference en local
			String name = DGraphParser.uri(uri).get(EnumUri.DGraphName) ;
			this.RmiHandler.put(name, RMIHelper.register(uri));
			this.Uri.put(name, uri);
			
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
	/*public void del(String anId) {
		//suppresion des references distantes
		if(this.DGraphClients.containsKey(anId)) {
			for(String k : this.DGraphClients.keySet()) {
				try {
					//this.DGraphClients.get(k).unregisterNotify(anId);
					this.DGraphClients.get(k).exec(null, "", "unregisterNotify", new String[] {anId});
				}
				catch(RemoteException e) {
					System.out.println(e.getMessage());
				}
			}
			//suppression de la reference du client en local
			this.DGraphClients.remove(anId);
		}
		
	}*/
	
	public RMIDGraphAdapter getDGraph(String id) {
		return this.RmiHandler.get(id);
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
