package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExternalCurricularCourse;

public class EditExternalCurricularCourseBean extends CreateExternalCurricularCourseBean {

    private DomainReference<ExternalCurricularCourse> externalCurricularCourse;
    
    public EditExternalCurricularCourseBean(final ExternalCurricularCourse externalCurricularCourse) {
	setParentUnit(externalCurricularCourse.getUnit());
	setName(externalCurricularCourse.getName());
	setCode(externalCurricularCourse.getCode());
	setEnrolStudent(false);
	setExternalEnrolmentBean(null);
	
	setExternalCurricularCourse(externalCurricularCourse);
    }
    
    public ExternalCurricularCourse getExternalCurricularCourse() {
	return (this.externalCurricularCourse != null) ? this.externalCurricularCourse.getObject() : null;
    }

    public void setExternalCurricularCourse(final ExternalCurricularCourse externalCurricularCourse) {
	this.externalCurricularCourse = (externalCurricularCourse != null) ? new DomainReference<ExternalCurricularCourse>(externalCurricularCourse) : null;
    }
    
}
