/*
 * Created on Nov 13, 2003
 *  
 */
package DataBeans.publication;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.ISiteComponent;
import DataBeans.InfoTeacher;

/**
 * @author TJBF
 * @author PFON
 *  
 */
public class InfoSitePublications extends DataTranferObject implements ISiteComponent {

    private List infoDidaticPublications;

    private List infoCientificPublications;

    private InfoTeacher infoTeacher;

    public InfoSitePublications() {
    }

    public Integer getNumberCientificPublications() {
        return new Integer(infoCientificPublications.size());
    }

    public Integer getNumberDidaticPublications() {
        return new Integer(infoDidaticPublications.size());
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
     * @param infoCientificPublications
     *            The infoCientificPublications to set.
     */
    public void setInfoCientificPublications(List infoCientificPublications) {
        this.infoCientificPublications = infoCientificPublications;
    }

    /**
     * @param infoDidaticPublications
     *            The infoDidaticPublications to set.
     */
    public void setInfoDidaticPublications(List infoDidaticPublications) {
        this.infoDidaticPublications = infoDidaticPublications;
    }

}