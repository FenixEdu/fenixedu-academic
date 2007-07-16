package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.notNeedToEnrol;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class AssociateEnrolmentsToNotNeedToEnrol extends Service {

    public void run(Student student,
	    NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse,
	    Collection<Enrolment> selectedEnrolments, Collection<ExternalEnrolment> externalEnrolments)
	    throws FenixServiceException {

	for (Enrolment enrolment : notNeedToEnrollInCurricularCourse.getEnrolmentsSet()) {
	    if (selectedEnrolments == null || !selectedEnrolments.contains(enrolment)) {
		notNeedToEnrollInCurricularCourse.removeEnrolments(enrolment);
	    }
	}

	if (selectedEnrolments != null && !selectedEnrolments.isEmpty()) {
	    Collection<Enrolment> aprovedEnrolments = student.getApprovedEnrolments();
	    for (Enrolment selectedEnrolment : selectedEnrolments) {
		Enrolment enrolment = getAprovedEnrolment(aprovedEnrolments, selectedEnrolment);
		if (enrolment == null) {
		    throw new FenixServiceException();
		}

		notNeedToEnrollInCurricularCourse.addEnrolments(enrolment);
	    }
	}

	for (ExternalEnrolment enrolment : notNeedToEnrollInCurricularCourse.getExternalEnrolmentsSet()) {
	    if (externalEnrolments == null || !externalEnrolments.contains(enrolment)) {
		notNeedToEnrollInCurricularCourse.removeExternalEnrolments(enrolment);
	    }
	}

	if (externalEnrolments != null && !externalEnrolments.isEmpty()) {
	    for (ExternalEnrolment selectedExternalEnrolment : externalEnrolments) {
		if (selectedExternalEnrolment.getRegistration().getStudent() != student) {
		    throw new FenixServiceException();
		}
		notNeedToEnrollInCurricularCourse.addExternalEnrolments(selectedExternalEnrolment);
	    }
	}

    }

    private Enrolment getAprovedEnrolment(Collection<Enrolment> aprovedEnrolments,
	    Enrolment selectedEnrolment) {
	for (Enrolment enrolment : aprovedEnrolments) {
	    if (enrolment.equals(selectedEnrolment)) {
		return enrolment;
	    }
	}
	return null;
    }

}
