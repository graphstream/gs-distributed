package org.miv.graphstream.distributed.req;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.miv.graphstream.algorithm.Algorithm;
import org.miv.graphstream.distributed.rmi.GraphRegistry;
import org.miv.graphstream.graph.Graph;

public class GraphReqRunner {

	//GraphRemoteObjectFactory
	HashMap<String, Object> objectRegistry ;
	String graphId ;
	GraphRegistry graphRegistry ;

	// Constructor
	public GraphReqRunner(Graph aGraph) {
		this.graphId = aGraph.getId();
		objectRegistry = new HashMap<String, Object>() ;
		objectRegistry.put(aGraph.getId(), aGraph);
	}

	public GraphReqRunner(GraphRegistry aGraphRegistry) {
		this.graphRegistry = aGraphRegistry ;
	}

	// Creation dynamique d'objets et enregistrement ds une hastable
	public void newAlgorithm(String className, String instanceName) {
		objectRegistry.put(instanceName, (new GraphRemoteObjectFactory()).newInstance(className));
		((Algorithm)(objectRegistry.get(instanceName))).init((Graph)objectRegistry.get(this.graphId));
	}

	// Ajoute une instance de classe existante
	/*public void addClassInst(String ) {

	}*/

	// Créé dynamiquement une instance de classe et l'ajoute ds le registry
	public void newClassInst(String className, String instanceName) {
		objectRegistry.put(instanceName, (new GraphRemoteObjectFactory()).newInstance(className));
		((Algorithm)(objectRegistry.get(instanceName))).init((Graph)objectRegistry.get(this.graphId));
	}

	// Modifier
	public Object executeRequest(GraphReqContainer req) {
		try {
			Class[] argType = new Class[req.getNbParameters()] ;
		  	for(int i = 0 ; i < argType.length ; i++) {
		  		argType[i] = req.getParametersValue(i).getClass();
		  	}
		  	Method m = objectRegistry.get(req.getGraphId()).getClass().getMethod(req.getMethodName(), argType);
			Object res = m.invoke(objectRegistry.get(req.getGraphId()), req.getParametersValue());

			// bidouille ! pour savoir si une classe est serializable ou pas (je regarde si c'est une classe du package java ou pas)
			// a modifier
			if(!res.getClass().getName().startsWith("org.miv")) {
				return res ;
			}
			else {
				return res.toString() ;
			}
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return null ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return  null ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception : " + e.getMessage() + e.getCause());
			return null ;
		}

	}

	/**
	 * Nouvelles modifs
	 */

	public Object run(GraphGenericReq aReq) {

		try {
		  	return lancerMethode(	aReq.getObjInst(),
		  							aReq.getParameters(),
		  							aReq.getMethodName()) ;
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return null ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return null ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception in run : " + e.getMessage() + e.getStackTrace());
			for(int i = 0 ; i < e.getStackTrace().length ; i++) {
				System.out.println("StackTrace : " + e.getStackTrace()[i]);
			}
			return null ;
		}
		catch(Exception e) {
			System.out.println("run Exception (run()) : " + e.getMessage());
			return null ;
		}

	}

	public Object run(String id, String method, Object[] params) {

		try {
		  	return lancerMethode(	this.graphRegistry.getClient(id).exec(),
		  							params,
		  							method) ;
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return null ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return null ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception in run : " + e.getMessage() + e.getStackTrace());
			for(int i = 0 ; i < e.getStackTrace().length ; i++) {
				System.out.println("StackTrace : " + e.getStackTrace()[i]);
			}
			return null ;
		}
		catch(Exception e) {
			System.out.println("run Exception (run()) : " + e.getMessage());
			return null ;
		}

	}

	public Object lancerMethode(Object obj, Object[] args, String nomMethode) throws Exception	{
	   Class[] paramTypes = null;
	   if(args != null) {
	      paramTypes = new Class[args.length];
	      for(int i=0;i<args.length;++i) {
	         paramTypes[i] = args[i].getClass();
	      }
	   }
	   Method m = obj.getClass().getMethod(nomMethode,paramTypes);
	   return m.invoke(obj,args);
	}



}
