/*
 * Created on 29/Jul/2003, 14:09:34
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import Dominio.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 14:09:34
 *  
 */
public class CaseStudyChoice extends DomainObject implements ICaseStudyChoice {
    private Integer order;

    private Integer caseStudyIdInternal;

    private ICaseStudy caseStudy;

    private Integer candidacyIdInternal;

    private ICandidacy candidacy;

    public CaseStudyChoice() {
    }

    /**
     * @return
     */
    public Integer getCandidacyIdInternal() {
        return candidacyIdInternal;
    }

    /**
     * @return
     */
    public Integer getCaseStudyIdInternal() {
        return caseStudyIdInternal;
    }

    /**
     * @return
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param integer
     */
    public void setCandidacyIdInternal(Integer integer) {
        candidacyIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setCaseStudyIdInternal(Integer integer) {
        caseStudyIdInternal = integer;
    }

    /**
     * @param integer
     */
    public void setOrder(Integer integer) {
        order = integer;
    }

    /**
     * @return Returns the candidacy.
     */
    public ICandidacy getCandidacy() {
        return candidacy;
    }

    /**
     * @param candidacy
     *            The candidacy to set.
     */
    public void setCandidacy(ICandidacy candidacy) {
        this.candidacy = candidacy;
    }

    /**
     * @return Returns the caseStudy.
     */
    public ICaseStudy getCaseStudy() {
        return caseStudy;
    }

    /**
     * @param caseStudy
     *            The caseStudy to set.
     */
    public void setCaseStudy(ICaseStudy caseStudy) {
        this.caseStudy = caseStudy;
    }
}