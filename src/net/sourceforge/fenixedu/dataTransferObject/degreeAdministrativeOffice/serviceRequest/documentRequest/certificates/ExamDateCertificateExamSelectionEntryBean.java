package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;

public class ExamDateCertificateExamSelectionEntryBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<Enrolment> enrolment;

    private DomainReference<Exam> exam;

    public ExamDateCertificateExamSelectionEntryBean(final Enrolment enrolment, final Exam exam) {
	setEnrolment(enrolment);
	setExam(exam);
    }

    public Enrolment getEnrolment() {
	return (this.enrolment != null) ? this.enrolment.getObject() : null;
    }

    public void setEnrolment(Enrolment enrolment) {
	this.enrolment = (enrolment != null) ? new DomainReference<Enrolment>(enrolment) : null;
    }

    public Exam getExam() {
	return (this.exam != null) ? this.exam.getObject() : null;
    }

    public void setExam(Exam exam) {
	this.exam = (exam != null) ? new DomainReference<Exam>(exam) : null;
    }

    public Set<DegreeModuleScope> getDegreeModuleScopesForEnrolment() {
	return getExam().getDegreeModuleScopesFor(getEnrolment().getCurricularCourse());
    }

}
