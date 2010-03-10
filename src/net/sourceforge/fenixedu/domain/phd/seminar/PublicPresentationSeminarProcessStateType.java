package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;

public enum PublicPresentationSeminarProcessStateType implements PhdProcessStateType {

    WAITING_FOR_COMISSION_CONSTITUTION,

    COMMISSION_WAITING_FOR_VALIDATION,

    COMMISSION_VALIDATED,

    PUBLIC_PRESENTATION_DATE_SCHEDULED,

    REPORT_WAITING_FOR_VALIDATION,

    REPORT_VALIDATED;

    @Override
    public String getName() {
	return name();
    }

    @Override
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
