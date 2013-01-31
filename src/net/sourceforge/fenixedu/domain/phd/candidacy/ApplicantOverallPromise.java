package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum ApplicantOverallPromise {

	OUTSTANDING,

	EXCELLENT,

	GOOD,

	ABOVE_AVERAGE,

	BELOW_AVERAGE,

	UNABLE_TO_COMMENT;

	public String getLocalizedName() {
		return getLocalizedName(Language.getLocale());
	}

	public String getLocalizedName(final Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
	}

	public String getQualifiedName() {
		return getClass().getSimpleName() + "." + name();
	}
}
