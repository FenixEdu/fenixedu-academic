package Dominio;

/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public interface IEnrolmentInOptionalCurricularCourse extends IEnrollment
{
	public ICurricularCourse getCurricularCourseForOption();
	public Integer getCurricularCourseForOptionKey();
	public void setCurricularCourseForOption(ICurricularCourse curricularCourseForOption);
	public void setCurricularCourseForOptionKey(Integer curricularCourseForOptionKey);
}