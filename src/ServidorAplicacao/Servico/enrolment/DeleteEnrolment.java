package ServidorAplicacao.Servico.enrolment;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos Jan 26, 2004
 */
public class DeleteEnrolment implements IService
{
	public DeleteEnrolment()
	{
	}
	// some of these arguments may be null. they are only needed for filter
	public void run(Integer executionDegreeId, Integer studentCurricularPlanId, Integer enrolmentID) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
			IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistentSuport.getIPersistentEnrolmentEvaluation();

			IEnrolment enrolment = (IEnrolment) enrolmentDAO.readByOID(Enrolment.class, enrolmentID);

			if (enrolment != null)
			{
				if (enrolment.getEvaluations() != null)
				{
					Iterator iterator = enrolment.getEvaluations().iterator();
					while (iterator.hasNext())
					{
						IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
						enrolmentEvaluationDAO.simpleLockWrite(enrolment);
						enrolmentEvaluationDAO.deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
					}
				}
				
				deleteAttend(enrolment);

				enrolmentDAO.simpleLockWrite(enrolment);
				enrolmentDAO.deleteByOID(Enrolment.class, enrolment.getIdInternal());
			}
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param enrolment
	 * @throws ExcepcaoPersistencia
	 */
	public static void deleteAttend(IEnrolment enrolment) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentExecutionCourse executionCourseDAO = persistentSuport.getIPersistentExecutionCourse();
		IFrequentaPersistente attendDAO = persistentSuport.getIFrequentaPersistente();
		IPersistentMark markDAO = persistentSuport.getIPersistentMark();
		IPersistentStudentGroupAttend studentGroupAttendDAO = persistentSuport.getIPersistentStudentGroupAttend();
		ITurnoAlunoPersistente shiftStudentDAO = persistentSuport.getITurnoAlunoPersistente();
		
		IExecutionCourse executionCourse =
			executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(
				enrolment.getCurricularCourse(),
				enrolment.getExecutionPeriod());

		if (executionCourse != null)
		{
			IFrequenta attend =
				attendDAO.readByAlunoAndDisciplinaExecucao(
					enrolment.getStudentCurricularPlan().getStudent(),
					executionCourse);

			if (attend != null)
			{
				List marks = markDAO.readBy(attend);
				if (marks == null || marks.isEmpty())
				{
					IStudentGroupAttend studentGroupAttend = studentGroupAttendDAO.readBy(attend);
					if (studentGroupAttend == null)
					{
						List shiftsStudentIsIn =
							shiftStudentDAO.readByStudent(enrolment.getStudentCurricularPlan().getStudent());

						if (shiftsStudentIsIn == null || shiftsStudentIsIn.isEmpty())
						{
							attendDAO.simpleLockWrite(attend);
							attendDAO.deleteByOID(Frequenta.class, attend.getIdInternal());
						} else
						{
							attendDAO.simpleLockWrite(attend);
							attend.setEnrolment(null);
						}
					} else
					{
						attendDAO.simpleLockWrite(attend);
						attend.setEnrolment(null);
					}
				} else
				{
					attendDAO.simpleLockWrite(attend);
					attend.setEnrolment(null);
				}
			}
		}
	}
}