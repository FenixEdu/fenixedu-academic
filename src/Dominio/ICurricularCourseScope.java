package Dominio;

import Util.HasAlternativeSemester;


/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourseScope {
	public IBranch getBranch();
	public ICurricularCourse getCurricularCourse();
	public ICurricularSemester getCurricularSemester();
	public HasAlternativeSemester getHasAlternativeSemester();
	
	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
	public void setHasAlternativeSemester(HasAlternativeSemester hasAlternativeSemester);
	
	public boolean hasAlternativeSemester();
}
