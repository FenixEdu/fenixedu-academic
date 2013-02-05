package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits;

import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class EditExternalEnrolmentBean extends CreateExternalEnrolmentBean {

    private ExternalEnrolment externalEnrolment;

    public EditExternalEnrolmentBean(final ExternalEnrolment externalEnrolment) {
        setExternalCurricularCourse(externalEnrolment.getExternalCurricularCourse());
        setExecutionPeriod(externalEnrolment.getExecutionPeriod());
        setStudentNumber(externalEnrolment.getRegistration().getNumber());
        setGrade(externalEnrolment.getGrade());
        setEvaluationDate(externalEnrolment.getEvaluationDate());
        setExternalEnrolment(externalEnrolment);
        setEctsCredits(externalEnrolment.getEctsCredits());
    }

    public ExternalEnrolment getExternalEnrolment() {
        return this.externalEnrolment;
    }

    public void setExternalEnrolment(ExternalEnrolment externalEnrolment) {
        this.externalEnrolment = externalEnrolment;
    }

}
