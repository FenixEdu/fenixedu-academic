/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at Jul 23, 2003, 10:12:40 AM
 * 
 */
public class Theme extends Theme_Base {

    public Theme() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static List<Theme> getAllThemes() {
        return RootDomainObject.getInstance().getThemes();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CaseStudy> getCaseStudies() {
        return getCaseStudiesSet();
    }

    @Deprecated
    public boolean hasAnyCaseStudies() {
        return !getCaseStudiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy> getAssociatedCandidacies() {
        return getAssociatedCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCandidacies() {
        return !getAssociatedCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency> getCourseEquivalencies() {
        return getCourseEquivalenciesSet();
    }

    @Deprecated
    public boolean hasAnyCourseEquivalencies() {
        return !getCourseEquivalenciesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasShortName() {
        return getShortName() != null;
    }

}
