/*
 * Created on 20/Nov/2003
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.IGrantOrientationTeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class InfoGrantOrientationTeacher extends InfoObject {

    private Date beginDate;

    private Date endDate;

    private InfoTeacher orientationTeacherInfo;

    private InfoGrantContract grantContractInfo;

    /**
     * @return
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public InfoGrantContract getGrantContractInfo() {
        return grantContractInfo;
    }

    /**
     * @param grantContractInfo
     */
    public void setGrantContractInfo(InfoGrantContract grantContractInfo) {
        this.grantContractInfo = grantContractInfo;
    }

    /**
     * @return
     */
    public InfoTeacher getOrientationTeacherInfo() {
        return orientationTeacherInfo;
    }

    /**
     * @param orientationTeacherInfo
     */
    public void setOrientationTeacherInfo(InfoTeacher orientationTeacher) {
        orientationTeacherInfo = orientationTeacher;
    }

    /**
     * @param GrantOrientationTeacher
     */
    public void copyFromDomain(IGrantOrientationTeacher grantOrientationTeacher) {
        super.copyFromDomain(grantOrientationTeacher);
        if (grantOrientationTeacher != null) {
            setBeginDate(grantOrientationTeacher.getBeginDate());
            setEndDate(grantOrientationTeacher.getEndDate());
        }
    }

    /**
     * @param GrantOrientationTeacher
     * @return
     */
    public static InfoGrantOrientationTeacher newInfoFromDomain(
            IGrantOrientationTeacher grantOrientationTeacher) {
        InfoGrantOrientationTeacher infoGrantOrientationTeacher = null;
        if (grantOrientationTeacher != null) {
            infoGrantOrientationTeacher = new InfoGrantOrientationTeacher();
            infoGrantOrientationTeacher.copyFromDomain(grantOrientationTeacher);
        }
        return infoGrantOrientationTeacher;
    }

    public void copyToDomain(InfoGrantOrientationTeacher infoGrantOrientationTeacher,
            IGrantOrientationTeacher grantOrientationTeacher) {
        super.copyToDomain(infoGrantOrientationTeacher, grantOrientationTeacher);

        grantOrientationTeacher.setBeginDate(infoGrantOrientationTeacher.getBeginDate());
        grantOrientationTeacher.setEndDate(infoGrantOrientationTeacher.getEndDate());
    }

    public static IGrantOrientationTeacher newDomainFromInfo(
            InfoGrantOrientationTeacher infoGrantOrientationTeacher) {
        IGrantOrientationTeacher grantOrientationTeacher = null;
        if (infoGrantOrientationTeacher != null) {
            grantOrientationTeacher = new GrantOrientationTeacher();
            infoGrantOrientationTeacher.copyToDomain(infoGrantOrientationTeacher,
                    grantOrientationTeacher);
        }
        return grantOrientationTeacher;
    }
}