/*
 * Created on Aug 2, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IInsuranceValue extends IDomainObject {

    /**
     * @return Returns the anualValue.
     */
    public abstract Double getAnnualValue();

    /**
     * @param anualValue
     *            The anualValue to set.
     */
    public abstract void setAnnualValue(Double anualValue);

    /**
     * @return Returns the executionYear.
     */
    public abstract IExecutionYear getExecutionYear();

    /**
     * @param executionYear
     *            The executionYear to set.
     */
    public abstract void setExecutionYear(IExecutionYear executionYear);

    /**
     * @return Returns the endDate.
     */
    public Date getEndDate();

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Date endDate);
}