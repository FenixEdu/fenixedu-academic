/*
 * Created on Nov 13, 2003
 *  
 */
package DataBeans.teacher;

import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoTeacher;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteOldPublications implements ISiteComponent
{

    private List infoOldPublications;
    private OldPublicationType oldPublicationType;
    private InfoTeacher infoTeacher;

    public InfoSiteOldPublications()
    {
    }

    public Integer getNumberOldPublications()
    {
        return new Integer(infoOldPublications.size());
    }
    
    /**
	 * @return Returns the infoOldPublications.
	 */
    public List getInfoOldPublications()
    {
        return infoOldPublications;
    }

    /**
	 * @param infoOldPublications
	 *            The infoOldPublications to set.
	 */
    public void setInfoOldPublications(List infoOldPublications)
    {
        this.infoOldPublications = infoOldPublications;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    /**
	 * @return Returns the oldPublicationType.
	 */
    public OldPublicationType getOldPublicationType()
    {
        return oldPublicationType;
    }

    /**
	 * @param oldPublicationType
	 *            The oldPublicationType to set.
	 */
    public void setOldPublicationType(OldPublicationType oldPublicationType)
    {
        this.oldPublicationType = oldPublicationType;
    }

}
