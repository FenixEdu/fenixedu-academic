package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.ArrayList;
import java.util.List;

public enum JustificationType {
    TIME, OCCURRENCE, BALANCE, MULTIPLE_MONTH_BALANCE, ANULATION, HALF_OCCURRENCE, HALF_MULTIPLE_MONTH_BALANCE;

    public static List<JustificationType> getJustificationTypesForJustificationMotives() {
	List<JustificationType> justificationTypes = new ArrayList<JustificationType>();
	justificationTypes.add(JustificationType.TIME);
	justificationTypes.add(JustificationType.OCCURRENCE);
	justificationTypes.add(JustificationType.HALF_OCCURRENCE);
	justificationTypes.add(JustificationType.BALANCE);
	justificationTypes.add(JustificationType.MULTIPLE_MONTH_BALANCE);
	justificationTypes.add(JustificationType.HALF_MULTIPLE_MONTH_BALANCE);
	return justificationTypes;
    }

    public static List<JustificationType> getDayJustificationTypes() {
	List<JustificationType> justificationTypes = new ArrayList<JustificationType>();
	justificationTypes.add(JustificationType.OCCURRENCE);
	justificationTypes.add(JustificationType.MULTIPLE_MONTH_BALANCE);
	return justificationTypes;
    }

    public static List<JustificationType> getHalfDayJustificationTypes() {
	List<JustificationType> justificationTypes = new ArrayList<JustificationType>();
	justificationTypes.add(JustificationType.HALF_OCCURRENCE);
	justificationTypes.add(JustificationType.HALF_MULTIPLE_MONTH_BALANCE);
	return justificationTypes;
    }

    public static List<JustificationType> getHoursJustificationTypes() {
	List<JustificationType> justificationTypes = new ArrayList<JustificationType>();
	justificationTypes.add(JustificationType.TIME);
	justificationTypes.add(JustificationType.BALANCE);
	return justificationTypes;
    }
}
