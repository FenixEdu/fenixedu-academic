package ServidorAplicacao.Servico.enrolment.shift;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/*
 * 
 * @author Fernanda Quitério 11/Fev/2004
 *  
 */
public class WriteStudentAttendingCourse implements IService
{
	public class ReachedAttendsLimitServiceException extends FenixServiceException
	{

	}
	
	public WriteStudentAttendingCourse()
	{
	}

	public Boolean run(InfoStudent infoStudent, Integer executionCourseId) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();

			
			/* :AQUI: acrescentei a leitura. o que chega aqui da action é o idInternal */
			IPersistentStudent studentDAO=sp.getIPersistentStudent();
			IStudent student =(IStudent) studentDAO.readByOId(new Student(infoStudent.getIdInternal()), false);
			infoStudent.setNumber(student.getNumber());
			
			
			if (infoStudent == null)
			{
				return new Boolean(false);
			}
			if (executionCourseId != null)
			{
				IStudentCurricularPlan studentCurricularPlan =
					findStudentCurricularPlan(infoStudent, persistentStudentCurricularPlan);

				IExecutionCourse executionCourse =
					findExecutionCourse(executionCourseId, persistentExecutionCourse);

				//Read every course the student attends to:
				List attends =
					persistentAttend.readByStudentNumberInCurrentExecutionPeriod(studentCurricularPlan.getStudent().getNumber());

				if (attends != null && attends.size() >= 8)
				{
					throw new ReachedAttendsLimitServiceException();
				}

				IFrequenta attendsEntry =
					persistentAttend.readByAlunoAndDisciplinaExecucao(
						studentCurricularPlan.getStudent(),
						executionCourse);
				if (attendsEntry == null)
				{
					attendsEntry = new Frequenta();
					persistentAttend.simpleLockWrite(attendsEntry);
					attendsEntry.setAluno(studentCurricularPlan.getStudent());
					attendsEntry.setDisciplinaExecucao(executionCourse);

					findEnrollmentForAttend(
						persistentEnrolment,
						studentCurricularPlan,
						executionCourse,
						attendsEntry);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return new Boolean(true);
	}

	private void findEnrollmentForAttend(
		IPersistentEnrolment persistentEnrolment,
		IStudentCurricularPlan studentCurricularPlan,
		IExecutionCourse executionCourse,
		IFrequenta attendsEntry)
		throws ExcepcaoPersistencia
	{
		// checks if there is an enrollment for this attend
		Iterator iterCurricularCourses = executionCourse.getAssociatedCurricularCourses().iterator();
		while (iterCurricularCourses.hasNext())
		{
			ICurricularCourse curricularCourseElem = (ICurricularCourse) iterCurricularCourses.next();

			IEnrolment enrollment =
				persistentEnrolment.readByStudentCurricularPlanAndCurricularCourse(
					studentCurricularPlan,
					curricularCourseElem);
			if (enrollment != null)
			{
				attendsEntry.setEnrolment(enrollment);
				break;
			}
		}
	}

	private IExecutionCourse findExecutionCourse(
		Integer executionCourseId,
		IPersistentExecutionCourse persistentExecutionCourse)
		throws FenixServiceException
	{
		IExecutionCourse executionCourse = new ExecutionCourse();
		executionCourse.setIdInternal(executionCourseId);
		executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);

		if (executionCourse == null)
		{
			throw new FenixServiceException("noExecutionCourse");
		}
		return executionCourse;
	}

	private IStudentCurricularPlan findStudentCurricularPlan(
		InfoStudent infoStudent,
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan)
		throws FenixServiceException
	{
		try
		{
			IStudentCurricularPlan studentCurricularPlan =
				(
					IStudentCurricularPlan) persistentStudentCurricularPlan
						.readActiveByStudentNumberAndDegreeType(
					infoStudent.getNumber(),
					TipoCurso.LICENCIATURA_OBJ);
			if (studentCurricularPlan == null)
			{
				throw new FenixServiceException("noStudent");
			}
			return studentCurricularPlan;
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException();
		}
	}
}