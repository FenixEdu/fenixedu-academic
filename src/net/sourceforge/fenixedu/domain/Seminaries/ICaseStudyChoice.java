/*
 * Created on 29/Jul/2003, 14:19:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 14:19:43
 *  
 */
public interface ICaseStudyChoice extends IDomainObject {
    public abstract Integer getOrder();

    /**
     * @param integer
     */
    public abstract void setOrder(Integer integer);

    public ICandidacy getCandidacy();

    public void setCandidacy(ICandidacy candidacy);

    public ICaseStudy getCaseStudy();

    public void setCaseStudy(ICaseStudy caseStudy);
}