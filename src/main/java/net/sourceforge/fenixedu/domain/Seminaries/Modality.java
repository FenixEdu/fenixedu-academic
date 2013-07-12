/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at Jul 23, 2003, 10:15:55 AM
 * 
 */
public class Modality extends Modality_Base {

    public Modality() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static Collection<Modality> getAllModalities() {
        return RootDomainObject.getInstance().getModalitys();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy> getAssociatedModalities() {
        return getAssociatedModalitiesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedModalities() {
        return !getAssociatedModalitiesSet().isEmpty();
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

}
