package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public interface ICurricularCourseScope extends IDomainObject {
    public IBranch getBranch();

    public ICurricularCourse getCurricularCourse();

    public ICurricularSemester getCurricularSemester();

    public Calendar getBeginDate();

    public Calendar getEndDate();

    public void setBranch(IBranch branch);

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public void setCurricularSemester(ICurricularSemester curricularSemester);

    public void setBeginDate(Calendar beginDate);

    public void setEndDate(Calendar endDate);

    public Boolean isActive();
}