package Dominio;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IEnrolment {

	ICurricularCourse getCurricularCourse();
	IStudentCurricularPlan getStudentCurricularPlan();
	void setCurricularCourse(ICurricularCourse curricularCourse);
	void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
}