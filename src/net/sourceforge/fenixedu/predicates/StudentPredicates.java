package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentPredicates {

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsStudentOwnerOrManager = new AccessControlPredicate<Student>() {
	public boolean evaluate(Student student) {
	    final Person person = AccessControl.getPerson();
	    return person.getStudent() == student || person.hasRole(RoleType.MANAGER);
	}
    };

    public static final AccessControlPredicate<Student> checkIfLoggedPersonIsCoordinator = new AccessControlPredicate<Student>() {
	public boolean evaluate(Student student) {
	    return AccessControl.getPerson().hasRole(RoleType.COORDINATOR);
	}
    };

}
