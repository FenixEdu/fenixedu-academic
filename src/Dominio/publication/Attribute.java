/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.publication;

import java.util.List;

import Dominio.DomainObject;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Attribute extends DomainObject implements IAttribute {
	
	private String attributeType;
	
	private List publications;
	
	/**
	 * 
	 */
	public Attribute() {
	}
	
	/**
	 * @return Returns the attributeType.
	 */
	public String getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType The attributeType to set.
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * @return Returns the publications.
	 */
	public List getPublications() {
		return publications;
	}
	/**
	 * @param publications The publications to set.
	 */
	public void setPublications(List publications) {
		this.publications = publications;
	}
}
