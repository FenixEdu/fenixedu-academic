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

	public ICurricularCourse getCurricularCourse();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public EnrolmentState getState();
	IExecutionPeriod getExecutionPeriod();
	public EnrolmentEvaluationType getEvaluationType();
	public String getUniversityCode();
	public List getEvaluations();
		
	public void setState(EnrolmentState state);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	void setExecutionPeriod(IExecutionPeriod executionPeriod);
	public void setEvaluationType(EnrolmentEvaluationType type);
	public void setUniversityCode(String universityCode);	
	public void setEvaluations(List list);

	public ICurricularCourse getRealCurricularCourse();
	
}