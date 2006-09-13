/**
 * @author Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 */
package net.sourceforge.fenixedu.util;

import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentType;

/**
 * 
 * This class is responsible for handling all complex operations done to the
 * username
 * 
 * @author - Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 * 
 */
public class UsernameUtils extends FenixUtil {

    public static boolean shouldHaveUID(Person person) {
	if (person.getUsername().matches("[A-Z]+[0-9]+")) {
	    String letters = person.getUsername().replaceFirst("[0-9]+", "");
	    return (letters.equals("D") || letters.equals("F") || letters.equals("B")
		    || letters.equals("M") || letters.equals("L") || letters.equals("P"));
	} else {
	    return false;
	}

    }

    /**
         * This method is used to determine what should be the person's current
         * username. Note - this method is NOT resposible for actually removing
         * the role but or setting the new username.
         * 
         * @param person
         *                person for whom the username is being determined
         * @return a string representing what should be the person's username
         */
    public static String updateUsername(Person person) {

	Role mostImportantRole = getMostImportantRole(person.getPersonRoles());

	if (mostImportantRole == null) {
	    return person.getUsername();
	}
	try {
	    return generateNewUsername(person.getUsername(), mostImportantRole.getRoleType(), person);
	} catch (DomainException e) {
	    e.printStackTrace();
	    return person.getUsername();
	}

    }

    public static String updateIstUsername(Person person) {
	if (person.getIstUsername() == null) {
	    String ist = "ist";
	    String istUsername = null;
	    String username = person.getUsername().toUpperCase().trim();

	    if (username.startsWith("D") && person.getTeacher() != null) {
		istUsername = ist + sumNumber(username.substring(1), 10000);
	    } else if (username.startsWith("F") && person.getEmployee() != null) {
		istUsername = ist + sumNumber(username.substring(1), 20000);
	    } else if (username.startsWith("B") && person.getGrantOwner() != null) {
		istUsername = ist + sumNumber(username.substring(1), 30000);
	    } else if (username.startsWith("M")
		    && person.getStudentByType(DegreeType.MASTER_DEGREE) != null) {
		istUsername = ist + sumNumber(username.substring(1), 40000);
	    } else if (username.startsWith("T")
		    && person.getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) != null) {
		istUsername = ist + sumNumber(username.substring(1), 100000);
	    } else if (username.startsWith("L") && person.getStudentByType(DegreeType.DEGREE) != null) {
		istUsername = ist + sumNumber(username.substring(1), 100000);
	    } else if (username.startsWith("C")) {
		return person.getIstUsername();
	    } else if (username.startsWith("P")) {
		if (RootDomainObject.getInstance().getUsers().size() == 1) {
		    istUsername = ist + "0000001";
		} else {
		    User user = Collections.max(RootDomainObject.getInstance().getUsers(),
			    User.USER_UID_COMPARATOR);
		    Integer maxIstNumber = Integer.valueOf(user.getUserUId().replaceFirst("[a-zA-Z]+",
			    ""));
		    istUsername = ist + (maxIstNumber + 1);
		}
	    }

	    return istUsername;
	}
	return person.getIstUsername();
    }

    private static String generateNewUsername(String oldUsername, RoleType roleType, Person person) {

	if (roleType.equals(RoleType.TEACHER)) {
	    if (person.getTeacher() != null) {
		return "D" + person.getTeacher().getTeacherNumber();
	    } else {
		throw new DomainException("error.person.addingInvalidRole", RoleType.TEACHER.getName());
	    }
	} else if (roleType.equals(RoleType.EMPLOYEE)) {
	    if (person.getEmployee() != null) {
		return "F" + person.getEmployee().getEmployeeNumber();
	    } else {
		throw new DomainException("error.person.addingInvalidRole", RoleType.EMPLOYEE.getName());
	    }
	} else if (roleType.equals(RoleType.FIRST_TIME_STUDENT)) {
	    Registration registration = person.getStudentByType(DegreeType.DEGREE);
	    return "L" + registration.getNumber();
	} else if (roleType.equals(RoleType.STUDENT)) {

	    Registration registration = person
		    .getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	    if (registration != null) {
		StudentType studentType = registration.getStudentKind().getStudentType();
		if (studentType.equals(StudentType.NORMAL)) {
		    return "T" + registration.getNumber();
		} else if (studentType.equals(StudentType.FOREIGN_STUDENT)) {
		    return "I" + registration.getNumber(); // International
		    // students
		} else if (studentType.equals(StudentType.EXTERNAL_STUDENT)) {
		    return "A" + registration.getNumber(); // Academy
		    // students
		}
	    }

	    registration = person.getStudentByType(DegreeType.MASTER_DEGREE);
	    if (registration != null) {
		StudentType studentType = registration.getStudentKind().getStudentType();
		if (studentType.equals(StudentType.NORMAL)) {
		    return "M" + registration.getNumber();
		} else if (studentType.equals(StudentType.FOREIGN_STUDENT)) {
		    return "I" + registration.getNumber(); // International
		    // students
		} else if (studentType.equals(StudentType.EXTERNAL_STUDENT)) {
		    return "A" + registration.getNumber(); // Academy
		    // students
		}
	    }

	    registration = person.getStudentByType(DegreeType.DEGREE);
	    if (registration != null) {
		StudentType studentType = registration.getStudentKind().getStudentType();
		if (studentType.equals(StudentType.NORMAL)) {
		    return "L" + registration.getNumber();
		} else if (studentType.equals(StudentType.FOREIGN_STUDENT)) {
		    return "I" + registration.getNumber();
		} else if (studentType.equals(StudentType.EXTERNAL_STUDENT)) {
		    return "A" + registration.getNumber();
		}
	    }

	    throw new DomainException("error.person.addingInvalidRole", RoleType.STUDENT.getName());

	} else if (roleType.equals(RoleType.GRANT_OWNER)) {
	    if (person.getGrantOwner() != null) {
		return "B" + person.getGrantOwner().getNumber();
	    }
	} else if (roleType.equals(RoleType.PROJECTS_MANAGER)
		|| roleType.equals(RoleType.INSTITUCIONAL_PROJECTS_MANAGER)) {
	    return "G" + person.getIdInternal();
	} else if (roleType.equals(RoleType.ALUMNI)) {
	    Registration registration = person.getStudentByType(DegreeType.DEGREE);
	    if (registration != null) {
		return "L" + registration.getNumber();
	    }
	    throw new DomainException("error.person.addingInvalidRole", RoleType.ALUMNI.getName());
	} else if (roleType.equals(RoleType.MASTER_DEGREE_CANDIDATE)
		|| roleType.equals(RoleType.CANDIDATE)) {
	    return "C" + person.getIdInternal();
	} else if (roleType.equals(RoleType.PERSON)) {
	    return "P" + person.getIdInternal();
	}

	return oldUsername;

    }

    /*
         * Given a list of roles returns the most important role
         */
    private static Role getMostImportantRole(Collection<Role> roles) {
	for (RoleType roleType : RoleType.getRolesImportance()) {
	    for (Role role : roles) {
		if (role.getRoleType().equals(roleType)) {
		    return role;
		}
	    }
	}
	return null;
    }

    private static String sumNumber(String number, Integer sum) {
	Integer num = Integer.valueOf(number);
	Integer result = num + sum;
	return result.toString();
    }
}
