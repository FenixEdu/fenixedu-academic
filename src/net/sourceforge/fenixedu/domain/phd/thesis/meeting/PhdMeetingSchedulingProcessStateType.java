package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdMeetingSchedulingProcessStateType implements PhdProcessStateType {

    WAITING_FIRST_THESIS_MEETING_REQUEST,

    WAITING_FIRST_THESIS_MEETING_SCHEDULE,

    WITHOUT_THESIS_MEETING_REQUEST,

    WAITING_THESIS_MEETING_SCHEDULE;

    @Override
    public String getName() {
	return name();
    }

    @Override
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName(Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

}
