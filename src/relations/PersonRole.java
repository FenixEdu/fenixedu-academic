package relations;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PersonRole extends PersonRole_Base {
    /**
     * This method is called transparently to the programmer when he adds a role
     * a person. This method's responsabilities are: to verify if the person
     * allready has the role being added; to verify if the person meets the
     * prerequisites to add this new role; to update the username; to actually
     * add the role.
     */
    public static void add(IPerson person, IRole role) {

        // verify if the person already has the role being inserted
        if (!person.hasRole(role.getRoleType())) {
            
            // verify role dependencies and throw a DomainException in case they
            // aren't met.
            if (!verifiesDependencies(person, role)) {
                throw new DomainException("error.person.addingInvalidRole", role.getRoleType()
                        .toString());
            }

            // associate the role with the person
            PersonRole_Base.add(person, role);

        }

        person.updateUsername();
        person.updateIstUsername();
    }

    /**
     * This method is called transparently to the programmer when he removes a
     * role from a person This method's responsabilities are: to actually remove
     * the role; to remove all dependencies existant from the recently removed
     * role; to update the username.
     * 
     */
    public static void remove(IPerson person, IRole removedRole) {        
        if (removedRole != null && person.hasRole(removedRole.getRoleType())) {
            // Remove the role in case
            PersonRole_Base.remove(person, removedRole);
            // Remove role dependencies
            removeDependencies(person, removedRole);
        }

        // Update person's username according to the removal of the role
        person.updateUsername();
        person.updateIstUsername();
    }

    private static Boolean verifiesDependencies(IPerson person, IRole role) {
        switch (role.getRoleType()) {
        case COORDINATOR:
        case DIRECTIVE_COUNCIL:
        case SEMINARIES_COORDINATOR:
            return person.hasRole(RoleType.TEACHER);
        case DEGREE_ADMINISTRATIVE_OFFICE:
        case DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER:
        case DEPARTMENT_CREDITS_MANAGER:
        case GRANT_OWNER_MANAGER:
        case MASTER_DEGREE_ADMINISTRATIVE_OFFICE:
        case TREASURY:
        case CREDITS_MANAGER:
        case DEPARTMENT_MEMBER:
            return person.hasRole(RoleType.EMPLOYEE);
        case DELEGATE:
            return person.hasRole(RoleType.STUDENT);
        case PERSON:
            return true;
        default:
            return person.hasRole(RoleType.PERSON);
        }
    }

    private static void removeDependencies(IPerson person, IRole removedRole) {
        switch (removedRole.getRoleType()) {
        case PERSON:
            removeRoleIfPresent(person, RoleType.TEACHER);
            removeRoleIfPresent(person, RoleType.EMPLOYEE);
            removeRoleIfPresent(person, RoleType.STUDENT);
            removeRoleIfPresent(person, RoleType.GEP);
            removeRoleIfPresent(person, RoleType.GRANT_OWNER);
            removeRoleIfPresent(person, RoleType.MANAGER);
            removeRoleIfPresent(person, RoleType.OPERATOR);
            removeRoleIfPresent(person, RoleType.TIME_TABLE_MANAGER);
            removeRoleIfPresent(person, RoleType.WEBSITE_MANAGER);
            removeRoleIfPresent(person, RoleType.FIRST_TIME_STUDENT);
            break;

        case TEACHER:
            removeRoleIfPresent(person, RoleType.COORDINATOR);
            removeRoleIfPresent(person, RoleType.DIRECTIVE_COUNCIL);
            removeRoleIfPresent(person, RoleType.SEMINARIES_COORDINATOR);
            break;

        case EMPLOYEE:
            removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
            removeRoleIfPresent(person, RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
            removeRoleIfPresent(person, RoleType.DEPARTMENT_CREDITS_MANAGER);
            removeRoleIfPresent(person, RoleType.GRANT_OWNER_MANAGER);
            removeRoleIfPresent(person, RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
            removeRoleIfPresent(person, RoleType.TREASURY);
            removeRoleIfPresent(person, RoleType.CREDITS_MANAGER);
            removeRoleIfPresent(person, RoleType.DEPARTMENT_MEMBER);
            break;

        case STUDENT:
            removeRoleIfPresent(person, RoleType.DELEGATE);
            break;
        }
    }

    private static void removeRoleIfPresent(IPerson person, RoleType roleType) {
        IRole tmpRole = null;
        tmpRole = person.getPersonRole(roleType);
        if (tmpRole != null) {
            person.getPersonRoles().remove(tmpRole);
        }
    }

}
