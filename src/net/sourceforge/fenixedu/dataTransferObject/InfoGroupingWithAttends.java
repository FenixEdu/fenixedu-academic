package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;

public class InfoGroupingWithAttends extends InfoGrouping {

    public void copyFromDomain(IGrouping grouping) {
        super.copyFromDomain(grouping);
        if (grouping != null) {
            final List<IAttends> attendsList  = grouping.getAttends();
            final List<InfoFrequenta> infoAttendsList = new ArrayList<InfoFrequenta>(attendsList.size());
            for (final IAttends attends : attendsList) {
                infoAttendsList.add(InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment.newInfoFromDomain(attends));
            }
            setInfoAttends(infoAttendsList);
        }
    }

    public static InfoGroupingWithAttends newInfoFromDomain(IGrouping groupProperties) {
        InfoGroupingWithAttends infoGroupProperties = null;
        if (groupProperties != null) {
            infoGroupProperties = new InfoGroupingWithAttends();
            infoGroupProperties.copyFromDomain(groupProperties);
        }

        return infoGroupProperties;
    }

}
