package Dominio;

import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrolment {

	public ICurricularCourse getCurricularCourse();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public EnrolmentState getState();
	IExecutionPeriod getExecutionPeriod();
	
	public void setState(EnrolmentState state);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	void setExecutionPeriod(IExecutionPeriod executionPeriod);
	
	public ICurricularCourse getRealCurricularCourse();
}