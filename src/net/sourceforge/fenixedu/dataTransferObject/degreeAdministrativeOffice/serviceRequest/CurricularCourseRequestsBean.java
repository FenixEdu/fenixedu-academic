package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CurricularCourseRequestsBean implements Serializable {

    private DomainReference<Registration> registration;

    private DomainReference<CurricularCourse> requestedCurricularCourse;

    private DomainReference<CurricularCourse> proposedCurricularCourse;

    private CurricularCourseRequestType type;

    public CurricularCourseRequestsBean(Registration registration) {
	setRegistration(registration);
	setRequestedCurricularCourse(null);
	setProposedCurricularCourse(null);
    }

    public CurricularCourse getRequestedCurricularCourse() {
	return requestedCurricularCourse.getObject();
    }

    public void setRequestedCurricularCourse(CurricularCourse requestedCurricularCourse) {
	this.requestedCurricularCourse = new DomainReference<CurricularCourse>(requestedCurricularCourse);
    }

    public Registration getRegistration() {
	return registration.getObject();
    }

    public void setRegistration(Registration registration) {
	this.registration = new DomainReference<Registration>(registration);
    }

    public CurricularCourse getProposedCurricularCourse() {
	return proposedCurricularCourse.getObject();
    }

    public void setProposedCurricularCourse(CurricularCourse proposedCurricularCourse) {
	this.proposedCurricularCourse = new DomainReference<CurricularCourse>(proposedCurricularCourse);
    }

    public CurricularCourseRequestType getType() {
	return type;
    }

    public void setType(CurricularCourseRequestType type) {
	this.type = type;
    }

    public enum CurricularCourseRequestType {
	CURRICULAR_COURSE_TRADE, CURRICULAR_COURSE_EQUIVALENCE;
    }
}
