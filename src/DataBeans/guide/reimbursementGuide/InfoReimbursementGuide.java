/*
 * Created on 12/Nov/2003
 *  
 */
package DataBeans.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoGuide;
import DataBeans.InfoObject;
import Util.State;

/**
 * 
 * 
 * This class contains all the information regarding a Reimbursement Guide. <br>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 *  
 */
public class InfoReimbursementGuide extends InfoObject {

    protected Integer number;

    protected InfoGuide infoGuide;

    protected Calendar creationDate;

    protected List infoReimbursementGuideSituations;

    protected List infoReimbursementGuideEntries;

    /**
     *  
     */
    public InfoReimbursementGuide() {

    }

    /**
     * @param reimbursementGuideId
     */
    public InfoReimbursementGuide(Integer reimbursementGuideId) {
        setIdInternal(reimbursementGuideId);
    }

    /**
     * @return
     */
    public Calendar getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return
     */
    public InfoGuide getInfoGuide() {
        return infoGuide;
    }

    /**
     * @param paymentGuide
     */
    public void setInfoGuide(InfoGuide paymentGuide) {
        this.infoGuide = paymentGuide;
    }

    /**
     * @return
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return
     */
    public List getInfoReimbursementGuideSituations() {
        return infoReimbursementGuideSituations;
    }

    /**
     * @param infoReimbursementGuideSituations
     */
    public void setInfoReimbursementGuideSituations(List infoReimbursementGuideSituations) {
        this.infoReimbursementGuideSituations = infoReimbursementGuideSituations;
    }

    /**
     * @return
     */
    public InfoReimbursementGuideSituation getActiveInfoReimbursementGuideSituation() {
        return (InfoReimbursementGuideSituation) CollectionUtils.find(
                getInfoReimbursementGuideSituations(), new Predicate() {
                    public boolean evaluate(Object obj) {
                        InfoReimbursementGuideSituation situation = (InfoReimbursementGuideSituation) obj;
                        return situation.getState().getState().intValue() == State.ACTIVE;
                    }
                });
    }

    /**
     * @return Returns the infoReimbursementGuideEntries.
     */
    public List getInfoReimbursementGuideEntries() {
        return infoReimbursementGuideEntries;
    }

    /**
     * @param infoReimbursementGuideEntries
     *            The infoReimbursementGuideEntries to set.
     */
    public void setInfoReimbursementGuideEntries(List infoReimbursementGuideEntries) {
        this.infoReimbursementGuideEntries = infoReimbursementGuideEntries;
    }

}