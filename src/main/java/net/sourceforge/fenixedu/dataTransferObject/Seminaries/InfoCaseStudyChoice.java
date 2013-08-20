/*
 * Created on 5/Ago/2003, 17:12:20
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 5/Ago/2003, 17:12:20
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

    public void copyFromDomain(CaseStudyChoice caseStudyChoice) {
        super.copyFromDomain(caseStudyChoice);
        if (caseStudyChoice != null) {
            // setCandidacyExternalId(caseStudyChoice.getCandidacyExternalId());
            setCaseStudy(InfoCaseStudy.newInfoFromDomain(caseStudyChoice.getCaseStudy()));
            setOrder(caseStudyChoice.getOrder());
        }
    }

    public static InfoCaseStudyChoice newInfoFromDomain(CaseStudyChoice caseStudyChoice) {
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