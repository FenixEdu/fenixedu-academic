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
package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.SpecializationDegreeGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.InvocationResult;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AccountingEventsManager {

    private final List<DegreeType> acceptedDegreeTypesForAdministrativeOfficeFeeAndInsuranceEvent = Arrays
            .asList(new DegreeType[] { DegreeType.DEGREE, DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE,
                    DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE, DegreeType.EMPTY });

    private final List<DegreeType> acceptedDegreeTypesForGratuityEvent = Arrays.asList(new DegreeType[] { DegreeType.DEGREE,
            DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
            DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA, DegreeType.BOLONHA_SPECIALIZATION_DEGREE });

    private final List<DegreeType> acceptedDegreeTypesForInsuranceEvent = Arrays.asList(new DegreeType[] {
            DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA, DegreeType.BOLONHA_SPECIALIZATION_DEGREE });

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
                LabelFormatter.APPLICATION_RESOURCES,
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

        if (studentCurricularPlan.getDegreeType() == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
            return createDfaGratuityEvent(studentCurricularPlan, executionYear, checkConditions);
        } else if (studentCurricularPlan.getDegreeType() == DegreeType.BOLONHA_SPECIALIZATION_DEGREE) {
            return createSpecializationDegreeGratuityEvent(studentCurricularPlan, executionYear, checkConditions);
        } else if (studentCurricularPlan.getDegreeType() == DegreeType.EMPTY) {
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
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
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
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES, studentCurricularPlan.getRegistration().getStudent()
                        .getNumber().toString(), studentCurricularPlan.getRegistration().getDegree().getPresentationName(),
                        executionYear.getYear());

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
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
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

            if (!acceptedDegreeTypesForGratuityEvent.contains(studentCurricularPlan.getDegreeType())) {
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
                        "error.accounting.events.AccountingEventsManager.cannot.create.gratuity.event.for.degree.type",
                        studentCurricularPlan.getDegree().getPresentationName());
                return result;
            }

            result.setSuccess(true);

        } else {
            result.addMessage(
                    LabelFormatter.APPLICATION_RESOURCES,
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
                        LabelFormatter.APPLICATION_RESOURCES,
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
            if (!acceptedDegreeTypesForAdministrativeOfficeFeeAndInsuranceEvent.contains(studentCurricularPlan.getDegreeType())) {
                result.addMessage(
                        LabelFormatter.APPLICATION_RESOURCES,
                        "error.accounting.events.AccountingEventsManager.cannot.create.administrativeoffice.fee.and.insurance.event.for.degree.type",
                        studentCurricularPlan.getDegree().getPresentationName());

                return result;
            }

            result.setSuccess(true);

        } else {
            result.addMessage(
                    LabelFormatter.APPLICATION_RESOURCES,
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
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
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
            if (!acceptedDegreeTypesForInsuranceEvent.contains(studentCurricularPlan.getDegreeType())) {
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
                        "error.accounting.events.AccountingEventsManager.cannot.create.insurance.event.for.degree.type",
                        studentCurricularPlan.getDegree().getPresentationName());

                return result;
            }

            result.setSuccess(true);

        } else {
            result.addMessage(
                    LabelFormatter.APPLICATION_RESOURCES,
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
                result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
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
