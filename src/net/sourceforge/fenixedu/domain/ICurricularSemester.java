package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public interface ICurricularSemester extends IDomainObject {

    public Integer getSemester();

    //	public List getAssociatedCurricularCourses();
    public ICurricularYear getCurricularYear();

    public List getScopes();

    //	public Integer getInternalID();

    public void setSemester(Integer code);

    //	public void setAssociatedCurricularCourses(List
    // associatedCurricularCourses);
    public void setCurricularYear(ICurricularYear curricularYear);

    public void setScopes(List scopes);
    //	public void setInternalID(Integer id);
}