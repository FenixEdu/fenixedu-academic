package Dominio;

import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrolment {

//	public ICurricularCourse getCurricularCourse();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public EnrolmentState getEnrolmentState();
	IExecutionPeriod getExecutionPeriod();
	public EnrolmentEvaluationType getEnrolmentEvaluationType();
	public List getEvaluations();
	public ICurricularCourseScope getCurricularCourseScope();
	public Integer getInternalID();

	public void setEnrolmentState(EnrolmentState state);
//	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	void setExecutionPeriod(IExecutionPeriod executionPeriod);
	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type);
	public void setEvaluations(List list);
	public void setCurricularCourseScope(ICurricularCourseScope scope);

//	public ICurricularCourse getRealCurricularCourse();
	
}