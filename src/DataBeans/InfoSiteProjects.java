/*
 * Created on 4/Ago/2003
 *
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 *
 */

public class InfoSiteProjects extends DataTranferObject implements ISiteComponent{

	private List infoGroupPropertiesList;

	/**
	* @return
	*/
	public List getInfoGroupPropertiesList() {
		return infoGroupPropertiesList;
	}

	/**
	* @param name
	*/
	public void setInfoGroupPropertiesList(List infoGroupPropertiesList) {
		this.infoGroupPropertiesList = infoGroupPropertiesList;
	}

	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if(objectToCompare instanceof InfoSiteProjects)
			result = true;
			
		if (((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList() == null
			&& this.getInfoGroupPropertiesList() == null) {	
			return true;
		}

		if (((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList() == null
			|| this.getInfoGroupPropertiesList() == null
			|| ((InfoSiteProjects) objectToCompare).getInfoGroupPropertiesList().size()
				!= this.getInfoGroupPropertiesList().size()) {
			return false;
		}
		
		ListIterator iter1 =
			((InfoSiteProjects) objectToCompare)
				.getInfoGroupPropertiesList()
				.listIterator();
		ListIterator iter2 = this.getInfoGroupPropertiesList().listIterator();
		while (result && iter1.hasNext()) {
			
			InfoGroupProperties groupProperties1 = (InfoGroupProperties) iter1.next();
			InfoGroupProperties groupProperties2 = (InfoGroupProperties) iter2.next();
			if (!groupProperties1.equals(groupProperties2)) {
			
				result = false;
			}
		}
		
		return result;
	}
    
    public String toString()
    {
        String result = "[InfoSiteProjects: ";
        result += "infoGroupPropertiesList - " + this.getInfoGroupPropertiesList() + "]";
        return result;
    }

}
