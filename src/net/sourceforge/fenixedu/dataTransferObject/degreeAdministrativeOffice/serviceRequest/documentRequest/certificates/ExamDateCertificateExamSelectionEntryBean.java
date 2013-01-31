package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;

public class ExamDateCertificateExamSelectionEntryBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Enrolment enrolment;

	private Exam exam;

	public ExamDateCertificateExamSelectionEntryBean(final Enrolment enrolment, final Exam exam) {
		setEnrolment(enrolment);
		setExam(exam);
	}

	public Enrolment getEnrolment() {
		return this.enrolment;
	}

	public void setEnrolment(Enrolment enrolment) {
		this.enrolment = enrolment;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Set<DegreeModuleScope> getDegreeModuleScopesForEnrolment() {
		return getExam().getDegreeModuleScopesFor(getEnrolment().getCurricularCourse());
	}

}
