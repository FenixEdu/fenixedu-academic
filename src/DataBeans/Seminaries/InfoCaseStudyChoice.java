/*
 * Created on 5/Ago/2003, 17:12:20
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package DataBeans.Seminaries;

import DataBeans.InfoObject;
import Dominio.Seminaries.ICaseStudyChoice;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 5/Ago/2003, 17:12:20
 *  
 */
public class InfoCaseStudyChoice extends InfoObject {
    private Integer order;

    private InfoCaseStudy caseStudy;

    private InfoCandidacy candidacy;

    /**
     * @return
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param integer
     */
    public void setOrder(Integer integer) {
        order = integer;
    }

    public void copyFromDomain(ICaseStudyChoice caseStudyChoice) {
        super.copyFromDomain(caseStudyChoice);
        if (caseStudyChoice != null) {
            // setCandidacyIdInternal(caseStudyChoice.getCandidacyIdInternal());
            setCaseStudy(InfoCaseStudy.newInfoFromDomain(caseStudyChoice.getCaseStudy()));
            setOrder(caseStudyChoice.getOrder());
        }
    }

    public static InfoCaseStudyChoice newInfoFromDomain(ICaseStudyChoice caseStudyChoice) {
        InfoCaseStudyChoice infoCaseStudyChoice = null;
        if (caseStudyChoice != null) {
            infoCaseStudyChoice = new InfoCaseStudyChoice();
            infoCaseStudyChoice.copyFromDomain(caseStudyChoice);
        }
        return infoCaseStudyChoice;
    }

    /**
     * @return Returns the candidacy.
     */
    public InfoCandidacy getCandidacy() {
        return candidacy;
    }

    /**
     * @param candidacy
     *            The candidacy to set.
     */
    public void setCandidacy(InfoCandidacy candidacy) {
        this.candidacy = candidacy;
    }

    /**
     * @return Returns the caseStudy.
     */
    public InfoCaseStudy getCaseStudy() {
        return caseStudy;
    }

    /**
     * @param caseStudy
     *            The caseStudy to set.
     */
    public void setCaseStudy(InfoCaseStudy caseStudy) {
        this.caseStudy = caseStudy;
    }
}