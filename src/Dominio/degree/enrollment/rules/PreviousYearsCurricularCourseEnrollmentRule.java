package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 23, 2004
 */

public class PreviousYearsCurricularCourseEnrollmentRule implements IEnrollmentRule
{
	private IExecutionPeriod executionPeriod;
	private IBranch studentBranch;
	private int degreeDuration;

	public PreviousYearsCurricularCourseEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
														IExecutionPeriod executionPeriod)
	{
		this.executionPeriod = executionPeriod;
		this.studentBranch = studentCurricularPlan.getBranch();
		this.degreeDuration = studentCurricularPlan.getDegreeCurricularPlan().getDegreeDuration().intValue();
	}

	public List apply(List curricularCoursesToBeEnrolledIn)
	{
		Map map = buildHashMapForCurricularCoursesList(curricularCoursesToBeEnrolledIn);
		boolean canPassToNextYear = false;

		for (int i = 1; i <= this.degreeDuration; i++)
		{
			List curricularCourses = (List) map.get(new Integer(i));
			
			if ( (curricularCourses != null) && (!curricularCourses.isEmpty()) )
			{
				int size = curricularCourses.size();
				int counter = 0;

				for (int j = 0; j < size; j++)
				{
					ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(j);
					if (naoTemNotaLancada(curricularCourse))
					{
						counter++;
					}
				}
				
				if (counter == size)
				{
					canPassToNextYear = true;
					curricularCoursesToBeEnrolledIn.removeAll(curricularCourses);
				} else
				{
					canPassToNextYear = false;

					for (int j = (i+1); j <= this.degreeDuration; j++)
					{
						List curricularCoursesList = (List) map.get(new Integer(j));
						
						if ( (curricularCoursesList != null) && (!curricularCoursesList.isEmpty()) )
						{
							curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesList);
						}
					}
				}
			}
			
			if (!canPassToNextYear)
			{
				break;
			}
		}

		return curricularCoursesToBeEnrolledIn;
	}

	private boolean naoTemNotaLancada(ICurricularCourse curricularCourse)
	{
		// TODO [DAVID] Auto-generated method stub
		return false;
	}

	private Map buildHashMapForCurricularCoursesList(List curricularCourses)
	{
		Map map = new HashMap();
		
		List copy = new ArrayList();
		copy.addAll(curricularCourses);
		
		for (int i = 1; i <= this.degreeDuration; i++)
		{
			int size = copy.size();

			List curricularCoursesToRemove = new ArrayList();
			
			for (int j = 0; j < size; j++)
			{
				ICurricularCourse curricularCourse = (ICurricularCourse) copy.get(j);
				
				if (isCurricularCourseFromYear(i, curricularCourse))
				{
					putCurricularCourseInHashMap(map, i, curricularCourse);
					curricularCoursesToRemove.add(curricularCourse);
				}
			}
			
			copy.removeAll(curricularCoursesToRemove);
		}

		return map;
	}

	private boolean isCurricularCourseFromYear(int i, ICurricularCourse curricularCourse)
	{
		return (curricularCourse.getCurricularYearByBranchAndSemester(this.studentBranch, this.executionPeriod.getSemester())
			.getYear().intValue() == i);
	}

	private void putCurricularCourseInHashMap(Map map, int i, ICurricularCourse curricularCourse)
	{
		Integer key = new Integer(i);

		List curricularCoursesList = (List) map.get(key);
		if (curricularCoursesList == null)
		{
			curricularCoursesList = new ArrayList();
			curricularCoursesList.add(curricularCourse);
		} else
		{
			if (!curricularCoursesList.contains(curricularCourse)) {
				curricularCoursesList.add(curricularCourse);
			}
		}
		
		map.put(key, curricularCoursesList);
	}
}