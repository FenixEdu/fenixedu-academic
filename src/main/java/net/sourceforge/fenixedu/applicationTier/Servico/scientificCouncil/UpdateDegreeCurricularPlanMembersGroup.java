package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

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

public class UpdateDegreeCurricularPlanMembersGroup {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(DegreeCurricularPlan degreeCurricularPlan, Integer[] add, Integer[] remove) {
        List<Person> toAdd = materializePersons(add);
        List<Person> toRemove = materializePersons(remove);
        List<Person> finalList = new ArrayList<Person>();

        Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);

        Group group = degreeCurricularPlan.getCurricularPlanMembersGroup();
        if (group == null) {
            group = new FixedSetGroup();
        }

        for (Person person : group.getElements()) {

            if (!toRemove.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            } else if (person.hasRole(RoleType.BOLONHA_MANAGER)
                    && !belongsToOtherGroupsWithSameRole(degreeCurricularPlan, person)) {
                person.removeRoleByType(RoleType.BOLONHA_MANAGER);
            }
        }

        for (Person person : toAdd) {
            if (!finalList.contains(person)) {
                finalList.add(person);
                addBolonhaRole(person, bolonhaRole);
            }
        }

        degreeCurricularPlan.setCurricularPlanMembersGroup(new FixedSetGroup(finalList));
    }

    private static List<Person> materializePersons(Integer[] personsIDs) {
        if (personsIDs != null) {
            List<Person> result = new ArrayList<Person>();

            for (Integer personID : personsIDs) {
                result.add((Person) RootDomainObject.getInstance().readPartyByOID(personID));
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

    private static boolean belongsToOtherGroupsWithSameRole(DegreeCurricularPlan dcpWhoAsks, Person person) {
        for (Degree bolonhaDegree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : bolonhaDegree.getDegreeCurricularPlans()) {
                if (dcp != dcpWhoAsks) {
                    Group group = dcp.getCurricularPlanMembersGroup();
                    if (group != null && group.isMember(person)) {
                        return true;
                    }
                }
            }
        }

        List<Department> departments = RootDomainObject.getInstance().getDepartments();
        for (Department department : departments) {
            Group group = department.getCompetenceCourseMembersGroup();
            if (group != null && group.isMember(person)) {
                return true;
            }
        }

        return false;
    }

}