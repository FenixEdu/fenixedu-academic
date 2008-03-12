package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class CourseGroupChangeRequest extends CourseGroupChangeRequest_Base {

    protected CourseGroupChangeRequest() {
	super();
    }

    public CourseGroupChangeRequest(final Registration registration, final CurriculumGroup curriculumGroup,
	    final CourseGroup newCourseGroup, final ExecutionYear executionYear, final DateTime requestDate) {
	this(registration, curriculumGroup, newCourseGroup, executionYear, requestDate, false, false);
    }

    public CourseGroupChangeRequest(final Registration registration, final CurriculumGroup curriculumGroup,
	    final CourseGroup newCourseGroup, final ExecutionYear executionYear, final DateTime requestDate,
	    final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);
	checkParameters(registration, curriculumGroup, newCourseGroup, executionYear);
	super.setOldCourseGroup(curriculumGroup.getDegreeModule());
	super.setNewCourseGroup(newCourseGroup);
    }

    private void checkParameters(final Registration registration, final CurriculumGroup curriculumGroup,
	    final CourseGroup newCourseGroup, final ExecutionYear executionYear) {
	if (curriculumGroup == null) {
	    throw new DomainException("error.CourseGroupChangeRequest.curriculumGroup.cannot.be.null");
	}

	if (newCourseGroup == null) {
	    throw new DomainException("error.CourseGroupChangeRequest.newCourseGroup.cannot.be.null");
	}

	if (executionYear == null) {
	    throw new DomainException("error.CourseGroupChangeRequest.executionYear.cannot.be.null");
	}

	if (!registration.getLastStudentCurricularPlan().hasCurriculumModule(curriculumGroup)) {
	    throw new DomainException("error.CourseGroupChangeRequest.invalid.curriculumGroup");
	}

	if (!registration.getLastDegreeCurricularPlan().hasDegreeModule(newCourseGroup)) {
	    throw new DomainException("error.CourseGroupChangeRequest.invalid.newCourseGroup");
	}
    }

    @Override
    public void setOldCourseGroup(CourseGroup oldCourseGroup) {
	throw new DomainException("error.CourseGroupChangeRequest.cannot.modify.oldCourseGroup");
    }

    public String getOldCourseGroupOneFullName() {
	return hasOldCourseGroup() ? getOldCourseGroup().getOneFullName() : StringUtils.EMPTY;
    }

    @Override
    public void setNewCourseGroup(CourseGroup newCourseGroup) {
	throw new DomainException("error.CourseGroupChangeRequest.cannot.modify.newCourseGroup");
    }

    public String getNewCourseGroupOneFullName() {
	return hasNewCourseGroup() ? getNewCourseGroup().getOneFullName() : StringUtils.EMPTY;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.COURSE_GROUP_CHANGE_REQUEST;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public void delete() {
	super.setOldCourseGroup(null);
	super.setNewCourseGroup(null);
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

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

}
