package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum ApplicantOverallPromise {

    OUTSTANDING,

    EXCELLENT,

    GOOD,

    ABOVE_AVERAGE,

    BELOW_AVERAGE,

    UNABLE_TO_COMMENT;

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }
}
