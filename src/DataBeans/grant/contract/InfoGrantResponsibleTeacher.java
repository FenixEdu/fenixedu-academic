/*
 * Created on 20/Nov/2003
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author  Barbosa
 * @author  Pica
 * 
 */

public class InfoGrantResponsibleTeacher extends InfoObject
{
    private Date beginDate;
    private Date endDate;
    private InfoTeacher responsibleTeacherInfo;
    private InfoGrantContract grantContractInfo;

    /**
     * @return
     */
    public Date getBeginDate()
    {
        return beginDate;
    }

    /**
     * @param beginDate
     */
    public void setBeginDate(Date beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     * @return
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public InfoGrantContract getGrantContractInfo()
    {
        return grantContractInfo;
    }

    /**
     * @param grantContractInfo
     */
    public void setGrantContractInfo(InfoGrantContract grantContractInfo)
    {
        this.grantContractInfo = grantContractInfo;
    }

    /**
     * @return
     */
    public InfoTeacher getResponsibleTeacherInfo()
    {
        return responsibleTeacherInfo;
    }

    /**
     * @param responsibleTeacherInfo
     */
    public void setResponsibleTeacherInfo(InfoTeacher responsibleTeacherInfo)
    {
        this.responsibleTeacherInfo = responsibleTeacherInfo;
    }

}