package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdThesisFinalGrade implements IPresentableEnum {

    NOT_APPROVED,

    APPROVED,

    APPROVED_WITH_PLUS,

    APPROVED_WITH_PLUS_PLUS;

    public String getName() {
	return name();
    }

    @Override
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }
}
