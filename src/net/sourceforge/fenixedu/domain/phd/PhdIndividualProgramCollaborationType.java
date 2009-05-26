package net.sourceforge.fenixedu.domain.phd;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramCollaborationType {

    CMU,

    UT_AUSTIN,

    MIT,

    NONE,

    OTHER;

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return PhdIndividualProgramCollaborationType.class.getSimpleName() + "." + name();
    }

}
