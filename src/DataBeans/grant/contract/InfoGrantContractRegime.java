/*
 * Created on May 5, 2004
 */

package DataBeans.grant.contract;

import java.util.Calendar;
import java.util.Date;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Dominio.grant.contract.IGrantContractRegime;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegime extends InfoObject {

    private static final int activeState = 1;

    private static final int inactiveState = 0;

    private Integer state;

    private Date dateBeginContract;

    private Date dateEndContract;

    private Date dateSendDispatchCC;

    private Date dateDispatchCC;

    private Date dateSendDispatchCD;

    private Date dateDispatchCD;

    private InfoTeacher infoTeacher;

    private InfoGrantContract infoGrantContract;

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
     * @return Returns the teacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setInfoTeacher(InfoTeacher teacher) {
        this.infoTeacher = teacher;
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
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return infoGrantContract;
    }

    /**
     * @param infoGrantContract
     *            The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
    }

    public boolean getContractRegimeActive() {
        if (this.dateEndContract.after(Calendar.getInstance().getTime())) {
            return true;
        }
        return false;

    }

    public Integer getActiveStateValue() {
        return new Integer(activeState);
    }

    public Integer getInactiveStateValue() {
        return new Integer(inactiveState);
    }

    /**
     * @param GrantContractRegime
     */
    public void copyFromDomain(IGrantContractRegime grantContractRegime) {
        super.copyFromDomain(grantContractRegime);
        if (grantContractRegime != null) {
            setState(grantContractRegime.getState());
            setDateBeginContract(grantContractRegime.getDateBeginContract());
            setDateEndContract(grantContractRegime.getDateEndContract());
            setDateDispatchCC(grantContractRegime.getDateDispatchCC());
            setDateDispatchCD(grantContractRegime.getDateDispatchCD());
            setDateSendDispatchCC(grantContractRegime.getDateSendDispatchCC());
            setDateSendDispatchCD(grantContractRegime.getDateSendDispatchCD());
        }
    }

    /**
     * @param GrantContractRegime
     * @return
     */
    public static InfoGrantContractRegime newInfoFromDomain(
            IGrantContractRegime grantContractRegime) {
        InfoGrantContractRegime infoGrantContractRegime = null;
        if (grantContractRegime != null) {
            infoGrantContractRegime = new InfoGrantContractRegime();
            infoGrantContractRegime.copyFromDomain(grantContractRegime);
        }
        return infoGrantContractRegime;
    }

    /**
     * @return Returns the activeState.
     */
    public static Integer getActiveState() {
        return new Integer(activeState);
    }

    /**
     * @return Returns the inactiveState.
     */
    public static Integer getInactiveState() {
        return new Integer(inactiveState);
    }
}