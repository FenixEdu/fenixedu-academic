package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class RegistrationProtocolLog extends RegistrationProtocolLog_Base {
    
    public RegistrationProtocolLog(String description) {
        super();
        setRootDomainObjectRegistrationProtocolLog(Bennu.getInstance());
        setDescription(description);
    }

    public static RegistrationProtocolLog createRegistrationProtocolLog(String description) {
        return new RegistrationProtocolLog(description);
    }

    public static RegistrationProtocolLog createLog(String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createRegistrationProtocolLog(label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }

}
