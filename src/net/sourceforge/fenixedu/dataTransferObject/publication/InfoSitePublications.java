/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

public class InfoSitePublications extends DataTranferObject implements ISiteComponent {

	private List infoPublications;
	private InfoTeacher infoTeacher;

	public InfoSitePublications() {
	}

	public Integer getNumberPublications() {
		return new Integer(infoPublications.size());
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
	 * @return Returns the infoPublications.
	 */
	public List getInfoPublications() {
		return infoPublications;
	}
	
//	/**
//	 * @return Returns the infoPublications (both cientific and didatic)
//	 */
//	public List getInfoPublications() {
//        if(infoPublications != null)
//            return infoPublications;
//		try {
//	    List publications = new ArrayList();
//	    if (infoCientificPublications != null && infoCientificPublications.size()>0){
//	    	publications.addAll(infoCientificPublications);
//        }
//	    if (infoDidaticPublications != null && infoDidaticPublications.size()>0){
//	    	publications.addAll(infoDidaticPublications);
//        }
//	    //publications.addAll(infoUndeterminedPublications);
//	    infoPublications = publications;
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	    return infoPublications;
//	}



	/**
	 * @param infoPublications The infoDidaticPublications to set.
	 */
	public void setInfoPublications(List infoPublications) {
		this.infoPublications = infoPublications;
	}

}
