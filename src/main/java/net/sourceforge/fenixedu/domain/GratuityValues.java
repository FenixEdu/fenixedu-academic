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

import java.util.Collections;

import org.fenixedu.bennu.core.domain.Bennu;

public class GratuityValues extends GratuityValues_Base {

    public GratuityValues() {
        super();
        setRootDomainObject(Bennu.getInstance());
        super.setPenaltyApplicable(true);
    }

    public void delete() {
        setExecutionDegree(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Double calculateTotalValueForMasterDegree() {
        Double totalValue = 0.0;

        Double annualValue = this.getAnualValue();

        if ((annualValue != null) && (annualValue > 0)) {
            // we have data to calculate using annual value
            totalValue = annualValue;
        } else {
            // we have to use the components information (scholarship +
            // final
            // proof)
            totalValue = this.getScholarShipValue() + (this.getFinalProofValue() == null ? 0.0 : this.getFinalProofValue());
        }

        return totalValue;
    }

    public Double calculateTotalValueForSpecialization(StudentCurricularPlan studentCurricularPlan) {

        ExecutionYear executionYear = this.getExecutionDegree().getExecutionYear();
        Double totalValue = 0.0;

        if ((this.getCourseValue() != null) && (this.getCourseValue() > 0)) {

            // calculate using value per course
            double valuePerCourse = this.getCourseValue();

            for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    totalValue += valuePerCourse;
                }
            }

        } else if ((this.getCreditValue() != null) && (this.getCreditValue() > 0)) {

            // calculate using value per credit
            double valuePerCredit = this.getCreditValue().doubleValue();
            double totalCredits = 0;

            for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                    totalCredits += enrolment.getCurricularCourse().getCredits();
                }
            }

            totalValue = totalCredits * valuePerCredit;

        }

        return totalValue;
    }

    public PaymentPhase getLastPaymentPhase() {
        return hasAnyPaymentPhaseList() ? Collections.max(getPaymentPhaseList(), PaymentPhase.COMPARATOR_BY_END_DATE) : null;
    }

    public boolean isPenaltyApplicable() {
        return getPenaltyApplicable().booleanValue();
    }

    @Deprecated
    public java.util.Date getEndPayment() {
        org.joda.time.YearMonthDay ymd = getEndPaymentYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndPayment(java.util.Date date) {
        if (date == null) {
            setEndPaymentYearMonthDay(null);
        } else {
            setEndPaymentYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartPayment() {
        org.joda.time.YearMonthDay ymd = getStartPaymentYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setStartPayment(java.util.Date date) {
        if (date == null) {
            setStartPaymentYearMonthDay(null);
        } else {
            setStartPaymentYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.GratuitySituation> getGratuitySituations() {
        return getGratuitySituationsSet();
    }

    @Deprecated
    public boolean hasAnyGratuitySituations() {
        return !getGratuitySituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.PaymentPhase> getPaymentPhaseList() {
        return getPaymentPhaseListSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPhaseList() {
        return !getPaymentPhaseListSet().isEmpty();
    }

    @Deprecated
    public boolean hasEndPaymentYearMonthDay() {
        return getEndPaymentYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAnualValue() {
        return getAnualValue() != null;
    }

    @Deprecated
    public boolean hasProofRequestPayment() {
        return getProofRequestPayment() != null;
    }

    @Deprecated
    public boolean hasCourseValue() {
        return getCourseValue() != null;
    }

    @Deprecated
    public boolean hasScholarShipValue() {
        return getScholarShipValue() != null;
    }

    @Deprecated
    public boolean hasFinalProofValue() {
        return getFinalProofValue() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasPenaltyApplicable() {
        return getPenaltyApplicable() != null;
    }

    @Deprecated
    public boolean hasEmployee() {
        return getEmployee() != null;
    }

    @Deprecated
    public boolean hasStartPaymentYearMonthDay() {
        return getStartPaymentYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasCreditValue() {
        return getCreditValue() != null;
    }

}
