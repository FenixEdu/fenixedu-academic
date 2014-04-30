package net.sourceforge.fenixedu.domain.accounting;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum PaymentMode {
    CASH, ATM, CREDIT_SPENDING;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return PaymentMode.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return PaymentMode.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
