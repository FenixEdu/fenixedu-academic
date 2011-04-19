package net.sourceforge.fenixedu.util.phd;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class InstitutionPhdCandidacyProcessProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
	return PropertiesManager.getProperty(key);
    }

    static public String getPublicCandidacyAccessLink() {
	return getProperty("phd.institution.public.candidacy.access.link");
    }

    static public String getPublicCandidacySubmissionLink() {
	return getProperty("phd.institution.public.candidacy.submission.link");
    }

}
