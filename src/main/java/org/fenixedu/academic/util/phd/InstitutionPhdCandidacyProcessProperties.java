/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util.phd;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;
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

    }

    static public String getPublicCandidacyAccessLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacyAccessLink().get(readLanguageCode(locale));
    }

    static public String getPublicCandidacySubmissionLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacySubmissionLink().get(readLanguageCode(locale));
    }

    static public String getPublicCandidacyRefereeFormLink(final Locale locale) {
        return getConfiguration().phdInstitutionPublicCandidacyRefereeFormLink().get(readLanguageCode(locale));
    }

    static public String getPublicCandidacyAccessLink(PhdProgramPublicCandidacyHashCode candidacyProcessHashCode,
            final Locale locale) {

        return String.format("%s?hash=%s", getPublicCandidacyAccessLink(locale), candidacyProcessHashCode.getValue());
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    static private String readLanguageCode(final Locale locale) {
        String country = locale.getCountry();
        String language = locale.getLanguage();

        String result = null;
        if (!StringUtils.isEmpty(language)) {
            result = language.toUpperCase();
        } else if (!StringUtils.isEmpty(country)) {
            result = country.toUpperCase();
        }

        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        return "PT";
    }
}
