package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionByCurricularCourse extends IRestriction {
    public ICurricularCourse getPrecedentCurricularCourse();

    public void setPrecedentCurricularCourse(ICurricularCourse curricularCourse);
}