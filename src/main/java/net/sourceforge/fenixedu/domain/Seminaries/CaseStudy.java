/*
 * Created on 28/Jul/2003, 15:05:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Collection;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 28/Jul/2003, 15:05:18
 * 
 */
public class CaseStudy extends CaseStudy_Base {

    public CaseStudy() {
        super();

        setRootDomainObject(Bennu.getInstance());
    }

    public CaseStudy(String name, String description, String code) {
        this();
        this.setName(name);
        this.setDescription(description);
        this.setCode(code);
    }

    public static Collection<CaseStudy> getAllCaseStudies() {
        return Bennu.getInstance().getCaseStudysSet();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice> getSeminaryCandidacies() {
        return getSeminaryCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnySeminaryCandidacies() {
        return !getSeminaryCandidaciesSet().isEmpty();
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSeminaryTheme() {
        return getSeminaryTheme() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
