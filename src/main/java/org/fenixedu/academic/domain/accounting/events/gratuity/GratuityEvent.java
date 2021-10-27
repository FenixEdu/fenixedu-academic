/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountType;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventState;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public abstract class GratuityEvent extends GratuityEvent_Base {

    protected GratuityEvent() {
        super();
    }

    protected void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear) {
        init(administrativeOffice, EventType.GRATUITY, person, studentCurricularPlan, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
            StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        super.init(administrativeOffice, eventType, person, executionYear);
        checkParameters(administrativeOffice, studentCurricularPlan);
        super.setStudentCurricularPlan(studentCurricularPlan);
        initEventStartDate();
    }

    @Override
    protected void initEventStartDate() {
        if (getStudentCurricularPlan() != null) {
            setEventStartDate(getStudentCurricularPlan().getRegistration().getStartDate().toLocalDate());
        }
    }

    private void checkParameters(AdministrativeOffice administrativeOffice, StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan == null) {
            throw new DomainException("error.accounting.events.gratuity.GratuityEvent.studentCurricularPlan.cannot.be.null");
        }

        if (administrativeOffice == null) {
            throw new DomainException("error.accounting.events.gratuity.GratuityEvent.administrativeOffice.cannot.be.null");
        }
    }

    @Override
    public Account getToAccount() {
        return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    private Unit getUnit() {
        return getDegree().getUnit();
    }

    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    @Override
    protected LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = super.getDescriptionForEntryType(entryType);
        return labelFormatter
                .appendLabel(" (")
                .appendLabel(getDegree().getSigla())
                .appendLabel(" ")
                .appendLabel(getExecutionYear().getYear()).appendLabel(")");
    }

    @Override
    protected DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
        return getDegreeCurricularPlan().getServiceAgreementTemplate();
    }

    private DegreeCurricularPlan getDegreeCurricularPlan() {
        return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public Registration getRegistration() {
        return getStudentCurricularPlan().getRegistration();
    }

    public boolean isCompleteEnrolmentModel() {
        return getRegistration().isCompleteEnrolmentModel(getExecutionYear());
    }

    public boolean isCustomEnrolmentModel() {
        return getRegistration().isCustomEnrolmentModel(getExecutionYear());
    }

    public double getEnrolmentsEctsForRegistration() {
        return getRegistration().getEnrolmentsEcts(getExecutionYear());
    }

    public int getNumberOfEnrolmentsForRegistration() {
        return getRegistration().getEnrolments(getExecutionYear()).size();
    }

    public boolean canRemoveExemption(final DateTime when) {
        if (hasGratuityExemption()) {
            if (isClosed()) {
                return getPayedAmount().greaterOrEqualThan(calculateTotalAmountToPayWithoutDiscount(when));
            }
        }
        return true;
    }

    public boolean hasGratuityExemption() {
        return getExemptionsSet().stream().anyMatch(exemption -> exemption instanceof GratuityExemption);
    }

    public boolean hasExternalScholarshipGratuityExemption() {
        return hasExemptionsOfType(ExternalScholarshipGratuityExemption.class);
    }

    public boolean hasExemptionsOfType(Class cl) {
        return getExemptionsSet().stream().anyMatch(exemption -> cl.isAssignableFrom(exemption.getClass()));
    }

    public ExternalScholarshipGratuityExemption getExternalScholarshipGratuityExemption() {
        return (ExternalScholarshipGratuityExemption) getExemptionsSet().stream().filter(e->e instanceof ExternalScholarshipGratuityExemption).findFirst().orElse
                (null);
    }

    public GratuityExemption getGratuityExemption() {
        return (GratuityExemption) getExemptionsSet().stream()
                .filter(exemption -> exemption instanceof GratuityExemption)
                .findFirst().orElse(null);
    }

    private Money calculateTotalAmountToPayWithoutDiscount(final DateTime when) {
        return getPostingRule().calculateTotalAmountToPay(this);
    }

    public boolean isGratuityExemptionAvailable() {
        return hasGratuityExemption();
    }

    public boolean isGratuityExemptionNotAvailable() {
        return !hasGratuityExemption();
    }

    public boolean canApplyExemption(final EventExemptionJustificationType justificationType) {
        return true;
    }

    public BigDecimal calculateDiscountPercentage(final Money amount) {
        ExternalScholarshipGratuityExemption scholarship = getExternalScholarshipGratuityExemption();
        GratuityExemption exemption = getGratuityExemption();
        BigDecimal percentage = exemption != null ? exemption.calculateDiscountPercentage(amount) : BigDecimal.ZERO;
        return percentage.add(scholarship != null ? scholarship.calculateDiscountPercentage(amount) : BigDecimal.ZERO);
    }

    @Override
    protected void disconnect() {
        super.setStudentCurricularPlan(null);
        super.disconnect();
    }

    public boolean isGratuityEventWithPaymentPlan() {
        return false;
    }

    @Override
    public boolean isTransferable() { return isOpen() && !hasExternalScholarshipGratuityExemption(); }

    @Override
    protected void internalRecalculateState(DateTime whenRegistered) {
        if (canCloseEvent(whenRegistered)) {
            closeEvent();
        } else {
            if (getCurrentEventState() != EventState.OPEN) {
                changeState(EventState.OPEN, new DateTime());
            }
        }
    }

    @Override
    public boolean isGratuity() {
        return true;
    }

    @Override
    public boolean isToApplyInterest() {
        return true;
    }

    public boolean isDfaGratuityEvent() {
        return false;
    }

    @Override public EntryType getEntryType() {
        return EntryType.GRATUITY_FEE;
    }

}
