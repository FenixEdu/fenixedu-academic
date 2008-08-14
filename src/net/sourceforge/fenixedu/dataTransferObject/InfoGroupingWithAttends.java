package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;

public class InfoGroupingWithAttends extends InfoGrouping {

    public void copyFromDomain(Grouping grouping) {
	super.copyFromDomain(grouping);
	if (grouping != null) {
	    final List<Attends> attendsList = grouping.getAttends();
	    final List<InfoFrequenta> infoAttendsList = new ArrayList<InfoFrequenta>(attendsList.size());
	    for (final Attends attends : attendsList) {
		infoAttendsList.add(InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment.newInfoFromDomain(attends));
	    }
	    setInfoAttends(infoAttendsList);
	}
    }

    public static InfoGroupingWithAttends newInfoFromDomain(Grouping groupProperties) {
	InfoGroupingWithAttends infoGroupProperties = null;
	if (groupProperties != null) {
	    infoGroupProperties = new InfoGroupingWithAttends();
	    infoGroupProperties.copyFromDomain(groupProperties);
	}

	return infoGroupProperties;
    }

}
