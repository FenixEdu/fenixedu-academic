package Dominio;



/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourseScope extends IDomainObject {
	public IBranch getBranch();
	public ICurricularCourse getCurricularCourse();
	public ICurricularSemester getCurricularSemester();
	public Integer getMaxIncrementNac();
	public Integer getMinIncrementNac();
	public Integer getWeigth();
	public Double getLabHours();
	public Double getPraticalHours();
	public Double getTheoPratHours();
	public Double getTheoreticalHours();

	public void setMaxIncrementNac(Integer integer);
	public void setMinIncrementNac(Integer integer);
	public void setWeigth(Integer integer);
	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
	public void setLabHours(Double double1);
	public void setPraticalHours(Double double1);
	public void setTheoPratHours(Double double1);
	public void setTheoreticalHours(Double double1);
}
