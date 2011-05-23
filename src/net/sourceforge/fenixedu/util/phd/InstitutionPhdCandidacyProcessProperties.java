package net.sourceforge.fenixedu.util.phd;

import java.util.Locale;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class InstitutionPhdCandidacyProcessProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
	return PropertiesManager.getProperty(key);
    }

    static public String getPublicCandidacyAccessLink(final Locale locale) {
	return getProperty("phd.institution.public.candidacy.access.link." + locale.getCountry().toUpperCase());
    }


    static public String getPublicCandidacySubmissionLink(final Locale locale) {
	return getProperty("phd.institution.public.candidacy.submission.link." + locale.getCountry().toUpperCase());
    }

    static public String getPublicCandidacyRefereeFormLink(final Locale locale) {
	return getProperty("phd.institution.public.candidacy.referee.form.link." + locale.getCountry().toUpperCase());
    }

    static public String getPhdExternalAccessLink() {
	return getProperty("phd.public.external.access.link");
    }
}
