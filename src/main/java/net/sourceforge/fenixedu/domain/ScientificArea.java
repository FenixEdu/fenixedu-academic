package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends ScientificArea_Base {

    public ScientificArea() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ScientificArea(final String name) {
        this();
        super.setName(name);
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
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInScientificArea> getCreditsInScientificAreas() {
        return getCreditsInScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInScientificAreas() {
        return !getCreditsInScientificAreasSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
