package Dominio;

import java.util.Date;
import java.util.List;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.enrollment.EnrollmentCondition;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrollment extends IDomainObject
{
	public ICurricularCourse getCurricularCourse();
	public IStudentCurricularPlan getStudentCurricularPlan();
	public EnrolmentState getEnrolmentState();
	public IExecutionPeriod getExecutionPeriod();
	public EnrolmentEvaluationType getEnrolmentEvaluationType();
	public List getEvaluations();
	public Date getCreationDate();
	public EnrollmentCondition getCondition();

	public void setEnrolmentState(EnrolmentState state);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	public void setExecutionPeriod(IExecutionPeriod executionPeriod);
	public void setEnrolmentEvaluationType(EnrolmentEvaluationType type);
	public void setEvaluations(List list);
	public void setCreationDate(Date creationDate);
	public void setCondition(EnrollmentCondition condition);
}