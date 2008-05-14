package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;
import dml.runtime.RelationAdapter;

public class EnrolmentOutOfPeriodEvent extends EnrolmentOutOfPeriodEvent_Base {

    static {
	StudentCurricularPlan.EnrolmentOutOfPeriodEventStudentCurricularPlan
		.addListener(new RelationAdapter<StudentCurricularPlan, EnrolmentOutOfPeriodEvent>() {
		    @Override
		    public void beforeAdd(StudentCurricularPlan studentCurricularPlan,
			    EnrolmentOutOfPeriodEvent enrolmentOutOfPeriodEvent) {
			if (studentCurricularPlan != null && enrolmentOutOfPeriodEvent != null) {
			    final Registration registration = studentCurricularPlan.getRegistration();
			    if (registration.containsEnrolmentOutOfPeriodEventFor(enrolmentOutOfPeriodEvent.getExecutionPeriod())) {
				throw new DomainException(
					"error.accounting.events.EnrolmentOutOfPeriodEvent.registration.already.contains.enrolment.out.of.period.event.for.period",
					studentCurricularPlan.getRegistration().getStudent().getNumber().toString(),
					studentCurricularPlan.getRegistration().getDegree().getPresentationName(),
					enrolmentOutOfPeriodEvent.getExecutionPeriod().getExecutionYear().getYear(),
					enrolmentOutOfPeriodEvent.getExecutionPeriod().getSemester().toString());
			    }

			}
		    }

		});

    }

    protected EnrolmentOutOfPeriodEvent() {
	super();
    }

    public EnrolmentOutOfPeriodEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
	    final Integer numberOfDelayDays) {
	this();
	init(administrativeOffice, person, studentCurricularPlan, executionSemester, numberOfDelayDays);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, StudentCurricularPlan studentCurricularPlan,
	    ExecutionSemester executionSemester, Integer numberOfDelayDays) {
	checkParameters(administrativeOffice, studentCurricularPlan, executionSemester, numberOfDelayDays);
	super.init(administrativeOffice, EventType.ENROLMENT_OUT_OF_PERIOD, person);
	super.setExecutionPeriod(executionSemester);
	super.setStudentCurricularPlan(studentCurricularPlan);
	super.setNumberOfDelayDays(numberOfDelayDays);
    }

    private void checkParameters(AdministrativeOffice administrativeOffice, StudentCurricularPlan studentCurricularPlan,
	    ExecutionSemester executionSemester, Integer numberOfDelayDays) {

	if (administrativeOffice == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent.administrativeOffice.cannot.be.null");
	}

	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent.studentCurricularPlan.cannot.be.null");
	}

	if (executionSemester == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent.executionPeriod.cannot.be.null");
	}

	if (numberOfDelayDays == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.events.EnrolmentOutOfPeriodEvent.numberOfDelayDays.cannot.be.null");
	}

    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter().appendLabel(entryType.name(),
		LabelFormatter.ENUMERATION_RESOURCES);
	addCommonDescription(labelFormatter);

	return labelFormatter;
    }

    @Override
    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = super.getDescription();
	addCommonDescription(labelFormatter);

	return labelFormatter;
    }

    private void addCommonDescription(final LabelFormatter labelFormatter) {
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" - ");
	labelFormatter.appendLabel(getDegree().getName());
	labelFormatter.appendLabel(" - ");
	labelFormatter.appendLabel(getExecutionPeriod().getSemester().toString());
	labelFormatter.appendLabel("label.semester", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel("  " + getExecutionPeriod().getYear());
	labelFormatter.appendLabel(")");
    }

    private Degree getDegree() {
	return getStudentCurricularPlan().getDegree();
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
		getWhenOccured());
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }

}
