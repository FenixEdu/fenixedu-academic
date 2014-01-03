package net.sourceforge.fenixedu.util.phd;

import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.annotation.ConfigurationManager;
import org.fenixedu.bennu.core.annotation.ConfigurationProperty;
import org.fenixedu.bennu.core.util.ConfigurationInvocationHandler;

public class InstitutionPhdCandidacyProcessProperties {
    @ConfigurationManager(description = "Phd Institution Public Candidacy Properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "phd.institution.public.candidacy.access.link.*")
        public Map<String, String> phdInstitutionPublicCandidacyAccessLink();

        @ConfigurationProperty(key = "phd.institution.public.candidacy.submission.link.*")
        public Map<String, String> phdInstitutionPublicCandidacySubmissionLink();

        @ConfigurationProperty(key = "phd.institution.public.candidacy.referee.form.link.*")
        public Map<String, String> phdInstitutionPublicCandidacyRefereeFormLink();

        @ConfigurationProperty(key = "phd.public.external.access.link")
        public String phdPublicExternalAccessLink();
    }

    static public String getPublicCandidacyAccessLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacyAccessLink().get(readCountryCode(locale));
    }

    static public String getPublicCandidacySubmissionLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacySubmissionLink().get(readCountryCode(locale));
    }

    static public String getPublicCandidacyRefereeFormLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacyRefereeFormLink().get(readCountryCode(locale));
    }

    static public String getPhdExternalAccessLink() {
        return getConfiguration().phdPublicExternalAccessLink();
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

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
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
}
