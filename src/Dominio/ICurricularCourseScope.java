package Dominio;


/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourseScope {
	public IBranch getBranch();
	public ICurricularCourse getCurricularCourse();
	public ICurricularSemester getCurricularSemester();

	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
}
