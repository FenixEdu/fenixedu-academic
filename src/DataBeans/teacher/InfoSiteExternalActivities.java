/*
 * Created on Nov 13, 2003
 *  
 */
package DataBeans.teacher;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.ISiteComponent;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteExternalActivities extends DataTranferObject implements ISiteComponent {

    private List infoExternalActivities;

    private InfoTeacher infoTeacher;

    public InfoSiteExternalActivities() {
    }

    /**
     * @return Returns the infoExternalActivities.
     */
    public List getInfoExternalActivities() {
        return infoExternalActivities;
    }

    /**
     * @param infoExternalActivities
     *            The infoExternalActivities to set.
     */
    public void setInfoExternalActivities(List infoExternalActivities) {
        this.infoExternalActivities = infoExternalActivities;
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

}