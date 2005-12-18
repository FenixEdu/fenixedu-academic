package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class InsertStudentsFinalEvaluation implements IService {

	public List run(List<InfoEnrolmentEvaluation> evaluations, Integer teacherNumber,
			Date evaluationDate, IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

		List<InfoEnrolmentEvaluation> infoEvaluationsWithError = new ArrayList<InfoEnrolmentEvaluation>();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
				.getIPersistentEnrolmentEvaluation();
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();

		for (InfoEnrolmentEvaluation infoEnrolmentEvaluation : evaluations) {

			ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
			if (teacher == null) {
				throw new NonExistingServiceException();
			}

			IStudent student = (IStudent) persistentStudent.readByOID(Student.class,
					infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan()
							.getInfoStudent().getIdInternal());

			infoEnrolmentEvaluation.getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent()
					.setNumber(student.getNumber());

			IEnrolmentEvaluation enrolmentEvaluationFromDb = (IEnrolmentEvaluation) persistentEnrolmentEvaluation
					.readByOID(EnrolmentEvaluation.class, infoEnrolmentEvaluation.getIdInternal());

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