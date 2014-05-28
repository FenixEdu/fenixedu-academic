/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
