package net.sourceforge.fenixedu.domain.person;

import java.util.ArrayList;
import java.util.List;

public class RoleTypeHelper {

    private RoleTypeHelper() {
	super();
    }

    public static String enumRoleTypeNamesToCSV() {
	StringBuilder retval = new StringBuilder();
	for (RoleType r : RoleType.values()) {
	    if (retval.length() > 0)
		retval.append(",");
	    retval.append(r.name());
	}
	return retval.toString();
    }

    public static String enumRoleTypeNamesToArrayFormat() {
	StringBuilder retval = new StringBuilder("{\"");
	for (RoleType r : RoleType.values()) {
	    if (retval.length() > 0)
		retval.append("\",\"");
	    retval.append(r.name());
	}
	retval.append("\"}");
	return retval.toString();
    }

    public static String enumRoleTypeLabelsToArrayFormat() {
	StringBuilder retval = new StringBuilder("{\"");
	for (RoleType r : RoleType.values()) {
	    if (retval.length() > 0)
		retval.append("\",\"");
	    retval.append(r.getDefaultLabel());
	}
	retval.append("\"}");
	return retval.toString();
    }

    public static List<String> enumRoleTypeNames() {
	ArrayList<String> retVal = new ArrayList<String>(RoleType.values().length);
	for (RoleType r : RoleType.values()) {
	    retVal.add(r.name());
	}
	return retVal;
    }

    public static List<String> enumRoleTypeLabels() {
	ArrayList<String> retVal = new ArrayList<String>(RoleType.values().length);
	for (RoleType r : RoleType.values()) {
	    retVal.add(r.getDefaultLabel());
	}
	return retVal;
    }
}
