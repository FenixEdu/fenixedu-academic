package ServidorAplicacao.strategy.enrolment.context;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos
 * Jan 27, 2004
 */

public final class StudentEnrolmentContext
{
	/**
	 * Map like this: key = curricularCourseCode + curricularCourseName
	 */
	private Map acumulatedEnrolments;
	private IExecutionPeriod executionPeriod;
	private IStudentCurricularPlan studentCurricularPlan;
	private List studentApprovedEnrollments;
	private List studentCurrentSemesterEnrollments;
	private List finalCurricularCoursesWhereStudentCanBeEnrolled;

	public StudentEnrolmentContext() {}

	/**
	 * @return Returns the acumulatedEnrolments.
	 */
	public Map getAcumulatedEnrolments()
	{
		return acumulatedEnrolments;
	}

	/**
	 * @param acumulatedEnrolments The acumulatedEnrolments to set.
	 */
	public void setAcumulatedEnrolments(Map acumulatedEnrolments)
	{
		this.acumulatedEnrolments = acumulatedEnrolments;
	}

	/**
	 * @return Returns the executionPeriod.
	 */
	public IExecutionPeriod getExecutionPeriod()
	{
		return executionPeriod;
	}

	/**
	 * @param executionPeriod The executionPeriod to set.
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod)
	{
		this.executionPeriod = executionPeriod;
	}

	/**
	 * @return Returns the finalCurricularCoursesWhereStudentCanBeEnrolled.
	 */
	public List getFinalCurricularCoursesWhereStudentCanBeEnrolled()
	{
		return finalCurricularCoursesWhereStudentCanBeEnrolled;
	}

	/**
	 * @param finalCurricularCoursesWhereStudentCanBeEnrolled The finalCurricularCoursesWhereStudentCanBeEnrolled to set.
	 */
	public void setFinalCurricularCoursesWhereStudentCanBeEnrolled(List finalCurricularCoursesWhereStudentCanBeEnrolled)
	{
		this.finalCurricularCoursesWhereStudentCanBeEnrolled = finalCurricularCoursesWhereStudentCanBeEnrolled;
	}

	/**
	 * @return Returns the studentApprovedEnrollments.
	 */
	public List getStudentApprovedEnrollments()
	{
		return studentApprovedEnrollments;
	}

	/**
	 * @param studentApprovedEnrollments The studentApprovedEnrollments to set.
	 */
	public void setStudentApprovedEnrollments(List studentApprovedEnrollments)
	{
		this.studentApprovedEnrollments = studentApprovedEnrollments;
	}

	/**
	 * @return Returns the studentCurrentSemesterEnrollments.
	 */
	public List getStudentCurrentSemesterEnrollments()
	{
		return studentCurrentSemesterEnrollments;
	}

	/**
	 * @param studentCurrentSemesterEnrollments The studentCurrentSemesterEnrollments to set.
	 */
	public void setStudentCurrentSemesterEnrollments(List studentCurrentSemesterEnrollments)
	{
		this.studentCurrentSemesterEnrollments = studentCurrentSemesterEnrollments;
	}

	/**
	 * @return Returns the studentCurricularPlan.
	 */
	public IStudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	/**
	 * @param studentCurricularPlan The studentCurricularPlan to set.
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * @param curricularCourse
	 * @return curricularCourseAcumulatedEnrolments
	 */
	public Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse)
	{
		Integer curricularCourseAcumulatedEnrolments =
			(Integer) this.acumulatedEnrolments.get(curricularCourse.getCode() + curricularCourse.getName()); 
		if (curricularCourseAcumulatedEnrolments == null)
		{
			curricularCourseAcumulatedEnrolments = new Integer (0);
		}
		return curricularCourseAcumulatedEnrolments;
	}

	/**
	 * @param studentEverEnrollledCurricularCourses
	 */
	public void setAcumulatedEnrolments(List studentEverEnrollledCurricularCourses)
	{
		this.acumulatedEnrolments = CollectionUtils.getCardinalityMap(studentEverEnrollledCurricularCourses);
	}

	/**
	 * @param curricularCourse
	 * @return true/false
	 */
	public boolean isCurricularCourseDone(ICurricularCourse curricularCourse)
	{
		Iterator iterator = this.getStudentApprovedEnrollments().iterator();
		while (iterator.hasNext())
		{
			IEnrolment enrolment = (IEnrolment) iterator.next();

			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse)
			{
				if (((IEnrolmentInOptionalCurricularCourse) enrolment).getCurricularCourseForOption().equals(curricularCourse))
				{
					return true;
				}
			} else
			{
				if (enrolment.getCurricularCourse().equals(curricularCourse))
				{
					return true;
				}
			}
		}
		return false;
	}
}