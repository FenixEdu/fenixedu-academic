/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package DataBeans.teacher;

import DataBeans.InfoObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InfoCategory extends InfoObject {

	private String name;

	public InfoCategory() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoCategory) {
			resultado = getName().equals(((InfoCategory) obj).getName());
		}
		return resultado;
	}
}
