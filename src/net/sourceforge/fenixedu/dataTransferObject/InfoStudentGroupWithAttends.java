package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;

public class InfoStudentGroupWithAttends extends InfoStudentGroup {

    public void copyFromDomain(final StudentGroup studentGroup) {
	super.copyFromDomain(studentGroup);

	if (studentGroup != null) {
	    final List<Attends> attends = studentGroup.getAttends();
	    final List<InfoFrequenta> infoAttends = new ArrayList<InfoFrequenta>(attends.size());
	    setInfoAttends(infoAttends);
	    for (final Attends attend : attends) {
		infoAttends.add(InfoFrequenta.newInfoFromDomain(attend));
	    }
	}
    }

    public static InfoStudentGroupWithAttends newInfoFromDomain(final StudentGroup studentGroup) {
	final InfoStudentGroupWithAttends infoStudentGroup;

	if (studentGroup != null) {
	    infoStudentGroup = new InfoStudentGroupWithAttends();
	    infoStudentGroup.copyFromDomain(studentGroup);
	} else {
	    infoStudentGroup = null;
	}

	return infoStudentGroup;
    }

}