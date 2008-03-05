package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.student.StudentStatuteBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class ExtraExamRequest extends ExtraExamRequest_Base {

    private static List<StudentStatuteType> acceptedStatutes = Arrays.asList(StudentStatuteType.ASSOCIATIVE_LEADER);

    protected ExtraExamRequest() {
	super();
    }

    public ExtraExamRequest(final Registration registration, final Enrolment enrolment, final ExecutionYear executionYear,
	    final DateTime requestDate) {
	this(registration, enrolment, executionYear, requestDate, false, false);
    }

    public ExtraExamRequest(final Registration registration, final Enrolment enrolment, final ExecutionYear executionYear,
	    final DateTime requestDate, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);
	checkParameters(registration, enrolment, executionYear);
	super.setEnrolment(enrolment);
    }

    private void checkParameters(final Registration registration, final Enrolment enrolment, final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException("error.ExtraExamRequest.executionYear.cannot.be.null");
	}

	if (!registration.hasEnrolments(enrolment)) {
	    throw new DomainException("error.ExtraExamRequest.registration.doesnot.have.enrolment");
	}

	if (!studentHasValidStatutes(registration, enrolment)) {
	    throw new DomainException("error.ExtraExamRequest.registration.doesnot.have.valid.statutes");
	}
    }

    private boolean studentHasValidStatutes(final Registration registration, final Enrolment enrolment) {
	final Student student = registration.getStudent();
	for (final StudentStatuteBean bean : student.getStatutes(enrolment.getExecutionPeriod())) {
	    if (acceptedStatutes.contains(bean.getStatuteType())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void setEnrolment(Enrolment enrolment) {
	throw new DomainException("error.ExtraExamRequest.cannot.modify.enrolment");
    }

    public String getEnrolmentName() {
	return hasEnrolment() ? getEnrolment().getName().getContent() : StringUtils.EMPTY;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.EXTRA_EXAM_REQUEST;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public void delete() {
	super.setEnrolment(null);
	super.delete();
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
	super.createAcademicServiceRequestSituations(academicServiceRequestBean);

	if (academicServiceRequestBean.isToConclude()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getEmployee()));
	}
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        
	if (academicServiceRequestBean.isToProcess()) {
	    academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
	}
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }
}
