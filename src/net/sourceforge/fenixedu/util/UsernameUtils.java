/**
 * @author Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 */
package net.sourceforge.fenixedu.util;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentType;

import org.apache.commons.lang.StringUtils;

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
	Login loginIdentification = person.getLoginIdentification();
	if (loginIdentification != null) {
	    for (LoginAlias loginAlias : loginIdentification.getAlias()) {
		if (loginAlias.getAlias().matches("[A-Z]+[0-9]+")) {
		    String letters = loginAlias.getAlias().replaceFirst("[0-9]+", "");
		    if (letters.equals("D") || letters.equals("F") || letters.equals("B")
			    || letters.equals("M") || letters.equals("L")) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public static String updateIstUsername(Person person) {

	String currentISTUsername = person.getIstUsername();
	if (currentISTUsername == null && shouldHaveUID(person)) {

	    String ist = "ist";
	    String istUsername = null;

	    Role mostImportantRole = getMostImportantRole(person.getPersonRoles());
	    
	    if (mostImportantRole.getRoleType() == RoleType.TEACHER) {
		istUsername = ist + sumNumber(person.getTeacher().getTeacherNumber(), 10000);

	    } else if (mostImportantRole.getRoleType() == RoleType.EMPLOYEE) {
		istUsername = ist + sumNumber(person.getEmployee().getEmployeeNumber(), 20000);

	    } else if (mostImportantRole.getRoleType() == RoleType.GRANT_OWNER
		    && person.getGrantOwner() != null) {
		istUsername = ist + sumNumber(person.getGrantOwner().getNumber(), 30000);

	    } else if (mostImportantRole.getRoleType() == RoleType.STUDENT
		    && person.getStudentByType(DegreeType.MASTER_DEGREE) != null) {
		istUsername = ist
			+ sumNumber(person.getStudentByType(DegreeType.MASTER_DEGREE).getNumber(), 40000);

	    } else if (mostImportantRole.getRoleType() == RoleType.STUDENT
		    && person.getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) != null) {
		istUsername = ist
			+ sumNumber(person.getStudentByType(
				DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA).getNumber(), 100000);

	    } else if (mostImportantRole.getRoleType() == RoleType.STUDENT) {
		Registration registration = person.getStudentByType(DegreeType.DEGREE);
		if (registration == null) {
		    registration = person.getStudentByType(DegreeType.BOLONHA_DEGREE);
		}
		if (registration == null) {
		    registration = person.getStudentByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
		}
		if (registration != null) {
		    istUsername = ist + sumNumber(registration.getNumber(), 100000);
		}

	    }	    
	    
	    if(StringUtils.isEmpty(istUsername)) {
		throw new DomainException("error.setting.istUsername.not.authorized");
	    }
	    
	    return istUsername;
	}
	
	return currentISTUsername;
    }

    public static String generateNewUsername(RoleType roleType, Person person) {

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

	    registration = person.getStudentByType(DegreeType.BOLONHA_DEGREE);
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

	    registration = person.getStudentByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
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
	    //throw new DomainException("error.person.addingInvalidRole", RoleType.ALUMNI.getName());
	    return null;
	    
	} else if (roleType.equals(RoleType.MASTER_DEGREE_CANDIDATE)
		|| roleType.equals(RoleType.CANDIDATE)) {
	    return "C" + person.getIdInternal();
	
	} else if (roleType.equals(RoleType.PERSON)) {
	    return "P" + person.getIdInternal();
	}
	
	return null;	
    }

    public static Role getMostImportantRole(Collection<Role> roles) {
	for (RoleType roleType : RoleType.getRolesImportance()) {
	    for (Role role : roles) {
		if (role.getRoleType().equals(roleType)) {
		    return role;
		}
	    }
	}
	return null;
    }

    private static String sumNumber(Integer number, Integer sum) {
	return Integer.toString(number.intValue() + sum.intValue());
    }
}
