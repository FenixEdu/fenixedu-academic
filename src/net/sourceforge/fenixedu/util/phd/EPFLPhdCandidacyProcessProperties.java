package net.sourceforge.fenixedu.util.phd;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

public class EPFLPhdCandidacyProcessProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
	return PropertiesManager.getProperty(key);
    }

    static public String getPublicCandidacyAccessLink() {
	return getProperty("phd.epfl.public.candidacy.access.link");
    }

    static public String getPublicCandidacySubmissionLink() {
	return getProperty("phd.epfl.public.candidacy.submission.link");
    }

    static public String getPublicCandidacyRefereeFormLink() {
	return getProperty("phd.epfl.public.candidacy.referee.form.link");
    }

    static public String getPhdExternalAccessLink() {
	return getProperty("phd.public.external.access.link");
    }

    static public String getPublicCandidacyAccessLink(PhdProgramPublicCandidacyHashCode candidacyProcessHashCode) {
	String url = String.format("%s?hash=%s", getPublicCandidacyAccessLink(), candidacyProcessHashCode.getValue());
	return url;
    }
}
