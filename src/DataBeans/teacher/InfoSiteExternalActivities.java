/*
 * Created on Nov 13, 2003
 *  
 */
package DataBeans.teacher;

import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteExternalActivities extends InfoObject implements ISiteComponent
{

	private List infoExternalActivities;
    
    public InfoSiteExternalActivities()
    {
    }
    
    /**
     * @return Returns the infoExternalActivities.
     */
    public List getInfoExternalActivities()
    {
        return infoExternalActivities;
    }

    /**
     * @param infoExternalActivities The infoExternalActivities to set.
     */
    public void setInfoExternalActivities(List infoExternalActivities)
    {
        this.infoExternalActivities = infoExternalActivities;
    }
}
