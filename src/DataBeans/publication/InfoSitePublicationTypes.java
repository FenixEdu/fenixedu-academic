/*
 * Created on Nov 13, 2003
 *  
 */
package DataBeans.publication;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.ISiteComponent;

/**
 * @author Sairf
 *  
 */
public class InfoSitePublicationTypes extends DataTranferObject implements ISiteComponent {

	private List infoPublicationTypes;

	public InfoSitePublicationTypes() {
	}


	/**
	 * @return Returns the infoPublicationTypes.
	 */
	public List getInfoPublicationTypes() {
		return infoPublicationTypes;
	}
	/**
	 * @param infoPublicationTypes The infoPublicationTypes to set.
	 */
	public void setInfoPublicationTypes(List infoPublicationTypes) {
		this.infoPublicationTypes = infoPublicationTypes;
	}
}
