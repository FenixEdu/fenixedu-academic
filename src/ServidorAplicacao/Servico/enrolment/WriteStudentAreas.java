package ServidorAplicacao.Servico.enrolment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Branch;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos
 * Jan 26, 2004
 */
public class WriteStudentAreas implements IService
{
	public WriteStudentAreas(){}

	public void run(Integer studentCurricularPlanID, Integer specializationAreaID, Integer secundaryAreaID)
		throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(StudentCurricularPlan.class, studentCurricularPlanID);
			
			IBranch specializationArea = (IBranch) branchDAO.readByOID(Branch.class, specializationAreaID);

			IBranch secundaryArea = (IBranch) branchDAO.readByOID(Branch.class, secundaryAreaID);
			
			if (studentCurricularPlan != null && specializationArea != null && secundaryArea != null)
			{
				if (!specializationArea.equals(secundaryArea))
				{
					studentCurricularPlanDAO.simpleLockWrite(studentCurricularPlan);
					studentCurricularPlan.setBranch(specializationArea);
					studentCurricularPlan.setSecundaryBranch(secundaryArea);
				} else
				{
					throw new BothAreasAreTheSameServiceException();
				}
			} else
			{
				throw new ExistingServiceException("studentCurricularPlan or specializationArea or secundaryArea or all are invalid");
			}
		} catch (ExcepcaoPersistencia e)
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
			executionCourseDAO.readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
		if (executionCourse != null)
		{
			IFrequenta attend = attendDAO.readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (attend != null)
			{
				attendDAO.simpleLockWrite(attend);
				attend.setEnrolment(enrolmentToWrite);
			} else
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