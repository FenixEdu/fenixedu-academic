package net.sourceforge.fenixedu.util.phd;

import pt.ist.bennu.core.annotation.ConfigurationManager;
import pt.ist.bennu.core.annotation.ConfigurationProperty;
import pt.ist.bennu.core.util.ConfigurationInvocationHandler;

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
