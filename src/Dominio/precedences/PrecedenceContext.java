package Dominio.precedences;

import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedenceContext
{
//	private List curricularCoursesWhereStudentCanBeEnrolled;
	private List curricularCourses2Enroll;
	private IExecutionPeriod executionPeriod;
	private IStudentCurricularPlan studentCurricularPlan;

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

    public List getCurricularCourses2Enroll() {
        return curricularCourses2Enroll;
    }

    public void setCurricularCourses2Enroll(List curricularCourses2Enroll) {
        this.curricularCourses2Enroll = curricularCourses2Enroll;
    }
	
//    public List getCurricularCoursesWhereStudentCanBeEnrolled()
//	{
//		return curricularCoursesWhereStudentCanBeEnrolled;
//	}
//
//	public void setCurricularCoursesWhereStudentCanBeEnrolled(List curricularCoursesWhereStudentCanBeEnrolled)
//	{
//		this.curricularCoursesWhereStudentCanBeEnrolled = curricularCoursesWhereStudentCanBeEnrolled;
//	}

	public IStudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
	}
}