package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.certificates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ExamDateCertificateExamSelectionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<ExamDateCertificateExamSelectionEntryBean> entries;

    public ExamDateCertificateExamSelectionBean() {
	this.entries = new ArrayList<ExamDateCertificateExamSelectionEntryBean>();
    }

    public List<ExamDateCertificateExamSelectionEntryBean> getEntries() {
	return entries;
    }

    public void addEntry(final ExamDateCertificateExamSelectionEntryBean entry) {
	this.entries.add(entry);
    }

    public void addEntries(final List<ExamDateCertificateExamSelectionEntryBean> entries) {
	this.entries.addAll(entries);
    }

    public static ExamDateCertificateExamSelectionBean buildFor(final Collection<Enrolment> enrolments,
	    final ExecutionPeriod executionPeriod) {
	final ExamDateCertificateExamSelectionBean result = new ExamDateCertificateExamSelectionBean();

	for (final Enrolment enrolment : enrolments) {
	    for (final Exam exam : enrolment.getAttendsFor(executionPeriod).getExecutionCourse().getPublishedExamsFor(
		    enrolment.getCurricularCourse())) {
		result.addEntry(new ExamDateCertificateExamSelectionEntryBean(enrolment, exam));
	    }
	}

	return result;
    }

    public Set<Enrolment> getEnrolmentsWithoutExam(final Collection<Enrolment> enrolments) {
	final Set<Enrolment> result = new HashSet<Enrolment>();

	for (final Enrolment enrolment : enrolments) {
	    if (!containsEnrolment(enrolment)) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    private boolean containsEnrolment(Enrolment enrolment) {
	for (final ExamDateCertificateExamSelectionEntryBean each : getEntries()) {
	    if (each.getEnrolment() == enrolment) {
		return true;
	    }
	}

	return false;
    }
}
