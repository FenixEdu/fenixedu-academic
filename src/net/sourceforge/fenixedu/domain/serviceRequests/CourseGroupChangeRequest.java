package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.lang.StringUtils;

public class CourseGroupChangeRequest extends CourseGroupChangeRequest_Base {

	protected CourseGroupChangeRequest() {
		super();
	}

	public CourseGroupChangeRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
		this();
		super.init(bean);

		checkParameters(bean);
		super.setOldCourseGroup(bean.getCurriculumGroup().getDegreeModule());
		super.setNewCourseGroup(bean.getCourseGroup());
	}

	private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
		final CurriculumGroup curriculumGroup = bean.getCurriculumGroup();
		final CourseGroup newCourseGroup = bean.getCourseGroup();
		final ExecutionYear executionYear = bean.getExecutionYear();
		final Registration registration = bean.getRegistration();

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
	protected void disconnect() {
		super.setOldCourseGroup(null);
		super.setNewCourseGroup(null);
		super.disconnect();
	}

	@Override
	protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
		super.createAcademicServiceRequestSituations(academicServiceRequestBean);

		if (academicServiceRequestBean.isToConclude()) {
			AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
					AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getResponsible()));
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
	public boolean isToPrint() {
		return false;
	}

	@Override
	public boolean isPossibleToSendToOtherEntity() {
		return true;
	}

	@Override
	public boolean isManagedWithRectorateSubmissionBatch() {
		return false;
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
