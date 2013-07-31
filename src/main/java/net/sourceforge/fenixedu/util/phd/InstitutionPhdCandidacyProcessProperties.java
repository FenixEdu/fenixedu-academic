package net.sourceforge.fenixedu.util.phd;

import java.util.Locale;

import pt.ist.bennu.core.util.ConfigurationManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.apache.commons.lang.StringUtils;

public class InstitutionPhdCandidacyProcessProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
        return ConfigurationManager.getProperty(key);
    }

    static public String getPublicCandidacyAccessLink(final Locale locale) {
        String countryCode = readCountryCode(locale);

        return getProperty("phd.institution.public.candidacy.access.link." + countryCode);
    }

    static private String readCountryCode(final Locale locale) {
        String country = locale.getCountry();
        String language = locale.getLanguage();

        String result = null;
        if (!StringUtils.isEmpty(country)) {
            result = country.toUpperCase();
        } else if (!StringUtils.isEmpty(language)) {
            result = language.toUpperCase();
        }

        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        return "PT";
    }

    static public String getPublicCandidacySubmissionLink(final Locale locale) {
        String countryCode = readCountryCode(locale);

        return getProperty("phd.institution.public.candidacy.submission.link." + countryCode);
    }

    static public String getPublicCandidacyRefereeFormLink(final Locale locale) {
        String countryCode = readCountryCode(locale);

        return getProperty("phd.institution.public.candidacy.referee.form.link." + countryCode);
    }

    static public String getPhdExternalAccessLink() {
        return getProperty("phd.public.external.access.link");
    }

    static public String getPublicCandidacyAccessLink(PhdProgramPublicCandidacyHashCode candidacyProcessHashCode,
            final Locale locale) {

        String countryCode = readCountryCode(locale);

        String url =
                String.format("%s?hash=%s&locale=", getPublicCandidacyAccessLink(locale), candidacyProcessHashCode.getValue());

        if ("PT".equals(countryCode)) {
            return url + "pt_PT";
        } else if ("EN".equals(countryCode)) {
            return url + "en_EN";
        }

        throw new DomainException("unable to build url");
    }
}
