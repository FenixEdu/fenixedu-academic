package net.sourceforge.fenixedu.domain.precedences;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByNumberOfCurricularCourses extends RestrictionByNumberOfCurricularCourses_Base {

    public RestrictionByNumberOfCurricularCourses() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public boolean hasNumberOfCurricularCourses() {
        return getNumberOfCurricularCourses() != null;
    }

}
