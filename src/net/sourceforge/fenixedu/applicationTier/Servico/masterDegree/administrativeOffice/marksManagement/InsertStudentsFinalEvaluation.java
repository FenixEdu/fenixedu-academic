package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

public class InsertStudentsFinalEvaluation extends FenixService {

    @Service
    public static void run(final InfoEnrolmentEvaluation infoEnrolmentEvaluation, final String teacherId,
	    final Date evaluationDate) throws NonExistingServiceException {
	final Teacher teacher = Teacher.readByIstId(teacherId);
	if (teacher == null) {
	    throw new NonExistingServiceException();
	}

	final EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(infoEnrolmentEvaluation
		.getIdInternal());
	enrolmentEvaluation.insertStudentFinalEvaluationForMasterDegree(infoEnrolmentEvaluation.getGradeValue(), teacher
		.getPerson(), evaluationDate);
    }

}