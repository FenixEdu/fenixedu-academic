package UtilInsc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;
import Util.MarkType;
import Util.TipoCurso;

/**
 * @author David Santos 11/Jun/2003
 */

public class GenerateExtraData
{

	private static final int MAX = 5;

	private static ISuportePersistente persistentSupport = null;

	public static void main(String[] args)
	{

		System.out.println("PROCESS STARTED...");

		// APAGO TODAS AS EXECUTION_COURSE PORQUE VOU ESCREVELAS DE RAIZ:
		turnOnPersistentSuport();
		IPersistentExecutionCourse persistentExecutionCourse =
			persistentSupport.getIPersistentExecutionCourse();
		//TODO: deleted-> persistentExecutionCourse.apagarTodasAsDisciplinasExecucao();
		turnOffPersistentSuport();

		// APAGO TODOS OS EXECUTION_DEGREE PORQUE VOU ESCREVELOS DE RAIZ:
		turnOnPersistentSuport();
		ICursoExecucaoPersistente persistentExecutionDegree =
			persistentSupport.getICursoExecucaoPersistente();
		try
		{
			persistentExecutionDegree.deleteAll();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}
		turnOffPersistentSuport();

		turnOnPersistentSuport();
		List curricularCoursesList = null;
		try
		{
			IPersistentCurricularCourse persistentCurricularCourse =
				persistentSupport.getIPersistentCurricularCourse();
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
				persistentSupport.getIPersistentDegreeCurricularPlan();
			DegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
			degreeCurricularPlanCriteria.setIdInternal(new Integer(1));
			IDegreeCurricularPlan oldDegreeCurricularPlan =
				(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
					degreeCurricularPlanCriteria);
			curricularCoursesList =
				persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(
					oldDegreeCurricularPlan);

			//			IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
			//			ITeacher teacherCriteria = new Teacher();
			//			teacherCriteria.setIdInternal(new Integer(1));
			//			ITeacher teacher = (ITeacher) persistentTeacher.readByOId(teacherCriteria, true);
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
			ICursoExecucao executionDegree = new CursoExecucao();

			//executionDegree.setCoordinator(teacher);
			executionDegree.setCurricularPlan(oldDegreeCurricularPlan);
			executionDegree.setExecutionYear(executionYear);
			executionDegree.setTemporaryExamMap(new Boolean(true));
			persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
			persistentExecutionDegree.lockWrite(executionDegree);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}
		turnOffPersistentSuport();

		Iterator iterator = curricularCoursesList.iterator();
		while (iterator.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
			generateExecutionCourse(curricularCourse);
		}

		for (int i = 1; i <= GenerateExtraData.MAX; i++)
		{

			// ESCREVO O DEGREE:
			turnOnPersistentSuport();
			ICurso degree = new Curso();
			degree.setDegreeCurricularPlans(null);
			degree.setIdInternal(null);
			degree.setNome("Curso Da Tanga " + i);
			degree.setSigla("CDT " + i);
			degree.setTipoCurso(TipoCurso.LICENCIATURA_OBJ);
			ICursoPersistente persistentdegree = persistentSupport.getICursoPersistente();
			try
			{
				persistentdegree.lockWrite(degree);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			turnOffPersistentSuport();

			// ESCREVO O DEGREE_CURRICULARR_PLAN:
			turnOnPersistentSuport();
			persistentdegree = persistentSupport.getICursoPersistente();
			ICurso degree2 = null;
			try
			{
				degree2 = (ICurso) persistentdegree.readDomainObjectByCriteria(degree);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setCurricularCourses(null);
			degreeCurricularPlan.setDegree(degree2);
			degreeCurricularPlan.setDegreeDuration(new Integer(5));
			degreeCurricularPlan.setEndDate(new Date());
			degreeCurricularPlan.setInitialDate(new Date());
			degreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));
			degreeCurricularPlan.setName("Plano Curricular do Curso Da Tanga " + i);
			degreeCurricularPlan.setState(DegreeCurricularPlanState.ACTIVE_OBJ);
			degreeCurricularPlan.setMarkType(MarkType.TYPE20_OBJ);
			degreeCurricularPlan.setNeededCredits(new Double(0));
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
				persistentSupport.getIPersistentDegreeCurricularPlan();
			try
			{
				persistentDegreeCurricularPlan.lockWrite(degreeCurricularPlan);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			turnOffPersistentSuport();

			// ESCREVO O BRANCH:
			turnOnPersistentSuport();
			persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan2 = null;
			try
			{
				degreeCurricularPlan2 =
					(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
						degreeCurricularPlan);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			IBranch branch = new Branch();
			branch.setCode("");
			branch.setName("");
			branch.setScopes(null);
			branch.setDegreeCurricularPlan(degreeCurricularPlan2);
			IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();
			try
			{
				persistentBranch.lockWrite(branch);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			turnOffPersistentSuport();

			// ESCREVO O EXECUTION_DEGREE:
			turnOnPersistentSuport();
			persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
			IPersistentExecutionYear persistentExecutionYear =
				persistentSupport.getIPersistentExecutionYear();
			persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
			//			IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
			try
			{
				degreeCurricularPlan2 =
					(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
						degreeCurricularPlan);
				IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
				//				ITeacher teacher = persistentTeacher.readByNumber(new Integer(1));
				ICursoExecucao executionDegree = new CursoExecucao();
				//executionDegree.setCoordinator(teacher);
				executionDegree.setCurricularPlan(degreeCurricularPlan2);
				executionDegree.setExecutionYear(executionYear);
				executionDegree.setTemporaryExamMap(new Boolean(true));
				persistentExecutionDegree.lockWrite(executionDegree);
			}
			catch (ExcepcaoPersistencia e1)
			{
				e1.printStackTrace(System.out);
			}
			turnOffPersistentSuport();

			generateCurricularCourses(degreeCurricularPlan, branch, i);
		}

		System.out.println("PROCESS ENDED!");
	}

	private static void turnOnPersistentSuport()
	{
		try
		{
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentSupport.iniciarTransaccao();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}
	}

	private static void turnOffPersistentSuport()
	{
		try
		{
			persistentSupport.confirmarTransaccao();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}
	}

	private static void generateCurricularCourses(
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch,
		int i)
	{
		List curricularCoursesList = null;
		IDegreeCurricularPlan degreeCurricularPlan2 = null;
		IBranch branch2 = null;

		turnOnPersistentSuport();
		IPersistentCurricularCourse persistentCurricularCourse =
			persistentSupport.getIPersistentCurricularCourse();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
			persistentSupport.getIPersistentDegreeCurricularPlan();
		IPersistentBranch persistentBranch = persistentSupport.getIPersistentBranch();
		IPersistentCurricularCourseScope persistentCurricularCourseScope =
			persistentSupport.getIPersistentCurricularCourseScope();
		try
		{
			DegreeCurricularPlan degreeCurricularPlanCriteria = new DegreeCurricularPlan();
			degreeCurricularPlanCriteria.setIdInternal(new Integer(1));
			IDegreeCurricularPlan oldDegreeCurricularPlan =
				(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
					degreeCurricularPlanCriteria);
			curricularCoursesList =
				persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(
					oldDegreeCurricularPlan);
			degreeCurricularPlan2 =
				(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readDomainObjectByCriteria(
					degreeCurricularPlan);
			branch2 = (IBranch) persistentBranch.readDomainObjectByCriteria(branch);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}
		turnOffPersistentSuport();

		Iterator iterator1 = curricularCoursesList.iterator();
		while (iterator1.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();

			// ESCREVO A CURRICULAR_COURSE:
			turnOnPersistentSuport();
			ICurricularCourse curricularCourseToWrite = new CurricularCourse();
			curricularCourseToWrite.setAssociatedExecutionCourses(null);
			curricularCourseToWrite.setDepartmentCourse(null);
			curricularCourseToWrite.setIdInternal(null);
			curricularCourseToWrite.setScopes(null);
			curricularCourseToWrite.setBasic(new Boolean(true));
			curricularCourseToWrite.setCode(curricularCourse.getCode() + i);
			curricularCourseToWrite.setCurricularCourseExecutionScope(
				curricularCourse.getCurricularCourseExecutionScope());
			curricularCourseToWrite.setDegreeCurricularPlan(degreeCurricularPlan2);
			curricularCourseToWrite.setMandatory(curricularCourse.getMandatory());
			curricularCourseToWrite.setName(curricularCourse.getName() + i);
			curricularCourseToWrite.setType(curricularCourse.getType());
			curricularCourseToWrite.setUniversity(curricularCourse.getUniversity());

			curricularCourseToWrite.setLabHours(curricularCourse.getLabHours());
			curricularCourseToWrite.setPraticalHours(curricularCourse.getPraticalHours());
			curricularCourseToWrite.setTheoPratHours(curricularCourse.getTheoPratHours());
			curricularCourseToWrite.setTheoreticalHours(curricularCourse.getTheoreticalHours());
			curricularCourseToWrite.setMaximumValueForAcumulatedEnrollments(curricularCourse.getMaximumValueForAcumulatedEnrollments());
			curricularCourseToWrite.setMinimumValueForAcumulatedEnrollments(curricularCourse.getMinimumValueForAcumulatedEnrollments());
			curricularCourseToWrite.setCredits(null);
			curricularCourseToWrite.setEctsCredits(curricularCourse.getEctsCredits());
			curricularCourseToWrite.setEnrollmentWeigth(curricularCourse.getEnrollmentWeigth());

			persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
			try
			{
				persistentCurricularCourse.lockWrite(curricularCourseToWrite);
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			turnOffPersistentSuport();

			// ESCREVO A EXECUTION_COURSE:
			generateExecutionCourse(curricularCourseToWrite);

			Iterator iterator2 = curricularCourse.getScopes().iterator();
			while (iterator2.hasNext())
			{
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator2.next();

				// ESCREVO O CURRICULARCOURSESCOPE
				turnOnPersistentSuport();
				ICurricularCourseScope curricularCourseScopeToWrite = new CurricularCourseScope();
				curricularCourseScopeToWrite.setIdInternal(null);

				curricularCourseScopeToWrite.setBeginDate(Calendar.getInstance());
				curricularCourseScopeToWrite.setEndDate(null);
				try
				{
					persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
					ICurricularCourse curricularCourse2 =
						(ICurricularCourse) persistentCurricularCourse.readDomainObjectByCriteria(
							curricularCourseToWrite);

					curricularCourseScopeToWrite.setBranch(branch2);
					curricularCourseScopeToWrite.setCurricularCourse(curricularCourse2);
					curricularCourseScopeToWrite.setCurricularSemester(
						curricularCourseScope.getCurricularSemester());

					persistentCurricularCourseScope.lockWrite(curricularCourseScopeToWrite);
				}
				catch (ExistingPersistentException e1)
				{
					e1.printStackTrace(System.out);
				}
				catch (ExcepcaoPersistencia e1)
				{
					e1.printStackTrace(System.out);
				}
				turnOffPersistentSuport();
			}
		}
	}

	private static void generateExecutionCourse(ICurricularCourse curricularCourse)
	{

		if (curricularCourse.getCode().equals(""))
		{
			turnOnPersistentSuport();
			IPersistentCurricularCourse persistentCurricularCourse =
				persistentSupport.getIPersistentCurricularCourse();
			try
			{
				persistentCurricularCourse.lockWrite(curricularCourse);
				curricularCourse.setCode(curricularCourse.getName());
			}
			catch (ExcepcaoPersistencia e)
			{
				e.printStackTrace(System.out);
			}
			turnOffPersistentSuport();
		}

		turnOnPersistentSuport();

		IPersistentCurricularCourse persistentCurricularCourse =
			persistentSupport.getIPersistentCurricularCourse();
		IPersistentExecutionCourse persistentExecutionCourse =
			persistentSupport.getIPersistentExecutionCourse();
		IPersistentExecutionPeriod persistentExecutionPeriod =
			persistentSupport.getIPersistentExecutionPeriod();

		ICurricularCourse curricularCourseToAssociate = null;
		IExecutionPeriod executionPeriod = null;
		try
		{
			curricularCourseToAssociate =
				persistentCurricularCourse.readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
					curricularCourse.getDegreeCurricularPlan().getIdInternal(),
					curricularCourse.getName(),
					curricularCourse.getCode());
			executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		IExecutionCourse executionCourse = new ExecutionCourse();
		executionCourse.setAssociatedExams(null);
		executionCourse.setComment(null);
		executionCourse.setIdInternal(null);

		//		executionCourse.setTheoreticalHours(curricularCourseToAssociate.getTheoreticalHours());
		//		executionCourse.setTheoPratHours(curricularCourseToAssociate.getTheoPratHours());
		executionCourse.setSigla(curricularCourseToAssociate.getCode());
		//		executionCourse.setPraticalHours(curricularCourseToAssociate.getPraticalHours());
		executionCourse.setNome(curricularCourseToAssociate.getName());
		//		executionCourse.setLabHours(curricularCourseToAssociate.getLabHours());
		executionCourse.setExecutionPeriod(executionPeriod);

		List associatedCurricularCourses = new ArrayList();
		associatedCurricularCourses.add(curricularCourseToAssociate);
		executionCourse.setAssociatedCurricularCourses(associatedCurricularCourses);

		try
		{
			persistentExecutionCourse.lockWrite(executionCourse);
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		turnOffPersistentSuport();
	}
}