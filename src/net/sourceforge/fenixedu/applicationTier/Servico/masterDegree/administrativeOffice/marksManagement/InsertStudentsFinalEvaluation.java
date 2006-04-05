package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertStudentsFinalEvaluation extends Service {

	public List run(List<InfoEnrolmentEvaluation> evaluations, Integer teacherNumber,
			Date evaluationDate, IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

		List<InfoEnrolmentEvaluation> infoEvaluationsWithError = new ArrayList<InfoEnrolmentEvaluation>();

		for (InfoEnrolmentEvaluation infoEnrolmentEvaluation : evaluations) {

			Teacher teacher = Teacher.readByNumber(teacherNumber);
			if (teacher == null) {
				throw new NonExistingServiceException();
			}

			Student student = rootDomainObject.readStudentByOID(infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getIdInternal());
			infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().setNumber(student.getNumber());

			EnrolmentEvaluation enrolmentEvaluationFromDb = rootDomainObject.readEnrolmentEvaluationByOID(infoEnrolmentEvaluation.getIdInternal());
			try {
				enrolmentEvaluationFromDb.insertStudentFinalEvaluationForMasterDegree(
						infoEnrolmentEvaluation.getGrade(), teacher.getPerson(), evaluationDate);
			}
			catch (DomainException e) {
				infoEvaluationsWithError.add(infoEnrolmentEvaluation);
			}
		}

		return infoEvaluationsWithError;
	}

}
