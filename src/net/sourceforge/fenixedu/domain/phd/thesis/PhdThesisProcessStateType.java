package net.sourceforge.fenixedu.domain.phd.thesis;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;

public enum PhdThesisProcessStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_JURY_CONSTITUTION,

    JURY_WAITING_FOR_VALIDATION,

    JURY_VALIDATED,

    WAITING_FOR_JURY_REPORTER_FEEDBACK,

    WAITING_FOR_THESIS_PROVISIONAL_VERSION_APPROVAL,

    WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING,

    THESIS_DISCUSSION_DATE_SCHECULED,

    CONCLUDED;

    @Override
    public String getName() {
	return name();
    }
}
