/*
 * Created on 16/Nov/2003
 *
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class InfoServiceProviderRegime extends InfoObject
{
    private ProviderRegimeType providerRegimeType;
    private InfoTeacher infoTeacher;
    private Date lastModificationDate;
    
    public InfoServiceProviderRegime() 
    {
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

    /**
     * @return Returns the providerRegimeType.
     */
    public ProviderRegimeType getProviderRegimeType()
    {
        return providerRegimeType;
    }

    /**
     * @param providerRegimeType The providerRegimeType to set.
     */
    public void setProviderRegimeType(ProviderRegimeType providerRegimeType)
    {
        this.providerRegimeType = providerRegimeType;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

}
