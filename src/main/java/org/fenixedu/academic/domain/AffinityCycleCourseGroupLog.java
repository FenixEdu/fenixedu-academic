package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class AffinityCycleCourseGroupLog extends AffinityCycleCourseGroupLog_Base {
    
    public AffinityCycleCourseGroupLog(String description) {
        super();
        setRootDomainObjectAffinityCycleCourseGroupLog(Bennu.getInstance());
        setDescription(description);
    }

    public static AffinityCycleCourseGroupLog createAffinityCycleCourseGroupLog(String description) {
        return new AffinityCycleCourseGroupLog(description);
    }

    public static AffinityCycleCourseGroupLog createLog(String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createAffinityCycleCourseGroupLog(label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }
    
}
