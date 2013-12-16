package net.sourceforge.fenixedu.domain.precedences;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends RestrictionByCurricularCourse_Base {

    public RestrictionByCurricularCourse() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Override
    public void delete() {
        setPrecedentCurricularCourse(null);
        super.delete();
    }

    @Deprecated
    public boolean hasPrecedentCurricularCourse() {
        return getPrecedentCurricularCourse() != null;
    }

}
