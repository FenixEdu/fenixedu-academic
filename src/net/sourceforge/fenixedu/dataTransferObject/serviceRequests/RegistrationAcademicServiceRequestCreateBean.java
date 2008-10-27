package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class RegistrationAcademicServiceRequestCreateBean extends AcademicServiceRequestCreateBean {

    private AcademicServiceRequestType academicServiceRequestType;
    private DomainReference<CurriculumGroup> curriculumGroup;
    private DomainReference<CourseGroup> courseGroup;
    private DomainReference<Enrolment> enrolment;
    private DomainReference<EquivalencePlanRequest> equivalencePlanRequest;
    private String subject;
    private String purpose;

    public RegistrationAcademicServiceRequestCreateBean(final Registration registration) {
	super(registration);
    }

    final public AcademicServiceRequestType getAcademicServiceRequestType() {
	return academicServiceRequestType;
    }

    final public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
	this.academicServiceRequestType = academicServiceRequestType;
    }

    final public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
    }

    final public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    final public CourseGroup getCourseGroup() {
	return (this.courseGroup != null) ? this.courseGroup.getObject() : null;
    }

    final public void setCourseGroup(CourseGroup courseGroup) {
	this.courseGroup = (courseGroup != null) ? new DomainReference<CourseGroup>(courseGroup) : null;
    }

    final public Enrolment getEnrolment() {
	return (this.enrolment != null) ? this.enrolment.getObject() : null;
    }

    final public void setEnrolment(Enrolment enrolment) {
	this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
    }

    final public EquivalencePlanRequest getEquivalencePlanRequest() {
	return (this.equivalencePlanRequest != null) ? this.equivalencePlanRequest.getObject() : null;
    }

    final public void setEquivalencePlanRequest(EquivalencePlanRequest equivalencePlanRequest) {
	this.equivalencePlanRequest = (equivalencePlanRequest != null) ? new DomainReference<EquivalencePlanRequest>(
		equivalencePlanRequest) : null;
    }

    final public String getSubject() {
	return subject;
    }

    final public void setSubject(String subject) {
	this.subject = subject;
    }

    final public String getPurpose() {
	return purpose;
    }

    final public void setPurpose(String purpose) {
	this.purpose = purpose;
    }

}
