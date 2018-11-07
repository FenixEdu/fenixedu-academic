package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class InterestRateLog extends InterestRateLog_Base {

    public InterestRateLog(String description) {
        super();
        setRootDomainObjectInterestRateLog(Bennu.getInstance());
        setDescription(description);
    }

    public static InterestRateLog createInterestRateLog(String description) {
        return new InterestRateLog(description);
    }

    public static InterestRateLog createLog(String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createInterestRateLog(label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }

}
