package net.sourceforge.fenixedu.presentationTier.Action.commons.ects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EctsTableType {
    ENROLMENT, GRADUATION;

    public List<EctsTableLevel> getAllowedLevels() {
	switch (this) {
	case ENROLMENT:
	    return Collections.unmodifiableList(Arrays.asList(EctsTableLevel.COMPETENCE_COURSE, EctsTableLevel.DEGREE,
		    EctsTableLevel.CURRICULAR_YEAR, EctsTableLevel.SCHOOL));
	case GRADUATION:
	    return Collections.unmodifiableList(Arrays.asList(EctsTableLevel.DEGREE, EctsTableLevel.CYCLE));
	default:
	    return Collections.emptyList();
	}
    }

    public String getName() {
	return name();
    }
}
