package ServidorAplicacao.Servico.enrolment;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos in Jan 27, 2004
 */

public class ShowAvailableCurricularCourses implements IService
{
	public ShowAvailableCurricularCourses()
	{
	}

	public InfoStudentEnrolmentContext run(Integer studentNumber) throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();
			IPersistentBranch branchDAO = persistentSuport.getIPersistentBranch();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();

			IStudent student = studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);

			if (student != null)
			{
				IStudentCurricularPlan studentCurricularPlan =
					studentCurricularPlanDAO.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

				if (studentCurricularPlan != null)
				{
					IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
					IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(studentCurricularPlan);

					StudentEnrolmentContext studentEnrolmentContext = strategy.getAvailableCurricularCourses();
					
					List areas = branchDAO.readByDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());
					studentEnrolmentContext.setAreas(areas);

					return InfoStudentEnrolmentContext.cloneStudentEnrolmentContextToInfoStudentEnrolmentContext(
						studentEnrolmentContext);
				} else
				{
					throw new ExistingServiceException("StudentCurricularPlan for student: " + studentNumber + " not found!");
				}
			} else
			{
				throw new ExistingServiceException("Student number: " + studentNumber + " not found!");
			}
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}
}