package net.sourceforge.fenixedu.util.phd;

import java.util.Locale;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

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

    static public String getPublicCandidacyAccessLink(PhdProgramPublicCandidacyHashCode candidacyProcessHashCode,
	    final Locale locale) {
	String countryCode = locale.getCountry().toUpperCase();
	String url = String.format("%s?hash=%s&locale=", getPublicCandidacyAccessLink(locale),
		candidacyProcessHashCode.getValue());

	if ("PT".equals(countryCode)) {
	    return url + "pt_PT";
	} else if ("EN".equals(countryCode)) {
	    return url + "en_EN";
	}

	throw new DomainException("unable to build url");
    }
}
