/*
 * Created on 31/Jul/2003, 9:20:49
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 31/Jul/2003, 9:20:49
 * 
 */
public class CourseEquivalency extends CourseEquivalency_Base {

    public CourseEquivalency() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setCurricularCourse(null);
        setModality(null);
        setSeminary(null);
        getThemes().clear();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.Theme> getThemes() {
        return getThemesSet();
    }

    @Deprecated
    public boolean hasAnyThemes() {
        return !getThemesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasModality() {
        return getModality() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

    @Deprecated
    public boolean hasSeminary() {
        return getSeminary() != null;
    }

}
