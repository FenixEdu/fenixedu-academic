package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseGroup;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.TipoCurso;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 14, 2004
 */

public class EnrollmentRulesFactory
{
	private static EnrollmentRulesFactory instance = null;

	private EnrollmentRulesFactory()
	{
	}

	public static synchronized EnrollmentRulesFactory getInstance()
	{
		if (instance == null)
		{
			instance = new EnrollmentRulesFactory();
		}
		return instance;
	}

	public static synchronized void resetInstance()
	{
		if (instance != null)
		{
			instance = null;
		}
	}

	public List getListOfEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType)
	{
		if (enrollmentRuleType.equals(EnrollmentRuleType.PARTIAL))
		{
			return getListOfPartialEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.TOTAL))
		{
			return getListOfTotalEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else if (enrollmentRuleType.equals(EnrollmentRuleType.EMPTY))
		{
			return getListOfEmptyEnrollmentRules(degreeCurricularPlan, studentCurricularPlan, executionPeriod);
		} else
		{
			return null;
		}
	}

	public List getCurricularCoursesFromArea(IDegreeCurricularPlan degreeCurricularPlan, IBranch area, AreaType areaType)
	{
		try
		{
			if (degreeCurricularPlan.getDegree().getTipoCurso().equals(TipoCurso.LICENCIATURA_OBJ)
				&& (degreeCurricularPlan.getName().equals("LEEC2003/2004") || degreeCurricularPlan.getName().equals("LEIC 2003")))
			{
				return this.getCurricularCoursesFromAreaThroughGroups(area, areaType);
			} else
			{
				return this.getCurricularCoursesFromAreaThroughScopes(area);
			}
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



	
	
	
	
	
	
	private List getListOfPartialEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		// TODO [DAVID]: Add code here.
		return null;
	}

	private List getListOfTotalEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		// TODO [DAVID]: Add code here.
		return null;
	}

	private List getListOfEmptyEnrollmentRules(IDegreeCurricularPlan degreeCurricularPlan,
		IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod)
	{
		return new ArrayList();
	}
	
	private List getCurricularCoursesFromAreaThroughGroups(IBranch area, AreaType areaType) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport.getIPersistentCurricularCourseGroup();

		List groups = curricularCourseGroupDAO.readByBranchAndAreaType(area, areaType);
		
		List curricularCourses = new ArrayList();
		
		int groupsSize = groups.size();

		for (int i = 0; i < groupsSize; i++)
		{
			ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) groups.get(i);
			
			List courses = curricularCourseGroup.getCurricularCourses();

			int coursesSize = courses.size();

			for (int j = 0; j < coursesSize; j++)
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) courses.get(i);

				if (curricularCourses.contains(curricularCourse))
				{
					curricularCourses.add(curricularCourse);
				}
			}
		}
		
		return curricularCourses;
	}

	private List getCurricularCoursesFromAreaThroughScopes(IBranch area) throws ExcepcaoPersistencia
	{
		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		IPersistentCurricularCourseScope curricularCourseScopeDAO = persistentSuport.getIPersistentCurricularCourseScope();

		List scopes = curricularCourseScopeDAO.readByBranch(area);
		
		List curricularCourses = new ArrayList();
		
		int scopesSize = scopes.size();

		for (int i = 0; i < scopesSize; i++)
		{
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(i);
			
			ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();

			if (curricularCourses.contains(curricularCourse))
			{
				curricularCourses.add(curricularCourse);
			}
		}
		
		return curricularCourses;
	}
	
}