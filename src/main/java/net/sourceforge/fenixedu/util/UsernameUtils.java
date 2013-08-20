/**
 * @author Carlos Gonzalez Pereira (cgmp@mega.ist.utl.pt)
 */
package net.sourceforge.fenixedu.util;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Invitation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

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
            return person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.EMPLOYEE) || person.hasRole(RoleType.STUDENT)
                    || person.hasRole(RoleType.GRANT_OWNER) || person.hasRole(RoleType.ALUMNI)
                    || person.hasRole(RoleType.CANDIDATE) || person.hasAnyInvitation() || person.hasExternalResearchContract();
        }
        return false;
    }

    public static String updateIstUsername(Person person) {

        String currentISTUsername = person.getIstUsername();
        if (currentISTUsername == null && shouldHaveUID(person)) {

            String ist = "ist";
            String istUsername = null;

            Role mostImportantRole = getMostImportantRole(person.getPersonRoles());

            if (mostImportantRole != null && mostImportantRole.getRoleType() == RoleType.TEACHER && person.getEmployee() != null) {
                istUsername = ist + sumNumber(person.getEmployee().getEmployeeNumber(), 10000);

            } else if (mostImportantRole != null && mostImportantRole.getRoleType() == RoleType.EMPLOYEE) {
                istUsername = ist + sumNumber(person.getEmployee().getEmployeeNumber(), 20000);

            } else if (mostImportantRole != null && mostImportantRole.getRoleType() == RoleType.GRANT_OWNER
                    && person.hasEmployee()) {
                istUsername = ist + sumNumber(person.getEmployee().getEmployeeNumber(), 20000);
            } else if (mostImportantRole != null && mostImportantRole.getRoleType() == RoleType.CANDIDATE) {
                if (person.hasStudent()) {
                    return ist + sumNumber(person.getStudent().getNumber(), 100000);
                }
                return currentISTUsername;

            } else if (mostImportantRole != null
                    && (mostImportantRole.getRoleType() == RoleType.STUDENT || mostImportantRole.getRoleType() == RoleType.ALUMNI)) {

                if (person.getStudentByType(DegreeType.MASTER_DEGREE) != null) {
                    final Integer number = person.getStudentByType(DegreeType.MASTER_DEGREE).getNumber();
                    if (number < 10000) {// old master degree students
                        istUsername = ist + sumNumber(number, 40000);
                    } else {// new master degree students
                        istUsername = ist + sumNumber(number, 100000);
                    }

                } else if (person.getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) != null) {
                    istUsername =
                            ist
                                    + sumNumber(person.getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)
                                            .getNumber(), 100000);

                } else if (person.getStudentByType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) != null) {
                    istUsername =
                            ist
                                    + sumNumber(person.getStudentByType(DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA)
                                            .getNumber(), 100000);

                } else {
                    Registration registration = person.getStudentByType(DegreeType.DEGREE);
                    if (registration == null) {
                        registration = person.getStudentByType(DegreeType.BOLONHA_DEGREE);
                    }
                    if (registration == null) {
                        registration = person.getStudentByType(DegreeType.BOLONHA_MASTER_DEGREE);
                    }
                    if (registration == null) {
                        registration = person.getStudentByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
                    }
                    if (registration != null) {
                        if (registration.getRegistrationAgreement().isNormal() || person.getStudent().getNumber() > 10000) {
                            istUsername = ist + sumNumber(person.getStudent().getNumber(), 100000);

                        } else if (!registration.getRegistrationAgreement().isNormal()) {

                            final Integer number = registration.getNumber();
                            if (number > 100000) {
                                // we subtract 100000 from the external/foreign
                                // student
                                // number to get his original legacy system
                                // number
                                istUsername = ist + sumNumber(number, 50000 - 100000);
                            } else if (number < 10000) {
                                istUsername = ist + sumNumber(number, 50000);
                            } else {
                                istUsername = ist + sumNumber(number, 100000);
                            }

                        }
                    } else {
                        if (person.hasStudent() && person.getStudent().getNumber().intValue() < 10000) {
                            // phd
                            istUsername = ist + sumNumber(person.getStudent().getNumber(), 60000);
                        } else if (person.hasStudent() && person.getStudent().getNumber().intValue() >= 10000) {
                            // alumni without registration
                            istUsername = ist + sumNumber(person.getStudent().getNumber(), 100000);
                        }
                    }
                }
            } else if (person.hasAnyInvitation() || person.hasExternalResearchContract()) {
                istUsername = ist + Invitation.nextUserIDForInvitedPerson();
            }

            if (StringUtils.isEmpty(istUsername)) {
                throw new DomainException("error.setting.istUsername.not.authorized");
            }

            return istUsername;
        }

        return currentISTUsername;
    }

    public static String generateNewUsername(RoleType roleType, Person person) {

        if (roleType.equals(RoleType.TEACHER)) {
            if (person.getTeacher() != null && person.getEmployee() != null) {
                return "D" + person.getEmployee().getEmployeeNumber();
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

            Registration registration = person.getStudentByType(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
            if (registration != null) {

                if (registration.getRegistrationAgreement().isNormal()) {
                    return "T" + registration.getNumber();
                } else if (registration.getRegistrationAgreement().isMilitaryAgreement()) {
                    return "A" + registration.getNumber(); // Academy
                    // students
                } else {
                    return "I" + registration.getNumber(); // International
                    // students
                }
            }

            registration = person.getStudentByType(DegreeType.MASTER_DEGREE);
            if (registration != null) {
                return buildMasterDegreeUsername(registration);
            }

            registration = person.getStudentByType(DegreeType.BOLONHA_MASTER_DEGREE);
            if (registration != null) {
                return buildMasterDegreeUsername(registration);
            }

            registration = person.getStudentByType(DegreeType.DEGREE);
            if (registration != null) {
                return buildDegreeUsername(registration);
            }

            registration = person.getStudentByType(DegreeType.BOLONHA_DEGREE);
            if (registration != null) {
                return buildDegreeUsername(registration);
            }

            registration = person.getStudentByType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            if (registration != null) {
                return buildDegreeUsername(registration);
            }

            if (person.hasStudent()) {// phd...
                return "T" + person.getStudent().getNumber();
            }

            throw new DomainException("error.person.addingInvalidRole", RoleType.STUDENT.getName());

//        } else if (roleType.equals(RoleType.GRANT_OWNER)) {
//            if (person.getEmployee() != null) {
//                return "B" + person.getEmployee().getEmployeeNumber();
//            }

        } else if (roleType.equals(RoleType.ALUMNI)) {
            Registration registration = person.getStudentByType(DegreeType.DEGREE);
            if (registration != null) {
                return "L" + registration.getNumber();
            }
            // throw new DomainException("error.person.addingInvalidRole",
            // RoleType.ALUMNI.getName());
            return null;

        } else if (roleType.equals(RoleType.MASTER_DEGREE_CANDIDATE) || roleType.equals(RoleType.CANDIDATE)) {
            return "C" + person.getExternalId();

        } else if (roleType.equals(RoleType.PERSON)) {
            return "P" + person.getExternalId();
        }

        return null;
    }

    private static String buildMasterDegreeUsername(Registration registration) {
        if (registration.getRegistrationAgreement().isNormal()) {
            return "M" + registration.getNumber();
        } else if (registration.getRegistrationAgreement().isMilitaryAgreement()) {
            return "A" + registration.getNumber(); // Academy
            // students
        } else {
            return "I" + registration.getNumber(); // International
            // students
        }
    }

    private static String buildDegreeUsername(Registration registration) {
        if (registration.getRegistrationAgreement().isNormal()) {
            return "L" + registration.getNumber();
        } else if (registration.getRegistrationAgreement().isMilitaryAgreement()) {
            return "A" + registration.getNumber(); // Academy
            // students
        } else {
            return "I" + registration.getNumber(); // International
            // students
        }
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
