/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author Tânia Pousão
 *  
 */
public class WriteEnrollmentsList implements IService
{
	public WriteEnrollmentsList()
	{
	}

	public void run(
		InfoStudent infoStudent,
		TipoCurso degreeType,
		InfoExecutionYear infoExecutionYear,
		List curricularCoursesList, Map optionalEnrollments)
		throws FenixServiceException
	{
		try
		{
			if (infoStudent == null
				|| infoStudent.getNumber() == null
				|| degreeType == null
				|| infoExecutionYear == null
				|| infoExecutionYear.getYear() == null)
			{
				throw new FenixServiceException("");
			}

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			IStudentCurricularPlan studentCurricularPlan =
				persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
					infoStudent.getNumber(),
					degreeType);
			if (studentCurricularPlan == null)
			{
				throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
			}

			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName(infoExecutionYear.getYear());
			if (executionYear == null)
			{
				throw new FenixServiceException("error.impossible.operations");
			}

			if (curricularCoursesList != null && curricularCoursesList.size() > 0)
			{
				ListIterator iterator = curricularCoursesList.listIterator();
				while (iterator.hasNext())
				{
				    String enrollmentInfo = (String) iterator.next();
				    
					Integer curricularCourseID = new Integer(enrollmentInfo.split("-")[0]);
					//currently ignoring this value
					//Integer enrollmentType = new Integer(enrollmentInfo.split("-")[0]);

					IExecutionPeriod executionPeriod =
						findExecutionPeriod(sp, executionYear, curricularCourseID, degreeType);
					Integer executionPeriodId = null;
					if (executionPeriod != null)
					{
						executionPeriodId = executionPeriod.getIdInternal();
					}

					WriteEnrollment writeEnrolmentService = new WriteEnrollment();
					writeEnrolmentService.run(
						null,
						studentCurricularPlan.getIdInternal(),
						curricularCourseID,
						executionPeriodId,
						CurricularCourseEnrollmentType.DEFINITIVE,
						(String) optionalEnrollments.get(curricularCourseID.toString())
					);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}

	}

	public static IExecutionPeriod findExecutionPeriod(
		ISuportePersistente sp,
		IExecutionYear executionYear,
		Integer curricularCourseID,
		TipoCurso degreeType)
		throws ExcepcaoPersistencia, FenixServiceException
	{
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
		IExecutionPeriod executionPeriod = null;

		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		ICurricularCourse curricularCourse =
			(ICurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseID);
		
		if (curricularCourse == null)
		{
			throw new FenixServiceException("");
		}

		List curricularCourseScopes = curricularCourse.getScopes();

		Integer semester = findBestSemester(persistentExecutionPeriod, curricularCourseScopes);
		if (semester != null)
		{
			executionPeriod = persistentExecutionPeriod.readBySemesterAndExecutionYear(semester, executionYear);
		}
		
		return executionPeriod;

	}

	private static Integer findBestSemester(
		IPersistentExecutionPeriod persistentExecutionPeriod,
		List curricularCourseScopes)
		throws ExcepcaoPersistencia
	{
		Integer semester = null;
		if (curricularCourseScopes != null && curricularCourseScopes.size() > 0)
		{
			ListIterator iterator = curricularCourseScopes.listIterator();
			if (iterator.hasNext())
			{
				//inicialize semester with the first scope
				semester =
					((ICurricularCourseScope) iterator.next()).getCurricularSemester().getSemester();

				while (iterator.hasNext())
				{
					ICurricularCourseScope scope = (ICurricularCourseScope) iterator.next();
					//if all scope have the same semester the semester correct,
					//it does not have doubts
					if (scope.getCurricularSemester() != null
						&& scope.getCurricularSemester().getSemester() != null
						&& !scope.getCurricularSemester().getSemester().equals(semester))
					{
						//if all scope don't have the same semester the semester correct,
						//the semester choosen is the actual semester
						IExecutionPeriod executionPeriod =
							persistentExecutionPeriod.readActualExecutionPeriod();
						if (executionPeriod != null)
						{
							semester = executionPeriod.getSemester();
							break;
						}
					}
				}
			}
		}
		return semester;
	}
}