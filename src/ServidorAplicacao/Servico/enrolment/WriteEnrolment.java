package ServidorAplicacao.Servico.enrolment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.ExecutionPeriod;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author David Santos Jan 26, 2004
 */
public class WriteEnrolment implements IService
{
	public WriteEnrolment()
	{
	}
	// some of these arguments may be null. they are only needed for filter
	public void run(
		Integer executionDegreeId, 
		Integer studentCurricularPlanID,
		Integer curricularCourseID,
		Integer executionPerriodID)
		throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO =
				persistentSuport.getIStudentCurricularPlanPersistente();
			IPersistentExecutionPeriod executionPeriodDAO =
				persistentSuport.getIPersistentExecutionPeriod();
			IPersistentCurricularCourse curricularCourseDAO =
				persistentSuport.getIPersistentCurricularCourse();

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(
					StudentCurricularPlan.class,
					studentCurricularPlanID);
			ICurricularCourse curricularCourse =
				(ICurricularCourse) curricularCourseDAO.readByOID(
					CurricularCourse.class,
					curricularCourseID);

			IExecutionPeriod executionPeriod = null;
			if (executionPerriodID == null)
			{
				executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
			}
			else
			{
				executionPeriod =
					(IExecutionPeriod) executionPeriodDAO.readByOID(
						ExecutionPeriod.class,
						executionPerriodID);
			}

			IEnrolment enrolment =
				enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
					studentCurricularPlan,
					curricularCourse,
					executionPeriod);

			if (enrolment == null)
			{
				IEnrolment enrolmentToWrite = new Enrolment();
				enrolmentDAO.simpleLockWrite(enrolmentToWrite);
				enrolmentToWrite.setCurricularCourse(curricularCourse);
				enrolmentToWrite.setEnrolmentState(EnrolmentState.ENROLED);
				enrolmentToWrite.setExecutionPeriod(executionPeriod);
				enrolmentToWrite.setStudentCurricularPlan(studentCurricularPlan);
				enrolmentToWrite.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);

				createAttend(
					studentCurricularPlan.getStudent(),
					curricularCourse,
					executionPeriod,
					enrolmentToWrite);
			}

		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @param executionPeriod
	 * @param enrolmentToWrite
	 * @throws ExcepcaoPersistencia
	 */
	public static void createAttend(
		IStudent student,
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod,
		IEnrolment enrolmentToWrite)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentExecutionCourse executionCourseDAO = persistentSuport.getIPersistentExecutionCourse();
		IFrequentaPersistente attendDAO = persistentSuport.getIFrequentaPersistente();

		IExecutionCourse executionCourse =
			executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(
				curricularCourse,
				executionPeriod);
		if (executionCourse != null)
		{
			IFrequenta attend = attendDAO.readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (attend != null)
			{
				attendDAO.simpleLockWrite(attend);
				attend.setEnrolment(enrolmentToWrite);
			}
			else
			{
				IFrequenta attendToWrite = new Frequenta();
				attendDAO.simpleLockWrite(attendToWrite);
				attendToWrite.setAluno(student);
				attendToWrite.setDisciplinaExecucao(executionCourse);
				attendToWrite.setEnrolment(enrolmentToWrite);
			}
		}
	}

	public static void createAttend(IEnrolment enrolment) throws ExcepcaoPersistencia
	{
		createAttend(
			enrolment.getStudentCurricularPlan().getStudent(),
			enrolment.getCurricularCourse(),
			enrolment.getExecutionPeriod(),
			enrolment);
	}
}