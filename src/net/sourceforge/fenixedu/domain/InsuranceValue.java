package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InsuranceValue extends InsuranceValue_Base {

    private Date endDate;

    public InsuranceValue() {

    }

    public InsuranceValue(IExecutionYear executionYear, Double annualValue, Date endDate) {
        this.setExecutionYear(executionYear);
        setAnnualValue(annualValue);
        this.endDate = endDate;

    }

    /**
     * @return Returns the endDate.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}