/*
 * Created on Jun 1, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans.publication;

import DataBeans.InfoObject;

/**
 * @author TJBF & PFON
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class InfoAuthorPerson extends InfoObject {

	private String keyFinal;
	
	private String name;
	
	private String organisation;
	/**
	 * 
	 */
	public InfoAuthorPerson() {
		super();
	}

	

	/**
	 * @return Returns the keyFinal.
	 */
	public String getKeyFinal() {
		return keyFinal;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the organisation.
	 */
	public String getOrganisation() {
		return organisation;
	}

	/**
	 * @param keyFinal The keyFinal to set.
	 */
	public void setKeyFinal(String keyFinal) {
		this.keyFinal = keyFinal;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param organisation The organisation to set.
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

}
