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
	public Integer getMaxIncrementNac();
	public Integer getMinIncrementNac();
	public Integer getWeigth();

	public void setMaxIncrementNac(Integer integer);
	public void setMinIncrementNac(Integer integer);
	public void setWeigth(Integer integer);
	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
}
