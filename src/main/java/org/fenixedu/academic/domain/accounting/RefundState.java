package org.fenixedu.academic.domain.accounting;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public enum RefundState {
    PENDING, CONCLUDED, ANNULED;


    public String getQualifiedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getClass().getSimpleName() + "." + name());
    }
}
