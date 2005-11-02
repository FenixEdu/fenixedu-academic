/**
 * @author Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 */
package net.sourceforge.fenixedu.util;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * This class is responsible for handling all complex operations done to the
 * username
 * 
 * @author - Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 * 
 */
public class UsernameUtils extends FenixUtil {

    /**
     * This method is used to determine what should be the person's current
     * username. Note - this method is NOT resposible for actually removing the
     * role but or setting the new username.
     * 
     * @param person
     *            person for whom the username is being determined
     * @return a string representing what should be the person's username
     */
    public static String updateUsername(IPerson person) {

        IRole mostImportantRole = getMostImportantRole(person.getPersonRoles());

        if (mostImportantRole == null) {
            return person.getUsername();
        }
        return generateNewUsername(person.getUsername(), mostImportantRole.getRoleType(), person);

    }
    
    public static String updateIstUsername(IPerson person) {
        if (person.getIstUsername() == null) {
            String ist = "ist";
            String istUsername = null; 
            String username = person.getUsername().toUpperCase().trim();
            
            if(username.startsWith("D")) {
                istUsername = ist + sumNumber(username.substring(1), 10000);
            }else if(username.startsWith("F")) {
                istUsername = ist + sumNumber(username.substring(1), 20000);
            }else if(username.startsWith("B")) {
                istUsername = ist + sumNumber(username.substring(1), 30000);
            }else if(username.startsWith("M")) {
                istUsername = ist + sumNumber(username.substring(1), 40000);
            }else if(username.startsWith("L")) {
                istUsername = ist + sumNumber(username.substring(1), 100000);
            }
            
            return istUsername;
        }
        return person.getIstUsername();
    }

    private static String generateNewUsername(String oldUsername, RoleType roleType, IPerson person) {
        if (oldUsername.startsWith("INA") || roleType.equals(RoleType.MASTER_DEGREE_CANDIDATE)) {
            return oldUsername;
        }

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
            IStudent student = person.getStudentByType(DegreeType.DEGREE);
            return "L" + student.getNumber();
        } else if (roleType.equals(RoleType.STUDENT)) {
            IStudent student = person.getStudentByType(DegreeType.MASTER_DEGREE);
            if (student != null) {
                return "M" + student.getNumber();
            }
            student = person.getStudentByType(DegreeType.DEGREE);
            if (student != null) {
                return "L" + student.getNumber();
            }
            throw new DomainException("error.person.addingInvalidRole", RoleType.STUDENT.getName());

        } else if (roleType.equals(RoleType.GRANT_OWNER)) {
            if (person.getGrantOwner() != null) {
                return "B" + person.getGrantOwner().getNumber();
            }
        } else if (roleType.equals(RoleType.PERSON)) {
            return "P" + person.getUsername().substring(1);
        }

        return oldUsername;

    }

    /*
     * Given a list of roles returns the most important role
     */
    private static IRole getMostImportantRole(Collection<IRole> roles) {
        for (RoleType roleType : RoleType.getRolesImportance()) {
            for (IRole role : roles) {
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
