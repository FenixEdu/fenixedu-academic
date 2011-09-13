package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdThesisProcessStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_JURY_CONSTITUTION,

    JURY_WAITING_FOR_VALIDATION,

    JURY_VALIDATED,

    WAITING_FOR_JURY_REPORTER_FEEDBACK,

    WAITING_FOR_THESIS_MEETING_SCHEDULING,

    WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING,

    THESIS_DISCUSSION_DATE_SCHECULED,

    WAITING_FOR_THESIS_RATIFICATION,

    WAITING_FOR_FINAL_GRADE,

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

    public static List<PhdThesisProcessStateType> getPossibleNextStates(final PhdThesisProcess process) {
	PhdThesisProcessStateType currentState = process.getActiveState();

	return getPossibleNextStates(currentState);
    }

    public static List<PhdThesisProcessStateType> getPossibleNextStates(final PhdThesisProcessStateType type) {
	if (type == null) {
	    return Collections.singletonList(NEW);
	}

	switch (type) {
	case NEW:
	    return Arrays.asList(WAITING_FOR_JURY_CONSTITUTION, WAITING_FOR_JURY_REPORTER_FEEDBACK);
	case WAITING_FOR_JURY_CONSTITUTION:
	    return Arrays.asList(JURY_WAITING_FOR_VALIDATION, JURY_VALIDATED, WAITING_FOR_JURY_REPORTER_FEEDBACK);
	case JURY_WAITING_FOR_VALIDATION:
	    return Arrays.asList(JURY_VALIDATED, WAITING_FOR_JURY_REPORTER_FEEDBACK);
	case JURY_VALIDATED:
	    return Collections.singletonList(WAITING_FOR_JURY_REPORTER_FEEDBACK);
	case WAITING_FOR_JURY_REPORTER_FEEDBACK:
	    return Collections.singletonList(WAITING_FOR_THESIS_MEETING_SCHEDULING);
	case WAITING_FOR_THESIS_MEETING_SCHEDULING:
	    return Collections.singletonList(WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);
	case WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING:
	    return Collections.singletonList(THESIS_DISCUSSION_DATE_SCHECULED);
	case THESIS_DISCUSSION_DATE_SCHECULED:
	    return Collections.singletonList(WAITING_FOR_THESIS_RATIFICATION);
	case WAITING_FOR_THESIS_RATIFICATION:
	    return Collections.singletonList(WAITING_FOR_FINAL_GRADE);
	case WAITING_FOR_FINAL_GRADE:
	    return Collections.singletonList(CONCLUDED);
	}

	return Collections.EMPTY_LIST;
    }

}
