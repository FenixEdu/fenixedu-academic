/*
 * Created on 13/Set/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author joaosa & rmalo
 *
 */

public class InfoSiteSentedProjectProposalsWaiting extends DataTranferObject implements ISiteComponent{

	private List infoSentedProjectProposalsWaitingList;


	public List getInfoGroupPropertiesList() {
		return infoSentedProjectProposalsWaitingList;
	}

	
	public void setInfoGroupPropertiesList(List infoGroupPropertiesList) {
		this.infoSentedProjectProposalsWaitingList = infoGroupPropertiesList;
	}

	
	
	public boolean equals(Object objectToCompare) {
		boolean result = false;
		if(objectToCompare instanceof InfoSiteSentedProjectProposalsWaiting)
			result = true;
			
		if (((InfoSiteSentedProjectProposalsWaiting) objectToCompare).getInfoGroupPropertiesList() == null
			&& this.getInfoGroupPropertiesList() == null) {	
			return true;
		}

		if (((InfoSiteSentedProjectProposalsWaiting) objectToCompare).getInfoGroupPropertiesList() == null
			|| this.getInfoGroupPropertiesList() == null
			|| ((InfoSiteSentedProjectProposalsWaiting) objectToCompare).getInfoGroupPropertiesList().size()
				!= this.getInfoGroupPropertiesList().size()) {
			return false;
		}
		
		ListIterator iter1 =
			((InfoSiteSentedProjectProposalsWaiting) objectToCompare)
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
        String result = "[InfoSiteSentedProjectProposalsWaiting: ";
        result += "infoSiteSentedProjectProposalsWaitingList - " + this.getInfoGroupPropertiesList() + "]";
        return result;
    }

}
