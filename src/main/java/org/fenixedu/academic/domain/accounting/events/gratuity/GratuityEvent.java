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
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class GratuityEvent extends GratuityEvent_Base {

    static {

        getRelationGratuityEventStudentCurricularPlan().addListener(new RelationAdapter<StudentCurricularPlan, GratuityEvent>() {
            @Override
            public void beforeAdd(StudentCurricularPlan studentCurricularPlan, GratuityEvent gratuityEvent) {
                if (gratuityEvent != null
                        && studentCurricularPlan != null
                        && studentCurricularPlan.getRegistration().hasGratuityEvent(gratuityEvent.getExecutionYear(),
                                gratuityEvent.getClass())) {
                    throw new DomainException(
                            "error.accounting.events.gratuity.GratuityEvent.person.already.has.gratuity.event.in.registration.and.year");

                }
            }
        });
    }

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
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION).appendLabel(" (")
                .appendLabel(getDegree().getDegreeType().getName().getContent()).appendLabel(" - ")
                .appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent()).appendLabel(" - ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ");
        labelFormatter.appendLabel(getExecutionYear().getYear());
        return labelFormatter;
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
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof GratuityExemption) {
                return true;
            }
        }

        return false;
    }

    public boolean hasExternalScholarshipGratuityExemption() {
        return getExemptionsSet().stream().anyMatch(e->e instanceof ExternalScholarshipGratuityExemption);
    }

    public ExternalScholarshipGratuityExemption getExternalScholarshipGratuityExemption() {
        return (ExternalScholarshipGratuityExemption) getExemptionsSet().stream().filter(e->e instanceof ExternalScholarshipGratuityExemption).findFirst().orElse
                (null);
    }

    public GratuityExemption getGratuityExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof GratuityExemption) {
                return (GratuityExemption) exemption;
            }
        }

        return null;
    }

    private Money calculateTotalAmountToPayWithoutDiscount(final DateTime when) {
        return getPostingRule().calculateTotalAmountToPay(this, when, false);
    }

    public boolean isGratuityExemptionAvailable() {
        return hasGratuityExemption();
    }

    public boolean isGratuityExemptionNotAvailable() {
        return !hasGratuityExemption();
    }

    public boolean canApplyExemption(final GratuityExemptionJustificationType justificationType) {
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
    public boolean isOpen() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).greaterThan(Money.ZERO);
    }

    @Override
    public boolean isClosed() {
        if (isCancelled()) {
            return false;
        }

        return calculateAmountToPay(new DateTime()).lessOrEqualThan(Money.ZERO);
    }


    @Override
    public boolean isTransferable() { return isOpen() && !hasExternalScholarshipGratuityExemption(); }

    @Override
    public boolean isInState(final EventState eventState) {
        if (eventState == EventState.OPEN) {
            return isOpen();
        } else if (eventState == EventState.CLOSED) {
            return isClosed();
        } else if (eventState == EventState.CANCELLED) {
            return isCancelled();
        } else {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent.unexpected.state.to.test");
        }
    }

    @Override
    protected void internalRecalculateState(DateTime whenRegistered) {
        if (canCloseEvent(whenRegistered)) {
            closeNonProcessedCodes();
            closeEvent();
        } else {
            if (getCurrentEventState() != EventState.OPEN) {
                changeState(EventState.OPEN, new DateTime());
                reopenCancelledCodes();
            }
        }
    }

    @Override
    public boolean isGratuity() {
        return true;
    }

    public boolean isDfaGratuityEvent() {
        return false;
    }

}
