package net.sourceforge.fenixedu.domain.accounting;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

}
