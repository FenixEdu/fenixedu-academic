package Dominio;

import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrolment extends IDomainObject{

//	public ICurricularCourse getCurricularCourse();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public EnrolmentState getEnrolmentState();
	public IExecutionPeriod getExecutionPeriod();
	public EnrolmentEvaluationType getEnrolmentEvaluationType();
	public List getEvaluations();
	public ICurricularCourseScope getCurricularCourseScope();
	

	public void setEnrolmentState(EnrolmentState state);
//	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	public void setExecutionPeriod(IExecutionPeriod executionPeriod);
	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type);
	public void setEvaluations(List list);
	public void setCurricularCourseScope(ICurricularCourseScope scope);
	
	

//	public ICurricularCourse getRealCurricularCourse();
	
}