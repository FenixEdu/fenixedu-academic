package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

public class RegistrationStateLog extends RegistrationStateLog_Base {

    public RegistrationStateLog() {
        super();
    }

    public RegistrationStateLog(Registration registration, String description) {
        super();
        if (getRegistration() == null) {
            setRegistration(registration);
        }
        setDescription(description);
    }

    public static RegistrationStateLog createRegistrationStateLog(Registration registration, String description) {
        return new RegistrationStateLog(registration, description);
    }

    public static RegistrationStateLog createRegistrationStateLog(Registration registration, String bundle, String key,
            String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createRegistrationStateLog(registration, label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getStringFromResourceBundle(bundle, key, args);
    }

    public String getPersonName() {
        return getPerson() != null ? getPerson().getName() : StringUtils.EMPTY;
    }

    public String getIstUsername() {
        return getPerson() != null ? getPerson().getIstUsername() : StringUtils.EMPTY;
    }
}
