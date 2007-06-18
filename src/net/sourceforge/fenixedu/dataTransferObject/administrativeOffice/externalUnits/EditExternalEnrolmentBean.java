package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class EditExternalEnrolmentBean extends CreateExternalEnrolmentBean {

    private DomainReference<ExternalEnrolment> externalEnrolment;
    
    public EditExternalEnrolmentBean(final ExternalEnrolment externalEnrolment) {
	setExternalCurricularCourse(externalEnrolment.getExternalCurricularCourse());
	setExecutionPeriod(externalEnrolment.getExecutionPeriod());
	setStudentNumber(externalEnrolment.getStudent().getNumber());
	setGrade(externalEnrolment.getGrade());
	setEvaluationDate(externalEnrolment.getEvaluationDate());
	setExternalEnrolment(externalEnrolment);
    }
    
    public ExternalEnrolment getExternalEnrolment() {
	return (this.externalEnrolment != null) ? this.externalEnrolment.getObject() : null;
    }

    public void setExternalEnrolment(ExternalEnrolment externalEnrolment) {
	this.externalEnrolment = (externalEnrolment != null) ? new DomainReference<ExternalEnrolment>(externalEnrolment) : null;
    }
    
}
