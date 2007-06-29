package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentPredicates {

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsStudentOwner = new AccessControlPredicate<Student>() {
	public boolean evaluate(Student student) {
	    return AccessControl.getPerson().getStudent() == student;
	}
    };

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsCoordinator = new AccessControlPredicate<Student>() {
	public boolean evaluate(Student student) {
	    return AccessControl.getPerson().hasRole(RoleType.COORDINATOR);
	}
    };

}
