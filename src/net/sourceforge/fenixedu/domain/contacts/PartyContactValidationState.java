package net.sourceforge.fenixedu.domain.contacts;

import net.sourceforge.fenixedu.util.BundleUtil;

public enum PartyContactValidationState {
    VALID, INVALID, REFUSED;

    public String getPresentationName() {
        final String key = getClass().getSimpleName() + "." + this;
        return BundleUtil.getStringFromResourceBundle("resources.EnumerationResources", key);
    };
}
