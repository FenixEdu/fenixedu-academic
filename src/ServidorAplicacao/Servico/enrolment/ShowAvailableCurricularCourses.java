package ServidorAplicacao.Servico.enrolment;

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
			IStudentCurricularPlanPersistente studentCurricularPlanDAO =
				persistentSuport.getIStudentCurricularPlanPersistente();

			IStudent student =
				studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);

			if (student != null)
			{
				IStudentCurricularPlan studentCurricularPlan =
					studentCurricularPlanDAO.readActiveStudentCurricularPlan(
						student.getNumber(),
						student.getDegreeType());

				if (studentCurricularPlan != null)
				{
					try
					{
						IEnrolmentStrategyFactory enrolmentStrategyFactory =
							EnrolmentStrategyFactory.getInstance();
						IEnrolmentStrategy strategy =
							enrolmentStrategyFactory.getEnrolmentStrategyInstance(studentCurricularPlan);
						StudentEnrolmentContext studentEnrolmentContext =
						strategy.getAvailableCurricularCourses();

						return InfoStudentEnrolmentContext
						.cloneStudentEnrolmentContextToInfoStudentEnrolmentContext(
								studentEnrolmentContext);
					}
					catch (IllegalArgumentException e)
					{
						throw new FenixServiceException("degree");
					}
				}
				else
				{
					throw new ExistingServiceException("studentCurricularPlan");
				}
			}
			else
			{
				throw new ExistingServiceException("student");
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}
}