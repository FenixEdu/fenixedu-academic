package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InsuranceValue extends InsuranceValue_Base {

    public InsuranceValue() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasAnnualValueBigDecimal() {
        return getAnnualValueBigDecimal() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
