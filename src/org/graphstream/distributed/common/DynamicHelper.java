package org.graphstream.distributed.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicHelper {
	
	
	private DynamicHelper() {
		
	}

	
	/**
	 * Dynamique invocation
	 */
	public static Object call2(Object anObject, String aMethod, boolean retour, Object ... params) {
		try {
			Class[] argType ;
			if(params!=null) {
				argType = new Class[params.length] ;
			} else {
				argType = new Class[0];
			}
		  	for(int i = 0 ; i < argType.length ; i++) {
		  		argType[i] = params[i].getClass();
		  	}
		  	Method m ;
		  	Object res ;

		  	m = anObject.getClass().getMethod(aMethod, argType);
		  	res = m.invoke(anObject, params);

			if(retour) {
				return res ;
			}
			else {
				return false ;
			}
		}
		catch(IllegalAccessException e) {
			System.out.println("IllegalAccessException exception : " + e.getMessage());
			return false ;
		}
		catch(NoSuchMethodException e) {
			System.out.println("NoSuchMethodException exception : " + e.getMessage());
			return  false ;
		}
		catch(InvocationTargetException e) {
			System.out.println("InvocationTargetException exception : " + e.getMessage() + e.getCause());
			return false ;
		}
	}

}
