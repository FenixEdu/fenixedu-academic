/*
 * Created on 20/Nov/2003
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantOrientationTeacher extends DomainObject implements IGrantOrientationTeacher {

    private Date beginDate;

    private Date endDate;

    private ITeacher OrientationTeacher;

    private IGrantContract grantContract;

    private Integer keyTeacher;

    private Integer keyContract;

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
    public IGrantContract getGrantContract() {
        return grantContract;
    }

    /**
     * @param grantContract
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }

    /**
     * @return
     */
    public Integer getKeyContract() {
        return keyContract;
    }

    /**
     * @param keyContract
     */
    public void setKeyContract(Integer keyContract) {
        this.keyContract = keyContract;
    }

    /**
     * @return
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return
     */
    public ITeacher getOrientationTeacher() {
        return OrientationTeacher;
    }

    /**
     * @param orientaionTeacher
     */
    public void setOrientationTeacher(ITeacher orientationTeacher) {
        OrientationTeacher = orientationTeacher;
    }

}