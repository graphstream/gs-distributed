package org.miv.graphstream.distributed.req;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GraphReqThread extends Thread {

	GraphGenericReq Req ;
	GraphRespContainer Res ;

	public GraphReqThread(GraphGenericReq aReq, GraphRespContainer aRes) {
		this.Req = aReq ;
		this.Res = aRes ;
	}


	public void run() {
		try {
		  	Object result = lancerMethode(	this.Req.getObjInst(),
		  									this.Req.getParameters(),
		  									this.Req.getMethodName()) ;
		  	if(this.Res != null) {
		  		this.Res.addRes(result);
		  		//System.out.println("Result : "+(Integer)result);
		  	}
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception in run : " + e.getMessage() + e.getStackTrace());
			for(int i = 0 ; i < e.getStackTrace().length ; i++) {
				System.out.println("StackTrace : " + e.getStackTrace()[i]);
			}
		}
		catch(Exception e) {
			System.out.println("run Exception (run()) : " + e.getMessage());
		}

	}

	public Object lancerMethode(Object obj, Object[] args, String nomMethode) throws Exception
	{
	   Class[] paramTypes = null;
	   if(args != null)
	   {
	      paramTypes = new Class[args.length];
	      for(int i=0;i<args.length;++i)
	      {
	         paramTypes[i] = args[i].getClass();
	      }
	   }
	   Method m = obj.getClass().getMethod(nomMethode,paramTypes);
	   return m.invoke(obj,args);
	}
}
