package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.StudentType;

public class StudentKind extends StudentKind_Base {

    public StudentKind(final StudentType studentType, final int minCourseToEnrol,
	    final int maxCourseToEnrol, final int maxNACToEnrol) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setStudentType(studentType);
	setMinCoursesToEnrol(Integer.valueOf(minCourseToEnrol));
	setMaxCoursesToEnrol(Integer.valueOf(maxCourseToEnrol));
	setMaxNACToEnrol(Integer.valueOf(maxNACToEnrol));
    }

    public static StudentKind readByStudentType(final StudentType studentType) {
	for (final StudentKind studentKind : RootDomainObject.getInstance().getStudentKindsSet()) {
	    if (studentKind.getStudentType() == studentType) {
		return studentKind;
	    }
	}
	return null;
    }

}
