package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceValue extends InsuranceValue_Base {

    public InsuranceValue() {
    }

    public InsuranceValue(IExecutionYear executionYear, Double annualValue, Date endDate) {
        this.setExecutionYear(executionYear);
        this.setAnnualValue(annualValue);
        this.setEndDate(endDate);

    }

}
