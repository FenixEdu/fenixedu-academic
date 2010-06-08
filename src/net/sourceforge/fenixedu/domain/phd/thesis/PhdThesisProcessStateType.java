package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;

public enum PhdThesisProcessStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_JURY_CONSTITUTION,

    JURY_WAITING_FOR_VALIDATION,

    JURY_VALIDATED,

    WAITING_FOR_JURY_REPORTER_FEEDBACK,

    WAITING_FOR_THESIS_MEETING_SCHEDULING,

    WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING,

    THESIS_DISCUSSION_DATE_SCHECULED,

    CONCLUDED;

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
