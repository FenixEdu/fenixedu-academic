package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IPrecedence extends IDomainObject {
    public ICurricularCourse getCurricularCourse();

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public List getRestrictions();

    public void setRestrictions(List restrictions);

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext);
}