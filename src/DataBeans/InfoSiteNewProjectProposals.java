/*
 * Created on 13/Set/2004
 *
 */
package DataBeans;

import java.util.List;
import java.util.ListIterator;

/**
 * @author joaosa & rmalo
 *
 */

public class InfoSiteNewProjectProposals extends DataTranferObject implements ISiteComponent{

	private List infoNewProjectProposalsList;


	public List getInfoGroupPropertiesList() {
		return infoNewProjectProposalsList;
	}

	
	
	
	public void setInfoGroupPropertiesList(List infoGroupPropertiesList) {
		this.infoNewProjectProposalsList = infoGroupPropertiesList;
	}

	
	
	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if(objectToCompare instanceof InfoSiteNewProjectProposals)
			result = true;
			
		if (((InfoSiteNewProjectProposals) objectToCompare).getInfoGroupPropertiesList() == null
			&& this.getInfoGroupPropertiesList() == null) {	
			return true;
		}

		if (((InfoSiteNewProjectProposals) objectToCompare).getInfoGroupPropertiesList() == null
			|| this.getInfoGroupPropertiesList() == null
			|| ((InfoSiteNewProjectProposals) objectToCompare).getInfoGroupPropertiesList().size()
				!= this.getInfoGroupPropertiesList().size()) {
			return false;
		}
		
		ListIterator iter1 =
			((InfoSiteNewProjectProposals) objectToCompare)
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
        String result = "[InfoSiteNewProjectProposals: ";
        result += "infoNewProjectProposalsList - " + this.getInfoGroupPropertiesList() + "]";
        return result;
    }

}
