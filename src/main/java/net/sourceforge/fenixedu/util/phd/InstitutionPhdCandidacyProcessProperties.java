/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util.phd;

import java.util.Locale;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

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
