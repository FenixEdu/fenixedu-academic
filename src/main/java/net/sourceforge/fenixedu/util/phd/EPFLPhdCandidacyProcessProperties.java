package net.sourceforge.fenixedu.util.phd;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramPublicCandidacyHashCode;

import org.fenixedu.bennu.core.annotation.ConfigurationManager;
import org.fenixedu.bennu.core.annotation.ConfigurationProperty;
import org.fenixedu.bennu.core.util.ConfigurationInvocationHandler;

public class EPFLPhdCandidacyProcessProperties {
    @ConfigurationManager(description = "EPFL Properties")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "phd.epfl.public.candidacy.access.link")
        public String getPublicCandidacyAccessLink();

        @ConfigurationProperty(key = "phd.epfl.public.candidacy.submission.link")
        public String getPublicCandidacySubmissionLink();

        @ConfigurationProperty(key = "phd.epfl.public.candidacy.referee.form.link")
        public String getPublicCandidacyRefereeFormLink();

        @ConfigurationProperty(key = "phd.public.external.access.link")
        public String getPhdExternalAccessLink();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    public static String getPublicCandidacyAccessLink(PhdProgramPublicCandidacyHashCode candidacyProcessHashCode) {
        return String.format("%s?hash=%s", EPFLPhdCandidacyProcessProperties.getConfiguration().getPublicCandidacyAccessLink(),
                candidacyProcessHashCode.getValue());
    }
}
