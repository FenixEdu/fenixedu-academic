package Dominio;

import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface ICurricularSemester {

	public Integer getSemester();
//	public List getAssociatedCurricularCourses();
	public ICurricularYear getCurricularYear();
	public List getScopes();
	
	public void setSemester(Integer code);
//	public void setAssociatedCurricularCourses(List associatedCurricularCourses);
	public void setCurricularYear(ICurricularYear curricularYear);
	public void setScopes(List scopes);
}