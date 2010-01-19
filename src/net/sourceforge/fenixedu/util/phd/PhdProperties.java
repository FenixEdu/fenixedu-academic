package net.sourceforge.fenixedu.util.phd;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class PhdProperties {

    static private final long serialVersionUID = 1L;

    static private String getProperty(final String key) {
	return PropertiesManager.getProperty(key);
    }

    static public String getPublicAccessLink() {
	return getProperty("phd.public.access.link");
    }

    static public String getPublicSubmissionLink() {
	return getProperty("phd.public.submission.link");
    }

    static public String getPublicRefereeFormLink() {
	return getProperty("phd.public.referee.form.link");
    }

}
