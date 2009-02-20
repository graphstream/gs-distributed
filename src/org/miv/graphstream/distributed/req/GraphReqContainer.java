package org.miv.graphstream.distributed.req;

import java.io.Serializable;
import java.util.LinkedList;

public class GraphReqContainer implements Serializable {

	private String graphId ;
	private String className ;
	private String methodName ;

	private LinkedList<Object> parametres ;

	private boolean messageMode ;

	//Ajouter un boolean si la fonction doit ou pas renvoyer un resultat (modeField message ou modeField function)
	//Ajouter un wrapper pour le resultat attendu si ce n'est pas un type RMI serialisable
	//Ajouter un booleen si le resultat attendu doit être sérialisée manuellement ou pas (utilisation d'un jsonWrapper) sinon le resultat
	// serialisable nativement par java

	// Constructors

	public GraphReqContainer() {
		parametres = new LinkedList<Object>() ;
	}

	public GraphReqContainer(String aGraphId, String aMethodName) {
		parametres = new LinkedList<Object>() ;
		this.setGraphId(aGraphId);
		this.setMethodName(aMethodName);
	}

	public GraphReqContainer(String aGraphId, String aMethodName, Object[] params) {
		this.setGraphId(aGraphId);
		this.setMethodName(aMethodName);
		parametres = new LinkedList<Object>() ;
		for(int i = 0 ; i < params.length ; i++) {
			this.addParameter(params[i]);
		}
	}

	// Modifiers

	public void setGraphId(String value) {
		this.graphId = value ;
	}

	public void setMethodName(String value) {
		this.methodName = value ;
	}

	public void setParameter(int index, Object value) {
		if(this.parametres.contains(value)) {
			this.parametres.set(index, value);
		}
		else {
			this.parametres.add(index, value);
		}
	}

	public void setParameters(Object[] values) {
		this.parametres.clear();
		this.addParameters(values);
	}

	public void setClassName(String value) {
		this.className = value ;
	}

	public void setAllParams(String aGraphId, String aClassName, String aMethodeName, Object[] parameters) {
		clear();
		this.graphId = aGraphId ;
		this.className = aClassName ;
		this.methodName = aMethodeName ;
		this.setParameters(parameters) ;
	}

	public void setAllParams(String aGraphId, String aMethodeName, Object[] parameters) {
		clear();
		this.graphId = aGraphId ;
		this.className = aGraphId ;
		this.methodName = aMethodeName ;
		this.setParameters(parameters) ;
	}

	public void addParameter(Object value) {
		this.parametres.add(value);
	}

	public void addParameters(Object[] values) {
		for(int i = 0 ; i < values.length ; i++) {
			this.parametres.add(values[i]);
		}
	}

	// Accessors

	public String getGraphId() {
		return this.graphId ;
	}

	public String getClassName() {
		return this.className ;
	}

	public String getMethodName() {
		return this.methodName ;
	}

	// getNbParameters
	public int getNbParameters() {
		return this.parametres.size() ;
	}

	// getParametersValue
	public Object getParametersValue(int index) {
		return parametres.get(index) ;
	}

	// getParametersValue
	public Object[] getParametersValue() {
		Object[] res = new String[this.parametres.size()] ;
		for(int i = 0 ; i < this.parametres.size() ; i++) {
			res[i] = this.parametres.get(i) ;
		}
		return res ;
	}

	// clear
	public void clear() {
		this.graphId = null ;
		this.methodName = null ;
		this.className = null ;
		this.parametres.clear() ;
	}

	// toString
	public String toString() {
		return "";
	}

}
