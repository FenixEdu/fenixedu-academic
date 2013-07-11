/*
 * Created on 29/Jul/2003, 14:09:34
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 29/Jul/2003, 14:09:34
 * 
 */
public class CaseStudyChoice extends CaseStudyChoice_Base {

    public CaseStudyChoice() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setCaseStudy(null);
        setCandidacy(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public Integer getOrder() {
        return super.getPreferenceOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setPreferenceOrder(order);
    }

    @Deprecated
    public boolean hasPreferenceOrder() {
        return getPreferenceOrder() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCandidacy() {
        return getCandidacy() != null;
    }

    @Deprecated
    public boolean hasCaseStudy() {
        return getCaseStudy() != null;
    }

}
