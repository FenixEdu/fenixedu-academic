package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    public static List<PhdMeetingSchedulingProcessStateType> getPossibleNextStates(final PhdMeetingSchedulingProcess process) {
	PhdMeetingSchedulingProcessStateType activeState = process.getActiveState();
	return getPossibleNextStates(activeState);
    }

    public static List<PhdMeetingSchedulingProcessStateType> getPossibleNextStates(final PhdMeetingSchedulingProcessStateType type) {

	if (type == null) {
	    return Arrays.asList(new PhdMeetingSchedulingProcessStateType[] { WAITING_FIRST_THESIS_MEETING_REQUEST,
		    WITHOUT_THESIS_MEETING_REQUEST });
	}

	switch (type) {
	case WAITING_FIRST_THESIS_MEETING_REQUEST:
	    return Collections.singletonList(WAITING_FIRST_THESIS_MEETING_SCHEDULE);
	case WAITING_FIRST_THESIS_MEETING_SCHEDULE:
	    return Collections.singletonList(WITHOUT_THESIS_MEETING_REQUEST);
	case WITHOUT_THESIS_MEETING_REQUEST:
	    return Collections.singletonList(WAITING_THESIS_MEETING_SCHEDULE);
	case WAITING_THESIS_MEETING_SCHEDULE:
	    return Collections.singletonList(WITHOUT_THESIS_MEETING_REQUEST);
	}

	return Collections.EMPTY_LIST;
    }
}
