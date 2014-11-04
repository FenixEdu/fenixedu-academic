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

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class PhdProperties {
    @ConfigurationManager(description = "Phd Candidacy Properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "phd.public.candidacy.access.link")
        public String getPublicCandidacyAccessLink();

        @ConfigurationProperty(key = "phd.public.candidacy.submission.link")
        public String getPublicCandidacySubmissionLink();

        @ConfigurationProperty(key = "phd.public.candidacy.referee.form.link")
        public String getPublicCandidacyRefereeFormLink();

        @ConfigurationProperty(key = "phd.public.external.access.link")
        public String getPhdExternalAccessLink();
    }

    static public String getPublicCandidacyAccessLink() {
        return getConfiguration().getPublicCandidacyAccessLink();
    }

    static public String getPublicCandidacySubmissionLink() {
        return getConfiguration().getPublicCandidacySubmissionLink();
    }

    static public String getPublicCandidacyRefereeFormLink() {
        return getConfiguration().getPublicCandidacyRefereeFormLink();
    }

    static public String getPhdExternalAccessLink() {
        return getConfiguration().getPhdExternalAccessLink();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }
}
