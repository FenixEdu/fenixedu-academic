package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdJuryElementsRatificationEntity {
    BY_COORDINATOR, BY_SCIENTIC_COUNCIL;

    public String getName() {
	return name();
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }
}
