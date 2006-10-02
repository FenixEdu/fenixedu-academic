package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceValue extends InsuranceValue_Base {

    public InsuranceValue() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public InsuranceValue(ExecutionYear executionYear, Double annualValue, Date endDate) {
	this(executionYear, BigDecimal.valueOf(annualValue), endDate);
    }

    public InsuranceValue(ExecutionYear executionYear, BigDecimal annualValue, Date endDate) {
	this();
	this.setExecutionYear(executionYear);
	this.setAnnualValueBigDecimal(annualValue);
	this.setEndDate(endDate);
    }

    @Deprecated
    public Double getAnnualValue() {
	return getAnnualValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setAnnualValue(Double annualValue) {
	setAnnualValueBigDecimal(BigDecimal.valueOf(annualValue));
    }

}
