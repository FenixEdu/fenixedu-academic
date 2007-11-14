package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Teacher;

public class InsertStudentsFinalEvaluation extends Service {

    public void run(final InfoEnrolmentEvaluation infoEnrolmentEvaluation, final Integer teacherNumber, final Date evaluationDate) throws NonExistingServiceException {
	final Teacher teacher = Teacher.readByNumber(teacherNumber);
	if (teacher == null) {
	    throw new NonExistingServiceException();
	}

	final EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(infoEnrolmentEvaluation.getIdInternal());
	enrolmentEvaluation.insertStudentFinalEvaluationForMasterDegree(infoEnrolmentEvaluation.getGradeValue(), teacher.getPerson(), evaluationDate);
    }

}
