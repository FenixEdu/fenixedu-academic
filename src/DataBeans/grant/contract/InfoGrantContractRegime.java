/*
 * Created on May 5, 2004
 */

package DataBeans.grant.contract;

import java.util.Date;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractRegime extends InfoObject {

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
}
