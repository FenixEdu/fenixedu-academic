package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.EventType;

public enum PenaltyExemptionJustificationType {
    SOCIAL_SHARE_GRANT_OWNER,

    ENROLMENT_AFTER_EQUIVALENCE,

    DIRECTIVE_COUNCIL_AUTHORIZATION;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return PenaltyExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return PenaltyExemptionJustificationType.class.getName() + "." + name();
    }

    public static List<PenaltyExemptionJustificationType> getValuesFor(EventType eventType) {
	switch (eventType) {
	case ADMINISTRATIVE_OFFICE_FEE_INSURANCE:
	    return Arrays.asList(new PenaltyExemptionJustificationType[] { ENROLMENT_AFTER_EQUIVALENCE,
		    DIRECTIVE_COUNCIL_AUTHORIZATION });
	case GRATUITY:
	    return Arrays.asList(new PenaltyExemptionJustificationType[] { SOCIAL_SHARE_GRANT_OWNER,
		    ENROLMENT_AFTER_EQUIVALENCE, DIRECTIVE_COUNCIL_AUTHORIZATION });
	default:
	    return Collections.EMPTY_LIST;
	}

    }
}
