/*
 * Created on 21/Nov/2003
 *  
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoPublicationsNumber extends InfoObject
{
    private PublicationType publicationType;
    private Date lastModificationDate;
    private Integer national;
    private Integer international;
    private InfoTeacher infoTeacher;

    /**
	 *  
	 */
    public InfoPublicationsNumber()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @return Returns the international.
	 */
    public Integer getInternational()
    {
        return international;
    }

    /**
	 * @param international
	 *            The international to set.
	 */
    public void setInternational(Integer international)
    {
        this.international = international;
    }

    /**
	 * @return Returns the lastModificationDate.
	 */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
	 * @param lastModificationDate
	 *            The lastModificationDate to set.
	 */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    /**
	 * @return Returns the national.
	 */
    public Integer getNational()
    {
        return national;
    }

    /**
	 * @param national
	 *            The national to set.
	 */
    public void setNational(Integer national)
    {
        this.national = national;
    }

    /**
	 * @return Returns the publicationType.
	 */
    public PublicationType getPublicationType()
    {
        return publicationType;
    }

    /**
	 * @param publicationType
	 *            The publicationType to set.
	 */
    public void setPublicationType(PublicationType publicationType)
    {
        this.publicationType = publicationType;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
     * @param infoTeacher The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

}
