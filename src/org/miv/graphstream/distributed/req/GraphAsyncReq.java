package org.miv.graphstream.distributed.req;

import java.util.Iterator;
import java.util.List;

import org.miv.graphstream.distributed.rmi.GraphRegistry;

public class GraphAsyncReq {

	GraphRespContainer Res ;
	GraphRegistry Registry ;

	// Constructors

	public GraphAsyncReq(GraphRegistry aRegistry, GraphRespContainer aRes) {
		this.Res = aRes ;
		this.Registry = aRegistry ;
	}

	public GraphAsyncReq(GraphRegistry aRegistry) {
		this.Res = new GraphRespContainer() ;
		this.Registry = aRegistry ;
	}

	/** Iteration sur la liste des requetes
	 *
	 * @param req
	 */
	public void exec(List<GraphGenericReq> req) {
		try {
			// Lancement des threads
			Thread[] t = new GraphReqThread[req.size()];
			for(int i = 0 ; i < req.size() ; i++) {
				t[i] = new GraphReqThread(req.get(i), this.Res);
				t[i].start();
			}
			// Attends la fin des threads
			for(int i = 0 ; i < req.size() ; i++) {
				t[i].join();
			}
		}
		catch(InterruptedException e) {
			System.out.println("error : " + e.getMessage()) ;
		}
	}


	/** Iteration sur les graphs dans le registry
	 *
	 * @param req
	 */
	public void exec(GraphGenericReq req) {
		try {
			// Lancement des threads
			Thread[] t = new GraphReqThread[this.Registry.getNbClients()];
			Iterator<String> it = this.Registry.getKeySet();
			int i = 0 ;
			while(it.hasNext()) {
				String graphId = it.next();
				System.out.println("graphId = " + graphId);
				req.setGraphId(graphId);
				req.setObjInst(this.Registry.getClient(graphId).exec());
				t[i] = new GraphReqThread(req, this.Res);
				t[i].start();
				i++ ;
			}
			// Attends la fin des threads
			for(i = 0 ; i < this.Registry.getNbClients() ; i++) {
				t[i].join();
			}
		}
		catch(InterruptedException e) {
			System.out.println("error : " + e.getMessage()) ;
		}
	}

	public GraphRespContainer getRes() {
		return this.Res ;
	}

	// Chargement de fichiers distribués
	/*public void loader(List<String[]> files) {
		try {
			Thread[] t = new GraphReqThread[files.size()];
			int i = 0 ;

			for(int j = 0 ; j < files.size() ; j++) {
				String id = files.get(j)[0];
				String fileName = files.get(j)[1];
				t[j] = new GraphReqThread(new GraphGenericReq(id, this.Registry.getClient(id).exec(), "loadData", new Object[] { fileName }), this.Res);
				t[j].start();
				i++;
			}
			// Attends la fin des threads
			for(int j = 0 ; j < files.size() ; j++) {
				t[j].join();
			}
		}
		catch(InterruptedException e) {
			System.out.println("error : " + e.getMessage()) ;
		}
	}*/


}
