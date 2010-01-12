package net.sourceforge.fenixedu.domain.phd.seminar;

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

}
