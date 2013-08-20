package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class UpdateDepartmentsCompetenceCourseManagementGroup {

    @Checked("RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Department department, String[] add, String[] remove) {
        List<Person> toAdd = materializePersons(add);
        List<Person> toRemove = materializePersons(remove);
        List<Person> finalList = new ArrayList<Person>();

        Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);

        Group group = department.getCompetenceCourseMembersGroup();
        if (group == null) {
            group = new FixedSetGroup();
        }

        for (Person person : group.getElements()) {

            if (!toRemove.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            } else if (person.hasRole(RoleType.BOLONHA_MANAGER) && !belongsToOtherGroupsWithSameRole(department, person)) {
                person.removeRoleByType(RoleType.BOLONHA_MANAGER);
            }
        }

        for (Person person : toAdd) {
            if (!finalList.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            }
        }

        department.setCompetenceCourseMembersGroup(new FixedSetGroup(finalList));
    }

    private static List<Person> materializePersons(String[] personsIDs) {
        if (personsIDs != null) {
            List<Person> result = new ArrayList<Person>();

            for (String personID : personsIDs) {
                result.add((Person) AbstractDomainObject.fromExternalId(personID));
            }

            return result;
        } else {
            return new ArrayList<Person>();
        }
    }

    private static void addBolonhaRole(Person person, Role bolonhaRole) {
        if (!person.hasRole(RoleType.BOLONHA_MANAGER)) {
            person.addPersonRoles(bolonhaRole);
        }
    }

    private static boolean belongsToOtherGroupsWithSameRole(Department departmentWhoAsks, Person person) {
        List<Department> departments = RootDomainObject.getInstance().getDepartments();
        for (Department department : departments) {
            if (department != departmentWhoAsks) {
                Group group = department.getCompetenceCourseMembersGroup();
                if (group != null && group.isMember(person)) {
                    return true;
                }
            }
        }

        for (Degree degree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
                Group group = dcp.getCurricularPlanMembersGroup();
                if (group != null && group.isMember(person)) {
                    return true;
                }
            }
        }

        return false;
    }

}