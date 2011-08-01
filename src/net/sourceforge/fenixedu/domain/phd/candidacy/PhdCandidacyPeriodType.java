package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdCandidacyPeriodType {
    INSTITUTION, EPFL;

    public String getFullQualifiedName() {
	return PhdCandidacyPeriodType.class.getName() + "." + name();
    }

    public String getQualifiedName() {
	return PhdCandidacyPeriodType.class.getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getFullQualifiedName());
    }

}
