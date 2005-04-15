/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class InfoSitePublications extends DataTranferObject implements ISiteComponent {

	private List infoDidaticPublications;
	private List infoCientificPublications;
	//private List infoUndeterminedPublications;
	private List infoPublications;
	private List infoOtherPublications;
	private InfoTeacher infoTeacher;

	public InfoSitePublications() {
	}

	public Integer getNumberCientificPublications() {
		return new Integer(infoCientificPublications.size());
	}
	public Integer getNumberDidaticPublications() {
		return new Integer(infoDidaticPublications.size());
	}
	public Integer getNumberOtherPublications() {
		return new Integer(infoOtherPublications.size());
	}
	

	/**
	 * @return Returns the infoTeacher.
	 */
	public InfoTeacher getInfoTeacher() {
		return infoTeacher;
	}

	/**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
	public void setInfoTeacher(InfoTeacher infoTeacher) {
		this.infoTeacher = infoTeacher;
	}

	/**
	 * @return Returns the infoCientificPublications.
	 */
	public List getInfoCientificPublications() {
		return infoCientificPublications;
	}

	/**
	 * @return Returns the infoDidaticPublications.
	 */
	public List getInfoDidaticPublications() {
		return infoDidaticPublications;
	}
	
	/**
	 * @return Returns the infoPublications (both cientific and didatic)
	 */
	public List getInfoPublications() {
        if(infoPublications != null)
            return infoPublications;
		try {
	    List publications = new ArrayList();
	    if (infoCientificPublications != null && infoCientificPublications.size()>0){
	    	publications.addAll(infoCientificPublications);
        }
	    if (infoDidaticPublications != null && infoDidaticPublications.size()>0){
	    	publications.addAll(infoDidaticPublications);
        }
	    //publications.addAll(infoUndeterminedPublications);
	    infoPublications = publications;
		} catch(Exception e) {
			e.printStackTrace();
		}
	    return infoPublications;
	}

	/**
	 * @return Returns the infoOtherPublications ()
	 */
	public List getOtherPublications() {
	    return infoOtherPublications;
	}	
	
	/**
	 * @param infoCientificPublications The infoCientificPublications to set.
	 */
	public void setInfoCientificPublications(List infoCientificPublications) {
		this.infoCientificPublications = infoCientificPublications;
	}

	/**
	 * @param infoDidaticPublications The infoDidaticPublications to set.
	 */
	public void setInfoDidaticPublications(List infoDidaticPublications) {
		this.infoDidaticPublications = infoDidaticPublications;
	}

	/**
	 * @param infoOtherPublications The infoOtherPublications to set.
	 */
	public void setInfoOtherPublications(List infoOtherPublications) {
		this.infoOtherPublications = infoOtherPublications;
	}	
	
	
	/**
	 * @param infoPublications The infoPublications to set.
	 */
	public void setInfoPublications(List infoPublications) {
		this.infoPublications = infoPublications;
	}	
	
}
