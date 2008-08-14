package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentGroup;

public class InfoStudentGroupWithAttendsAndGrouping extends InfoStudentGroupWithAttends {

    public void copyFromDomain(final StudentGroup studentGroup) {
	super.copyFromDomain(studentGroup);

	if (studentGroup != null) {
	    setInfoGrouping(InfoGrouping.newInfoFromDomain(studentGroup.getGrouping()));
	}
    }

    public static InfoStudentGroupWithAttendsAndGrouping newInfoFromDomain(final StudentGroup studentGroup) {
	final InfoStudentGroupWithAttendsAndGrouping infoStudentGroup;

	if (studentGroup != null) {
	    infoStudentGroup = new InfoStudentGroupWithAttendsAndGrouping();
	    infoStudentGroup.copyFromDomain(studentGroup);
	} else {
	    infoStudentGroup = null;
	}

	return infoStudentGroup;
    }

}