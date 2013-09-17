package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 */

public class University extends University_Base {

    public University() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourse> getAssociatedCurricularCourses() {
        return getAssociatedCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCurricularCourses() {
        return !getAssociatedCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
