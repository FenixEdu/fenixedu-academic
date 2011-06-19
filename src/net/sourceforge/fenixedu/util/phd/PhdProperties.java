package net.sourceforge.fenixedu.util.phd;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class PhdProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
	return PropertiesManager.getProperty(key);
    }

    static public String getPublicCandidacyAccessLink() {
	return getProperty("phd.public.candidacy.access.link");
    }

    static public String getPublicCandidacySubmissionLink() {
	return getProperty("phd.public.candidacy.submission.link");
    }

    static public String getPublicCandidacyRefereeFormLink() {
	return getProperty("phd.public.candidacy.referee.form.link");
    }

    static public String getPhdExternalAccessLink() {
	return getProperty("phd.public.external.access.link");
    }
    
    static public Boolean isWriteDocumentsOnDSpaceEnabled() {
	return Boolean.valueOf(getProperty("phd.write.documents.on.dspace"));
    }
}
