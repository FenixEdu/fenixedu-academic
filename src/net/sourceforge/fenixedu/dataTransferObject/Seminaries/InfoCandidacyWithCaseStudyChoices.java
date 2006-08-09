package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério
 * 
 * 
 * Created at 24/Jun/2004
 *  
 */
public class InfoCandidacyWithCaseStudyChoices extends InfoCandidacy {
    public void copyFromDomain(SeminaryCandidacy candidacy) {
        super.copyFromDomain(candidacy);
        if (candidacy != null && candidacy.getCaseStudyChoices() != null) {

            setCaseStudyChoices((List) CollectionUtils.collect(candidacy.getCaseStudyChoices(),
                    new Transformer() {

                        public Object transform(Object arg0) {
                            return InfoCaseStudyChoice.newInfoFromDomain((CaseStudyChoice) arg0);
                        }
                    }));
        }
    }

    public static InfoCandidacy newInfoFromDomain(SeminaryCandidacy candidacy) {
        InfoCandidacyWithCaseStudyChoices infoCandidacy = null;
        if (candidacy != null) {
            infoCandidacy = new InfoCandidacyWithCaseStudyChoices();
            infoCandidacy.copyFromDomain(candidacy);
        }
        return infoCandidacy;
    }

}