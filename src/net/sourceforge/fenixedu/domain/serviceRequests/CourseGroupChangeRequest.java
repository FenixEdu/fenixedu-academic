package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

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

    @Override
    public void setNewCourseGroup(CourseGroup newCourseGroup) {
	throw new DomainException("error.CourseGroupChangeRequest.cannot.modify.newCourseGroup");
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
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }
}
