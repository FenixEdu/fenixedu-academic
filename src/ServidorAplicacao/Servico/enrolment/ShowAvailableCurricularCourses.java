package ServidorAplicacao.Servico.enrolment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategyFactory;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
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
			IStudent student = getStudent(studentNumber);

			if (student != null)
			{
				IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

				if (studentCurricularPlan != null)
				{
					IEnrolmentPeriod enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
					IExecutionPeriod executionPeriod = getCurrentExecutionPeriod();
					if (executionPeriod.equals(enrolmentPeriod.getExecutionPeriod()))
					{
						try
						{
							return executeServiceLogics(studentCurricularPlan);
						}
						catch (IllegalArgumentException e)
						{
							throw new FenixServiceException("degree");
						}
					} else
					{
						throw new FenixServiceException("enrolmentPeriod");
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

	/**
	 * @param studentCurricularPlan
	 * @return InfoStudentEnrolmentContext
	 * @throws ExcepcaoPersistencia
	 */
	private InfoStudentEnrolmentContext executeServiceLogics(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		IEnrolmentStrategyFactory enrolmentStrategyFactory = EnrolmentStrategyFactory.getInstance();
		IEnrolmentStrategy strategy = enrolmentStrategyFactory.getEnrolmentStrategyInstance(studentCurricularPlan);
		StudentEnrolmentContext studentEnrolmentContext = strategy.getAvailableCurricularCourses();
		automaticalyEnrollInMandatoryCurricularCourses(studentEnrolmentContext);

		return InfoStudentEnrolmentContext.cloneStudentEnrolmentContextToInfoStudentEnrolmentContext(
			studentEnrolmentContext);
	}

	/**
	 * @param studentActiveCurricularPlan
	 * @return IEnrolmentPeriod
	 * @throws ExcepcaoPersistencia
	 * @throws OutOfCurricularCourseEnrolmentPeriod
	 */
	private IEnrolmentPeriod getEnrolmentPeriod(IStudentCurricularPlan studentActiveCurricularPlan)
		throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSuport.getIPersistentEnrolmentPeriod();
		IEnrolmentPeriod enrolmentPeriod =
			enrolmentPeriodDAO.readActualEnrolmentPeriodForDegreeCurricularPlan(
				studentActiveCurricularPlan.getDegreeCurricularPlan());
		if (enrolmentPeriod == null)
		{
			IEnrolmentPeriod nextEnrolmentPeriod =
				enrolmentPeriodDAO.readNextEnrolmentPeriodForDegreeCurricularPlan(
					studentActiveCurricularPlan.getDegreeCurricularPlan());
			Date startDate = null;
			Date endDate = null;
			if (nextEnrolmentPeriod != null)
			{
				startDate = nextEnrolmentPeriod.getStartDate();
				endDate = nextEnrolmentPeriod.getEndDate();
			}
			throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
		}
		return enrolmentPeriod;
	}

	/**
	 * @param studentNumber
	 * @return IStudent
	 * @throws ExcepcaoPersistencia
	 */
	private IStudent getStudent(Integer studentNumber) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();
		
		return studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
	}

	/**
	 * @param student
	 * @return IStudentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	private IStudentCurricularPlan getStudentCurricularPlan(IStudent student) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport.getIStudentCurricularPlanPersistente();

		return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
	}

	/**
	 * @return IExecutionPeriod
	 * @throws ExcepcaoPersistencia
	 */
	private IExecutionPeriod getCurrentExecutionPeriod() throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentExecutionPeriod executionPeriodDAO = persistentSuport.getIPersistentExecutionPeriod();

		return executionPeriodDAO.readActualExecutionPeriod();
	}

	/**
	 * @param studentEnrolmentContext
	 * @throws ExcepcaoPersistencia
	 */
	private void automaticalyEnrollInMandatoryCurricularCourses(StudentEnrolmentContext studentEnrolmentContext)
		throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentEnrolment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
		
		IStudentCurricularPlan studentCurricularPlan = studentEnrolmentContext.getStudentCurricularPlan();
		IExecutionPeriod executionPeriod = studentEnrolmentContext.getExecutionPeriod();

		List curricularCourses =
			getMandatoryCurricularCourses(studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled());
		
		if (curricularCourses != null && !curricularCourses.isEmpty())
		{
			List enrollmentsToAdd = new ArrayList();
			Iterator iterator = curricularCourses.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				IEnrolment enrolment =
					enrolmentDAO.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
						studentCurricularPlan,
						curricularCourse,
						executionPeriod);
				
				if (enrolment == null)
				{
					enrolment = new Enrolment();
					enrolmentDAO.simpleLockWrite(enrolment);
					enrolment.setCurricularCourse(curricularCourse);
					enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolment.setEnrolmentState(EnrolmentState.ENROLED);
					enrolment.setExecutionPeriod(executionPeriod);
					enrolment.setStudentCurricularPlan(studentCurricularPlan);
					enrollmentsToAdd.add(enrolment);
				}
			}
			
			studentEnrolmentContext.getStudentCurrentSemesterEnrollments().addAll(enrollmentsToAdd);
			studentEnrolmentContext.getFinalCurricularCoursesWhereStudentCanBeEnrolled().removeAll(curricularCourses);
		}
	}

	/**
	 * @param curricularCourses
	 * @return mandatoryCurricularCourses
	 */
	private List getMandatoryCurricularCourses(List curricularCourses)
	{
		List mandatoryCurricularCourses = new ArrayList();
		if (curricularCourses != null && !curricularCourses.isEmpty())
		{
			Iterator iterator = curricularCourses.iterator();
			while (iterator.hasNext())
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				
				if (curricularCourse.getMandatory().booleanValue())
				{
					mandatoryCurricularCourses.add(curricularCourse);
				}
			}
		}
		return mandatoryCurricularCourses;
	}

}