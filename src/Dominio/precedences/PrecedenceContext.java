package Dominio.precedences;

import java.util.List;
import java.util.Map;

import Dominio.ICurricularCourse;
import Dominio.IExecutionPeriod;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedenceContext
{
	private List studentApprovedEnrollments;
	private List studentEnrolledEnrollments;
	private List curricularCoursesWhereStudentCanBeEnrolled;
	private IExecutionPeriod executionPeriod;
	private Map acumulatedEnrollments;

	public PrecedenceContext()
	{
	}

	public IExecutionPeriod getExecutionPeriod()
	{
		return executionPeriod;
	}

	public void setExecutionPeriod(IExecutionPeriod executionPeriod)
	{
		this.executionPeriod = executionPeriod;
	}

	public List getStudentApprovedEnrollments()
	{
		return studentApprovedEnrollments;
	}

	public void setStudentApprovedEnrollments(List studentApprovedEnrollments)
	{
		this.studentApprovedEnrollments = studentApprovedEnrollments;
	}

	public List getStudentEnrolledEnrollments()
	{
		return studentEnrolledEnrollments;
	}

	public void setStudentEnrolledEnrollments(List studentEnrolledEnrollments)
	{
		this.studentEnrolledEnrollments = studentEnrolledEnrollments;
	}

	public List getCurricularCoursesWhereStudentCanBeEnrolled()
	{
		return curricularCoursesWhereStudentCanBeEnrolled;
	}

	public void setCurricularCoursesWhereStudentCanBeEnrolled(List curricularCoursesWhereStudentCanBeEnrolled)
	{
		this.curricularCoursesWhereStudentCanBeEnrolled = curricularCoursesWhereStudentCanBeEnrolled;
	}

	public Map getAcumulatedEnrollments()
	{
		return acumulatedEnrollments;
	}

	public void setAcumulatedEnrollments(Map acumulatedEnrollments)
	{
		this.acumulatedEnrollments = acumulatedEnrollments;
	}

	public Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse)
	{
		String key =
			curricularCourse.getCode()
				+ curricularCourse.getName()
				+ curricularCourse.getDegreeCurricularPlan().getName()
				+ curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
		
		Integer curricularCourseAcumulatedEnrolments =
			(Integer) this.acumulatedEnrollments.get(key);

		if (curricularCourseAcumulatedEnrolments == null)
		{
			curricularCourseAcumulatedEnrolments = new Integer (0);
		}
		return curricularCourseAcumulatedEnrolments;
	}
}