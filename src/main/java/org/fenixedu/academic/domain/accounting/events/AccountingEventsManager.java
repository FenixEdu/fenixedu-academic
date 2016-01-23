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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.events.gratuity.DfaGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.accounting.events.gratuity.SpecializationDegreeGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
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

        if (hasStandaloneCurriculumGroupAndEnrolmentsFor(studentCurricularPlan, executionYear)
                && !studentCurricularPlan.hasGratuityEvent(executionYear, StandaloneEnrolmentGratuityEvent.class)) {

            new StandaloneEnrolmentGratuityEvent(getAdministrativeOffice(studentCurricularPlan),
                    studentCurricularPlan.getPerson(), studentCurricularPlan, executionYear);

            return InvocationResult.createSuccess();

        }

        final InvocationResult result = InvocationResult.createInsuccess();
        result.addMessage(
                Bundle.APPLICATION,
                "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.standalone.gratuity.event");

        return result;

    }

    private boolean hasStandaloneCurriculumGroupAndEnrolmentsFor(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        return studentCurricularPlan.hasStandaloneCurriculumGroup()
                && studentCurricularPlan.getStandaloneCurriculumGroup().hasEnrolment(executionYear);
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

        return createGratuityEventWithPaymentPlan(studentCurricularPlan, executionYear, checkConditions);

    }

    private InvocationResult createDfaGratuityEvent(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear,
            boolean checkConditions) {

        final InvocationResult result =
                checkConditions ? verifyConditionsToCreateGratuityEvent(executionYear, studentCurricularPlan) : InvocationResult
                        .createSuccess();

        if (result.isSuccess()) {

            if (studentCurricularPlan.getRegistration().hasGratuityEvent(executionYear, DfaGratuityEvent.class)) {
                result.addMessage(Bundle.APPLICATION,
                        "error.accounting.events.AccountingEventsManager.student.already.has.gratuity.event.for.execution.year",
                        studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                                .getRegistration().getDegree().getPresentationName(), executionYear.getYear());

                result.setSuccess(false);

                return result;

            }

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
            if (studentCurricularPlan.getRegistration().hasGratuityEvent(executionYear, SpecializationDegreeGratuityEvent.class)) {
                result.addMessage(Bundle.APPLICATION,
                        studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                                .getRegistration().getDegree().getPresentationName(), executionYear.getYear());

                result.setSuccess(false);

                return result;
            }

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

            if (studentCurricularPlan.getRegistration().hasGratuityEvent(executionYear, GratuityEventWithPaymentPlan.class)) {
                result.addMessage(Bundle.APPLICATION,
                        "error.accounting.events.AccountingEventsManager.student.already.has.gratuity.event.for.execution.year",
                        studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
                                .getRegistration().getDegree().getPresentationName(), executionYear.getYear());

                result.setSuccess(false);

                return result;

            }

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

            if (student.getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
                result.addMessage(
                        Bundle.APPLICATION,
                        "error.accounting.events.AccountingEventsManager.student.already.has.administrativeoffice.fee.and.insurance.event.for.year",
                        student.getNumber().toString(), executionYear.getYear());

                result.setSuccess(false);

                return result;

            }

            new AdministrativeOfficeFeeAndInsuranceEvent(getAdministrativeOffice(studentCurricularPlan),
                    studentCurricularPlan.getPerson(), executionYear);
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

            final Student student = studentCurricularPlan.getRegistration().getStudent();

            if (student.getPerson().hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
                result.addMessage(Bundle.APPLICATION,
                        "error.accounting.events.AccountingEventsManager.student.already.has.insurance.event.for.year", student
                                .getNumber().toString(), executionYear.getYear());

                result.setSuccess(false);

                return result;

            }

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

            if (person.hasInsuranceEventOrAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
                result.addMessage(Bundle.APPLICATION,
                        "error.accounting.events.AccountingEventsManager.student.already.has.insurance.event.for.year", person
                                .getStudent().getNumber().toString(), executionYear.getYear());

                result.setSuccess(false);

                return result;

            }

            new InsuranceEvent(person, executionYear);
        }

        return result;
    }
}
