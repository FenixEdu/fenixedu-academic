package ServidorAplicacao.Servico.enrollment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.EnrolmentEvaluation;
import Dominio.EnrolmentInOptionalCurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author David Santos in Mar 23, 2004
 */

public class WriteEnrolmentInOptionalCourse implements IService
{
	public WriteEnrolmentInOptionalCourse()
	{
	}

	// Some of these arguments may be null. they are only needed for filter
	public void run(Integer executionDegreeId, Integer studentCurricularPlanID, Integer optionalCurricularCourseID,
		Integer chosenCurricularCourseID, String year, TipoCurso degreeType) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();
			IPersistentCurricularCourse curricularCourseDAO = persistentSuport.getIPersistentCurricularCourse();

			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(
				StudentCurricularPlan.class, studentCurricularPlanID);

			ICurricularCourse optionalCurricularCourse = (ICurricularCourse) curricularCourseDAO.readByOID(CurricularCourse.class,
				optionalCurricularCourseID);
			
			ICurricularCourse chosenCurricularCourse = (ICurricularCourse) curricularCourseDAO.readByOID(CurricularCourse.class,
				chosenCurricularCourseID);
			
			IPersistentExecutionYear executionYearDAO = persistentSuport.getIPersistentExecutionYear();
			IExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);
			
			IExecutionPeriod executionPeriod = WriteEnrollmentsList.findExecutionPeriod(persistentSuport, executionYear,
				chosenCurricularCourseID, degreeType);

			IEnrollment enrolment = enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
				studentCurricularPlan, optionalCurricularCourse, executionPeriod);

			if (enrolment == null)
			{
				IEnrolmentInOptionalCurricularCourse enrolmentToWrite = new EnrolmentInOptionalCurricularCourse();
				enrolmentDAO.simpleLockWrite(enrolmentToWrite);
				enrolmentToWrite.setCurricularCourse(optionalCurricularCourse);
				enrolmentToWrite.setCurricularCourseForOption(chosenCurricularCourse);
				enrolmentToWrite.setEnrolmentState(EnrolmentState.ENROLLED);
				enrolmentToWrite.setExecutionPeriod(executionPeriod);
				enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
				enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);

				createEnrollmentEvaluation(enrolmentToWrite);

				WriteEnrollment.createAttend(studentCurricularPlan.getStudent(), chosenCurricularCourse, executionPeriod,
					enrolmentToWrite);
			}

		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		WriteEnrollment.resetAttends();
	}

	private void createEnrollmentEvaluation(IEnrollment enrolment) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolmentEvaluation enrollmentEvaluationDAO = persistentSuport.getIPersistentEnrolmentEvaluation();

		IEnrolmentEvaluation enrolmentEvaluation = enrollmentEvaluationDAO
			.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment, EnrolmentEvaluationType.NORMAL_OBJ,
				null);

		if (enrolmentEvaluation == null)
		{
			enrolmentEvaluation = new EnrolmentEvaluation();
			enrollmentEvaluationDAO.simpleLockWrite(enrolmentEvaluation);
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolmentEvaluation.setCheckSum(null);
			enrolmentEvaluation.setEmployee(null);
			enrolmentEvaluation.setExamDate(null);
			enrolmentEvaluation.setGrade(null);
			enrolmentEvaluation.setGradeAvailableDate(null);
			enrolmentEvaluation.setObservation(null);
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
			enrolmentEvaluation.setWhen(null);
			enrolmentEvaluation.setAckOptLock(new Integer(1));
		}
	}

}