/*
 * Created on May 5, 2004
 */
package Dominio.grant.contract;

import java.util.Calendar;
import java.util.Date;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantContractRegime extends DomainObject implements IGrantContractRegime {

    private Integer state;

    private Date dateBeginContract;

    private Date dateEndContract;

    private Date dateSendDispatchCC;

    private Date dateDispatchCC;

    private Date dateSendDispatchCD;

    private Date dateDispatchCD;

    private ITeacher teacher;

    private Integer keyTeacher;

    private IGrantContract grantContract;

    private Integer keyGrantContract;
    
    private Integer costCenterKey;
    
    private IGrantCostCenter grantCostCenter;
    

	
    /**
     * Constructor
     */
    public GrantContractRegime() {
    }

    /**
     * @return Returns the dateBeginContract.
     */
    public Date getDateBeginContract() {
        return dateBeginContract;
    }

    /**
     * @param dateBeginContract
     *            The dateBeginContract to set.
     */
    public void setDateBeginContract(Date dateBeginContract) {
        this.dateBeginContract = dateBeginContract;
    }

    /**
     * @return Returns the dateDispatchCC.
     */
    public Date getDateDispatchCC() {
        return dateDispatchCC;
    }

    /**
     * @param dateDispatchCC
     *            The dateDispatchCC to set.
     */
    public void setDateDispatchCC(Date dateDispatchCC) {
        this.dateDispatchCC = dateDispatchCC;
    }

    /**
     * @return Returns the dateDispatchCD.
     */
    public Date getDateDispatchCD() {
        return dateDispatchCD;
    }

    /**
     * @param dateDispatchCD
     *            The dateDispatchCD to set.
     */
    public void setDateDispatchCD(Date dateDispatchCD) {
        this.dateDispatchCD = dateDispatchCD;
    }

    /**
     * @return Returns the dateEndContract.
     */
    public Date getDateEndContract() {
        return dateEndContract;
    }

    /**
     * @param dateEndContract
     *            The dateEndContract to set.
     */
    public void setDateEndContract(Date dateEndContract) {
        this.dateEndContract = dateEndContract;
    }

    /**
     * @return Returns the dateSendDispatchCC.
     */
    public Date getDateSendDispatchCC() {
        return dateSendDispatchCC;
    }

    /**
     * @param dateSendDispatchCC
     *            The dateSendDispatchCC to set.
     */
    public void setDateSendDispatchCC(Date dateSendDispatchCC) {
        this.dateSendDispatchCC = dateSendDispatchCC;
    }

    /**
     * @return Returns the dateSendDispatchCD.
     */
    public Date getDateSendDispatchCD() {
        return dateSendDispatchCD;
    }

    /**
     * @param dateSendDispatchCD
     *            The dateSendDispatchCD to set.
     */
    public void setDateSendDispatchCD(Date dateSendDispatchCD) {
        this.dateSendDispatchCD = dateSendDispatchCD;
    }

    /**
     * @return Returns the state.
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     *            The state to set.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return Returns the grantContract.
     */
    public IGrantContract getGrantContract() {
        return grantContract;
    }

    /**
     * @param grantContract
     *            The grantContract to set.
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }

    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return keyGrantContract;
    }

    /**
     * @param keyGrantContract
     *            The keyGrantContract to set.
     */
    public void setKeyGrantContract(Integer keyGrantContract) {
        this.keyGrantContract = keyGrantContract;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    public boolean getContractRegimeActive() {
        if (this.dateEndContract.after(Calendar.getInstance().getTime())) {
            return true;
        }
        return false;
    }
		/**
	 * @return Returns the costCenterkey.
	 */
	public Integer getCostCenterKey() {
		return costCenterKey;
	}
	/**
	 * @param costCenterkey The costCenterkey to set.
	 */
	public void setCostCenterKey(Integer costCenterKey) {
		this.costCenterKey = costCenterKey;
	}
	/**
	 * @return Returns the grantCostCenter.
	 */
	public IGrantCostCenter getGrantCostCenter() {
		return grantCostCenter;
	}
	/**
	 * @param grantCostCenter The grantCostCenter to set.
	 */
	public void setGrantCostCenter(IGrantCostCenter grantCostCenter) {
		this.grantCostCenter = grantCostCenter;
	}
}
