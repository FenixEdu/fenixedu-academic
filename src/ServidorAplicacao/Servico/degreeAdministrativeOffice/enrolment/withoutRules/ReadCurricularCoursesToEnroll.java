/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadCurricularCoursesToEnroll implements IService
{
	public ReadCurricularCoursesToEnroll()
	{
	}

	public Object run(
		InfoStudent infoStudent,
		TipoCurso degreeType,
		InfoExecutionYear infoExecutionYear,
		Integer executionDegreeID,
		final List curricularYearsList,
		final List curricularSemestersList)
		throws FenixServiceException
	{
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = new InfoStudentEnrolmentContext();

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Execution Degree
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeID);
			executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
			if (executionDegree == null)
			{
				throw new FenixServiceException("");
			}

			//Degree Curricular Plan
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
				sp.getIPersistentDegreeCurricularPlan();
			IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setIdInternal(executionDegree.getCurricularPlan().getIdInternal());
			degreeCurricularPlan =
				(IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
					degreeCurricularPlan,
					false);
			if (degreeCurricularPlan == null)
			{
				throw new FenixServiceException("");
			}
			
			// filters a list of curricular courses by all of its scopes that
			// matters in relation to the selected semester and the selected year.
			List curricularCoursesFromDegreeCurricularPlan =
				(
					List) CollectionUtils
						.select(degreeCurricularPlan.getCurricularCourses(), new Predicate()
			{

				public boolean evaluate(Object arg0)
				{
					boolean result = false;
					if (arg0 instanceof ICurricularCourse)
					{
						ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
						List scopes = curricularCourse.getScopes();
						Iterator iter = scopes.iterator();
						while (iter.hasNext() && !result)
						{
							ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
							if (curricularSemestersList
								.contains(scope.getCurricularSemester().getSemester())
								&& curricularYearsList.contains(
									scope.getCurricularSemester().getCurricularYear().getYear()))
							{
								result = true;
							}
						}
					}
					return result;
				}

			});

			//Student Curricular Plan
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			IStudentCurricularPlan studentCurricularPlan = null;
			if (infoStudent.getNumber() != null)
			{
				studentCurricularPlan =
					persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
						infoStudent.getNumber(),
						degreeType);
			}
			if (studentCurricularPlan == null)
			{
				throw new FenixServiceException("");
			}

			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			// Enrolments that have state APROVED and ENROLLED are to be subtracted from list of
			// possible choices.
			List enrollmentsEnrolled =
			persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					studentCurricularPlan,
					EnrolmentState.ENROLED);
			List enrollmentsAproved=
			persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					studentCurricularPlan,
					EnrolmentState.APROVED);
			
			List enrollmentsEnrolledAndAproved = new ArrayList();
			enrollmentsEnrolledAndAproved.addAll(enrollmentsEnrolled);
			enrollmentsEnrolledAndAproved.addAll(enrollmentsAproved);

			List curricularCoursesFromEnrolmentsWithStateEnroledAndAproved =
			(
					List) CollectionUtils
			.collect(enrollmentsEnrolledAndAproved, new Transformer()
					{
				public Object transform(Object obj)
				{
					IEnrolment enrolment = (IEnrolment) obj;

					return enrolment.getCurricularCourse();
				}
			});
			
			List possibleCurricularCoursesToChoose = (List) CollectionUtils.subtract(curricularCoursesFromDegreeCurricularPlan, curricularCoursesFromEnrolmentsWithStateEnroledAndAproved);
			
			infoStudentEnrolmentContext = buildResult(studentCurricularPlan, possibleCurricularCoursesToChoose);
			if (infoStudentEnrolmentContext == null)
			{
				throw new FenixServiceException("");
			}
			
			
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("");
		}

		return infoStudentEnrolmentContext;
	}
	
	private InfoStudentEnrolmentContext buildResult(
			IStudentCurricularPlan studentCurricularPlan,
			List curricularCoursesToChoose)
	{
		InfoStudentCurricularPlan infoStudentCurricularPlan =
		Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);

		List infoCurricularCoursesToChoose = (List) CollectionUtils.collect(curricularCoursesToChoose, new Transformer()
				{
			public Object transform(Object input)
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) input;
				return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
			}
		});
		Collections.sort(infoCurricularCoursesToChoose, new BeanComparator(("name")));

		InfoStudentEnrolmentContext infoStudentEnrolmentContext = new InfoStudentEnrolmentContext();
		infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		infoStudentEnrolmentContext.setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(infoCurricularCoursesToChoose);

		return infoStudentEnrolmentContext;
	}
}
