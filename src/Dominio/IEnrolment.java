package Dominio;
/**
 * @author tfc130
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface IEnrolment {
	ICurricularCourse getCurricularCourse();
	IStudentCurricularPlan getStudentCurricularPlan();
	void setCurricularCourse(ICurricularCourse curricularCourse);
	void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	
	//public boolean equals(Object obj);
}