package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class RegistrationAcademicServiceRequestCreateBean extends RegistrationSelectExecutionYearBean {
    
    private AcademicServiceRequestType academicServiceRequestType;
    private DomainReference<CurriculumGroup> curriculumGroup;
    private DomainReference<CourseGroup> courseGroup;
    private DomainReference<Enrolment> enrolment;
    private String purpose;
    
    public RegistrationAcademicServiceRequestCreateBean(final Registration registration) {
	super(registration);
    }
    
    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return academicServiceRequestType;
    }

    public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
        this.academicServiceRequestType = academicServiceRequestType;
    }

    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup != null) ? this.curriculumGroup.getObject() : null;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }
    
    public CourseGroup getCourseGroup() {
	return (this.courseGroup != null) ? this.courseGroup.getObject() : null;
    }

    public void setCourseGroup(CourseGroup courseGroup) {
	this.courseGroup = (courseGroup != null) ? new DomainReference<CourseGroup>(courseGroup) : null;
    }
    
    public Enrolment getEnrolment() {
	return (this.enrolment != null) ? this.enrolment.getObject() : null;
    }

    public void setEnrolment(Enrolment enrolment) {
	this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
}