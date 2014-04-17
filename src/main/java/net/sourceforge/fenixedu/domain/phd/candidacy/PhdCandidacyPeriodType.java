package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum PhdCandidacyPeriodType {
    INSTITUTION, EPFL;

    public String getFullQualifiedName() {
        return PhdCandidacyPeriodType.class.getName() + "." + name();
    }

    public String getQualifiedName() {
        return PhdCandidacyPeriodType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getFullQualifiedName());
    }

}
