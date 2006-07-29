/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * 
 * 
 * This class contains all the information regarding a Reimbursement Guide. <br/>
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

    public void copyFromDomain(ReimbursementGuide reimbursementGuide) {
        super.copyFromDomain(reimbursementGuide);
        if (reimbursementGuide != null) {
            setCreationDate(reimbursementGuide.getCreationDate());
            setInfoGuide(InfoGuideWithPersonAndExecutionDegreeAndDegreeCurricularPlanAndDegreeAndContributor
                    .newInfoFromDomain(reimbursementGuide.getGuide()));
            setNumber(reimbursementGuide.getNumber());

            List infoReimbursementGuideEntries = (List) CollectionUtils.collect(reimbursementGuide
                    .getReimbursementGuideEntries(), new Transformer() {

                public Object transform(Object arg0) {
                    ReimbursementGuideEntry reimbursementGuideEntry = (ReimbursementGuideEntry) arg0;
                    return InfoReimbursementGuideEntry.newInfoFromDomain(reimbursementGuideEntry);
                }
            });

            setInfoReimbursementGuideEntries(infoReimbursementGuideEntries);

            List infoReimbursementGuideSituations = (List) CollectionUtils.collect(reimbursementGuide
                    .getReimbursementGuideSituations(), new Transformer() {

                public Object transform(Object arg0) {
                    ReimbursementGuideSituation reimbursementGuideSituation = (ReimbursementGuideSituation) arg0;
                    return InfoReimbursementGuideSituation
                            .newInfoFromDomain(reimbursementGuideSituation);
                }
            });

            setInfoReimbursementGuideSituations(infoReimbursementGuideSituations);

        }
    }

    public static InfoReimbursementGuide newInfoFromDomain(ReimbursementGuide reimbursementGuide) {
        InfoReimbursementGuide infoReimbursementGuide = null;
        if (reimbursementGuide != null) {
            infoReimbursementGuide = new InfoReimbursementGuide();
            infoReimbursementGuide.copyFromDomain(reimbursementGuide);
        }
        return infoReimbursementGuide;
    }

}