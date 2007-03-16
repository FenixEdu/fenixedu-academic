package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.enrolment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;

public class DeleteExternalEnrolments extends Service {

    public void run(final Student student, String[] externalEnrolmentIDs) throws FenixServiceException {
	
	for (final String externalEnrolmentID : externalEnrolmentIDs) {
	    final ExternalEnrolment externalEnrolment = getExternalEnrolmentByID(student, Integer.valueOf(externalEnrolmentID));
	    if (externalEnrolment == null) {
		throw new FenixServiceException("error.DeleteExternalEnrolments.externalEnrolmentID.doesnot.belong.to.student");
	    }
	    externalEnrolment.delete();
	}
	
    }

    private ExternalEnrolment getExternalEnrolmentByID(final Student student, final Integer externalEnrolmentID) {
	for (final ExternalEnrolment externalEnrolment : student.getExternalEnrolmentsSet()) {
	    if (externalEnrolment.getIdInternal().equals(externalEnrolmentID)) {
		return externalEnrolment;
	    }
	}
	return null;
    }
}
