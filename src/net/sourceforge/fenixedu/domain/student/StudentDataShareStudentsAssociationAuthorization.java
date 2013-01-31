package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

public class StudentDataShareStudentsAssociationAuthorization extends StudentDataShareStudentsAssociationAuthorization_Base {

	public StudentDataShareStudentsAssociationAuthorization(Student student, StudentPersonalDataAuthorizationChoice authorization) {
		super();
		init(student, authorization);
	}

	@Override
	public boolean isStudentDataShareAuthorization() {
		return false;
	}
}
