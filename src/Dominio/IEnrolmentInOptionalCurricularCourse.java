/*
 * Created on 22/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public interface IEnrolmentInOptionalCurricularCourse extends IEnrolment {
	ICurricularCourse getCurricularCourseForOption();
	Integer getCurricularCourseForOptionKey();
	void setCurricularCourseForOption(ICurricularCourse curricularCourseForOption);
	void setCurricularCourseForOptionKey(Integer curricularCourseForOptionKey);
}