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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES).appendLabel(" (")
                .appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES).appendLabel(" - ")
                .appendLabel(getDegree().getNameFor(getExecutionYear()).getContent()).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES).appendLabel(" - ");
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

    @Override
    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setStudentCurricularPlan(studentCurricularPlan);
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
        return hasGratuityExemption() ? getGratuityExemption().calculateDiscountPercentage(amount) : BigDecimal.ZERO;
    }

    @Override
    protected void disconnect() {
        check(this, RolePredicates.MANAGER_PREDICATE);
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
    public boolean isInState(final EventState eventState) {
        if (eventState == EventState.OPEN) {
            return isOpen();
        } else if (eventState == EventState.CLOSED) {
            return isClosed();
        } else if (eventState == EventState.CANCELLED) {
            return isCancelled();
        } else {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent.unexpected.state.to.test");
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

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

}
