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
package org.fenixedu.academic.domain.accounting.events;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.EnrolmentGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.accounting.events.gratuity.PartialRegimeEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.SpecializationDegreeGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.IsAlienRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.InvocationResult;

import pt.ist.fenixframework.Atomic;

public class AccountingEventsManager {

    public InvocationResult createStandaloneEnrolmentGratuityEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {

        InvocationResult result = InvocationResult.createInsuccess();

        final Set<Enrolment> standaloneCurriculumGroupAndEnrolmentsFor = getStandaloneEnrolments(studentCurricularPlan,
                executionYear);

        IsAlienRule isAlienRule = new IsAlienRule();

        if (standaloneCurriculumGroupAndEnrolmentsFor.isEmpty()) {
            result.addMessage(
                    Bundle.APPLICATION,
                    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.standalone.gratuity.event");
        } else {
            for (Enrolment enrolment : standaloneCurriculumGroupAndEnrolmentsFor) {
                EnrolmentGratuityEvent.create(studentCurricularPlan.getPerson(), enrolment, EventType
                        .STANDALONE_PER_ENROLMENT_GRATUITY, isAlienRule.isAppliableFor(studentCurricularPlan, executionYear));
            }
            result =  InvocationResult.createSuccess();
        }
        
        return result;
    }


    public InvocationResult createStandaloneEnrolmentGratuityEvent(final Enrolment enrolment) {
        IsAlienRule isAlienRule = new IsAlienRule();
        EnrolmentGratuityEvent.create(enrolment.getPerson(), enrolment, EventType.STANDALONE_PER_ENROLMENT_GRATUITY,
                isAlienRule.isAppliableFor(enrolment.getStudentCurricularPlan(), enrolment.getExecutionYear()));
        return InvocationResult.createSuccess();
    }

    private Set<Enrolment> getStandaloneEnrolments(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {

        if (!studentCurricularPlan.hasStandaloneCurriculumGroup()) {
            return Collections.emptySet();
        }

        return studentCurricularPlan.getStandaloneCurriculumGroup().getEnrolmentsBy(executionYear);
    }

    public InvocationResult createGratuityEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        return createGratuityEvent(studentCurricularPlan, executionYear, true);
    }

    public InvocationResult createGratuityEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear, final boolean checkConditions) {

        if (studentCurricularPlan.getDegreeCurricularPlan().isPast()) {
            /*
             * TODO: To create events in past dcps its necessary to change
             * method args to add ammount and create PastDegreeGratuityEvent.
             * It's easier to create debts for past students by script.
             */
            throw new DomainException("error.AccountingEventsManager.invalid.degree.curricular.plan.type");
        }

        if (studentCurricularPlan.getDegreeType().isAdvancedFormationDiploma()) {
            return createDfaGratuityEvent(studentCurricularPlan, executionYear, checkConditions);
        } else if (studentCurricularPlan.getDegreeType().isSpecializationDegree()) {
            return createSpecializationDegreeGratuityEvent(studentCurricularPlan, executionYear, checkConditions);
        } else if (studentCurricularPlan.getDegreeType().isEmpty()) {
            return createStandaloneEnrolmentGratuityEvent(studentCurricularPlan, executionYear);
        }

        return PartialRegimeEvent.create(studentCurricularPlan.getRegistration(), executionYear).map
                (p -> InvocationResult.createSuccess()).orElseGet(() -> createGratuityEventWithPaymentPlan(studentCurricularPlan, executionYear, checkConditions));
    }

    private InvocationResult createDfaGratuityEvent(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear,
            boolean checkConditions) {

        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateGratuityEvent(executionYear, studentCurricularPlan) : InvocationResult
                        .createSuccess();

        if (result.isSuccess()) {
            new DfaGratuityEvent(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson(),
                    studentCurricularPlan, executionYear);
        }

        return result;

    }

    private InvocationResult createSpecializationDegreeGratuityEvent(StudentCurricularPlan studentCurricularPlan,
            ExecutionYear executionYear, boolean checkConditions) {

        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateGratuityEvent(executionYear, studentCurricularPlan) : InvocationResult
                        .createSuccess();

        if (result.isSuccess()) {
            new SpecializationDegreeGratuityEvent(getAdministrativeOffice(studentCurricularPlan),
                    studentCurricularPlan.getPerson(), studentCurricularPlan, executionYear);
        }

        return result;
    }

    private InvocationResult createGratuityEventWithPaymentPlan(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear, final boolean checkConditions) {

        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateGratuityEvent(executionYear, studentCurricularPlan) : InvocationResult
                        .createSuccess();

        if (result.isSuccess()) {
            new GratuityEventWithPaymentPlan(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson(),
                    studentCurricularPlan, executionYear);
        }

        return result;
    }

    private InvocationResult verifyConditionsToCreateGratuityEvent(final ExecutionYear executionYear,
            final StudentCurricularPlan studentCurricularPlan) {

        final InvocationResult result = new InvocationResult().setSuccess(false);
        final Registration registration = studentCurricularPlan.getRegistration();

        if (verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(executionYear, studentCurricularPlan,
                registration) && studentCurricularPlan.getDegree().canCreateGratuityEvent()) {

            result.setSuccess(true);

        } else {
            result.addMessage(
                    Bundle.APPLICATION,
                    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.gratuity.event",
                    studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                            .getDegree().getPresentationName());

        }

        return result;

    }

    public InvocationResult createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        return createAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan, executionYear, true);
    }

    public InvocationResult createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear, final boolean checkConditions) {

        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan,
                        executionYear) : InvocationResult.createSuccess();

        if (result.isSuccess()) {

            final Student student = studentCurricularPlan.getRegistration().getStudent();

              new AdministrativeOfficeFeeEvent(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson
                      (), executionYear);
              new InsuranceEvent(studentCurricularPlan.getPerson(), executionYear);
//            new AdministrativeOfficeFeeAndInsuranceEvent(getAdministrativeOffice(studentCurricularPlan),
//                    studentCurricularPlan.getPerson(), executionYear);
        }

        return result;
    }

    public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
        new EnrolmentOutOfPeriodEvent(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson(),
                studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    private AdministrativeOffice getAdministrativeOffice(final StudentCurricularPlan studentCurricularPlan) {
        return studentCurricularPlan.getDegree().getAdministrativeOffice();
    }

    private InvocationResult verifyConditionsToCreateAdministrativeOfficeFeeAndInsuranceEvent(
            final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {

        final InvocationResult result = new InvocationResult().setSuccess(false);

        final Registration registration = studentCurricularPlan.getRegistration();

        if (verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(executionYear, studentCurricularPlan,
                registration)) {
            result.setSuccess(true);

        } else {
            result.addMessage(
                    Bundle.APPLICATION,
                    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.administrativeoffice.fee.and.insurance.event",
                    studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                            .getDegree().getPresentationName());
        }

        return result;

    }

    private boolean verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(final ExecutionYear executionYear,
            final StudentCurricularPlan studentCurricularPlan, final Registration registration) {
        return registration.hasToPayGratuityOrInsurance()
                && registration.isActive()
                && studentCurricularPlan.getDegreeCurricularPlan().hasExecutionDegreeFor(executionYear)
                && (registration.isInMobilityState() || registration.hasAnyEnrolmentsIn(executionYear) || isSecondCycleInternalCandidacyAndStartedOn(
                        registration, executionYear));

    }

    private boolean isSecondCycleInternalCandidacyAndStartedOn(Registration registration, ExecutionYear executionYear) {
        return registration.isSecondCycleInternalCandidacyIngression()
                && executionYear.containsDate(registration.getStartDate().toDateTimeAtMidnight());
    }

    public InvocationResult createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        return createInsuranceEvent(studentCurricularPlan, executionYear, true);
    }

    public InvocationResult createInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear, final boolean checkConditions) {
        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateInsuranceEvent(studentCurricularPlan, executionYear) : InvocationResult
                        .createSuccess();

        if (result.isSuccess()) {
            new InsuranceEvent(studentCurricularPlan.getPerson(), executionYear);
        }

        return result;
    }

    private InvocationResult verifyConditionsToCreateInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {

        final InvocationResult result = new InvocationResult().setSuccess(false);
        final Registration registration = studentCurricularPlan.getRegistration();

        if (verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(executionYear, studentCurricularPlan,
                registration)) {

            result.setSuccess(true);

        } else {
            result.addMessage(
                    Bundle.APPLICATION,
                    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.insurance.event",
                    studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                            .getDegree().getPresentationName());
        }

        return result;

    }

    @Atomic
    public InvocationResult createInsuranceEvent(final Person person, final ExecutionYear executionYear) {
        final InvocationResult result = InvocationResult.createSuccess();
        if (result.isSuccess()) {
            new InsuranceEvent(person, executionYear);
        }
        return result;
    }

    @Atomic
    public InvocationResult createAdministrativeOfficeFeeEvent(final Person person, final ExecutionYear executionYear,
                                                               final AdministrativeOffice administrativeOffice) {
        final InvocationResult result = InvocationResult.createSuccess();
        if (result.isSuccess()) {
            new AdministrativeOfficeFeeEvent(administrativeOffice, person, executionYear);
        }
        return result;
    }
}
