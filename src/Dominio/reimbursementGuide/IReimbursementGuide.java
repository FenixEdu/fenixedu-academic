/*
 * Created on 13/Nov/2003
 *  
 */
package Dominio.reimbursementGuide;

import java.util.Calendar;
import java.util.List;

import Dominio.IDomainObject;
import Dominio.IGuide;

/**
 * fenix-head Dominio.reimbursementGuide
 * 
 * @author João Mota 13/Nov/2003
 *  
 */
public interface IReimbursementGuide extends IDomainObject {
    /**
     * @return
     */
    public Calendar getCreationDate();

    /**
     * @param creationDate
     */
    public void setCreationDate(Calendar creationDate);

    /**
     * @return
     */
    public IGuide getGuide();

    /**
     * @param paymentGuide
     */
    public void setGuide(IGuide paymentGuide);

    public Integer getNumber();

    /**
     * @param number
     */
    public void setNumber(Integer number);

    public IReimbursementGuideSituation getActiveReimbursementGuideSituation();

    public List getReimbursementGuideSituations();

    public void setReimbursementGuideSituations(List reimbursementGuideSituations);

    /**
     * @return Returns the reimbursementGuideEntries.
     */
    public abstract List getReimbursementGuideEntries();

    /**
     * @param reimbursementGuideEntries
     *            The reimbursementGuideEntries to set.
     */
    public abstract void setReimbursementGuideEntries(List reimbursementGuideEntries);
}