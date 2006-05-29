package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.StudentType;

public class StudentKind extends StudentKind_Base {

	public StudentKind() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public static StudentKind readByStudentType(final StudentType studentType) {
        for (final StudentKind studentKind : RootDomainObject.getInstance().getStudentKindsSet()) {
            if (studentKind.getStudentType().equals(studentType)) {
                return studentKind;
            }
        }
        return null;
    }
    
}
