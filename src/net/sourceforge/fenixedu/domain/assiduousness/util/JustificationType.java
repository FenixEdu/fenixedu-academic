package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.List;

public enum JustificationType {
    TIME, OCCURRENCE, BALANCE, ANULATION;

    public static List<JustificationType> getJustificationTypesForJustificationMotives() {
	List<JustificationType> justificationTypes = new ArrayList<JustificationType>();
	justificationTypes.add(JustificationType.TIME);
	justificationTypes.add(JustificationType.OCCURRENCE);
	justificationTypes.add(JustificationType.BALANCE);
	return justificationTypes;
    }
}
