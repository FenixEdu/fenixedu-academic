package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.InvocationResult;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class AccountingEventsManager {

    private final List<DegreeType> acceptedDegreeTypes = Arrays.asList(new DegreeType[] { DegreeType.BOLONHA_DEGREE,
	    DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE });

    public InvocationResult createGratuityEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	return createGratuityEvent(studentCurricularPlan, executionYear, true);
    }

    public InvocationResult createGratuityEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear, final boolean checkConditions) {

	final InvocationResult result = checkConditions ? verifyConditionsToCreateGratuityEvent(executionYear,
		studentCurricularPlan) : InvocationResult.createSuccess();

	if (result.isSuccess()) {
	    new GratuityEventWithPaymentPlan(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson(),
		    studentCurricularPlan, executionYear);
	}
	return result;
    }

    public InvocationResult createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear) {
	return createAdministrativeOfficeFeeAndInsuranceEvent(studentCurricularPlan, executionYear, true);
    }

    public InvocationResult createAdministrativeOfficeFeeAndInsuranceEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionYear executionYear, final boolean checkConditions) {
	final InvocationResult result = checkConditions ? verifyConditionsToCreateAdministrativeOfficeFeeAndInsuranceEvent(
		studentCurricularPlan, executionYear) : InvocationResult.createSuccess();
	if (result.isSuccess()) {
	    new AdministrativeOfficeFeeAndInsuranceEvent(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan
		    .getPerson(), executionYear);
	}
	return result;
    }

    public void createEnrolmentOutOfPeriodEvent(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester, final Integer numberOfDelayDays) {
	new EnrolmentOutOfPeriodEvent(getAdministrativeOffice(studentCurricularPlan), studentCurricularPlan.getPerson(),
		studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    private AdministrativeOffice getAdministrativeOffice(final StudentCurricularPlan studentCurricularPlan) {
	return AdministrativeOffice.readByAdministrativeOfficeType(studentCurricularPlan.getDegreeType()
		.getAdministrativeOfficeType());
    }

    private InvocationResult verifyConditionsToCreateAdministrativeOfficeFeeAndInsuranceEvent(
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {

	final InvocationResult result = new InvocationResult().setSuccess(false);

	final Registration registration = studentCurricularPlan.getRegistration();

	final Student student = registration.getStudent();

	if (verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(executionYear, studentCurricularPlan,
		registration)) {
	    if (!acceptedDegreeTypes.contains(studentCurricularPlan.getDegreeType())) {
		result
			.addMessage(
				LabelFormatter.APPLICATION_RESOURCES,
				"error.accounting.events.AccountingEventsManager.cannot.create.administrativeoffice.fee.and.insurance.event.for.degree.type",
				studentCurricularPlan.getDegree().getPresentationName());

		return result;
	    }

	    if (student.getPerson().hasAdministrativeOfficeFeeInsuranceEventFor(executionYear)) {
		result
			.addMessage(
				LabelFormatter.APPLICATION_RESOURCES,
				"error.accounting.events.AccountingEventsManager.student.already.has.administrativeoffice.fee.and.insurance.event.for.year",
				student.getNumber().toString(), executionYear.getYear());

		return result;

	    }

	    result.setSuccess(true);

	} else {
	    result
		    .addMessage(
			    LabelFormatter.APPLICATION_RESOURCES,
			    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.administrativeoffice.fee.and.insurance.event",
			    studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
				    .getDegree().getPresentationName());
	}

	return result;

    }

    private InvocationResult verifyConditionsToCreateGratuityEvent(final ExecutionYear executionYear,
	    final StudentCurricularPlan studentCurricularPlan) {

	final InvocationResult result = new InvocationResult().setSuccess(false);
	final Registration registration = studentCurricularPlan.getRegistration();

	if (verifyCommonConditionsToCreateGratuityAndAdministrativeOfficeEvents(executionYear, studentCurricularPlan,
		registration)) {

	    if (!acceptedDegreeTypes.contains(studentCurricularPlan.getDegreeType())) {
		result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
			"error.accounting.events.AccountingEventsManager.cannot.create.gratuity.event.for.degree.type",
			studentCurricularPlan.getDegree().getPresentationName());
		return result;
	    }

	    if (studentCurricularPlan.getRegistration().hasGratuityEvent(executionYear)) {
		result.addMessage(LabelFormatter.APPLICATION_RESOURCES,
			"error.accounting.events.AccountingEventsManager.student.already.has.gratuity.event.for.execution.year",
			studentCurricularPlan.getRegistration().getStudent().getNumber().toString(), studentCurricularPlan
				.getRegistration().getDegree().getPresentationName(), executionYear.getYear());
		return result;

	    }

	    result.setSuccess(true);

	} else {
	    result
		    .addMessage(
			    LabelFormatter.APPLICATION_RESOURCES,
			    "error.accounting.events.AccountingEventsManager.registration.for.student.does.not.respect.requirements.to.create.gratuity.event",
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

}
