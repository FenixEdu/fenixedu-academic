package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.domain.Seminaries.ICandidacy;
import net.sourceforge.fenixedu.domain.Seminaries.ICaseStudyChoice;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Fernanda Quitério
 * 
 * 
 * Created at 24/Jun/2004
 *  
 */
public class InfoCandidacyWithCaseStudyChoices extends InfoCandidacy {
    public void copyFromDomain(ICandidacy candidacy) {
        super.copyFromDomain(candidacy);
        if (candidacy != null && candidacy.getCaseStudyChoices() != null) {

            setCaseStudyChoices((List) CollectionUtils.collect(candidacy.getCaseStudyChoices(),
                    new Transformer() {

                        public Object transform(Object arg0) {
                            return InfoCaseStudyChoice.newInfoFromDomain((ICaseStudyChoice) arg0);
                        }
                    }));
        }
    }

    public static InfoCandidacy newInfoFromDomain(ICandidacy candidacy) {
        InfoCandidacyWithCaseStudyChoices infoCandidacy = null;
        if (candidacy != null) {
            infoCandidacy = new InfoCandidacyWithCaseStudyChoices();
            infoCandidacy.copyFromDomain(candidacy);
        }
        return infoCandidacy;
    }

}