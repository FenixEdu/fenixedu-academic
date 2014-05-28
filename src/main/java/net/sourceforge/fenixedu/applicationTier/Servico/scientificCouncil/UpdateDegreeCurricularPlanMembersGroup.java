/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Sets;

public class UpdateDegreeCurricularPlanMembersGroup {

    @Atomic
    public static void run(DegreeCurricularPlan degreeCurricularPlan, String[] add, String[] remove) {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        Group original = degreeCurricularPlan.getCurricularPlanMembersGroup();

        Group changed = original;
        if (add != null) {
            for (String personID : add) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.grant(person.getUser());
            }
        }
        if (remove != null) {
            for (String personID : remove) {
                Person person = FenixFramework.getDomainObject(personID);
                changed = changed.revoke(person.getUser());
            }
        }

        updateBolonhaManagerRoleToGroupDelta(degreeCurricularPlan, original, changed);
        degreeCurricularPlan.setCurricularPlanMembersGroup(changed);
    }

    private static void updateBolonhaManagerRoleToGroupDelta(DegreeCurricularPlan degreeCurricularPlan, Group original,
            Group changed) {
        Set<User> originalMembers = original.getMembers();
        Set<User> newMembers = changed.getMembers();
        for (User user : Sets.difference(originalMembers, newMembers)) {
            Person person = user.getPerson();
            if (person.hasRole(RoleType.BOLONHA_MANAGER) && !belongsToOtherGroupsWithSameRole(degreeCurricularPlan, person)) {
                person.removeRoleByType(RoleType.BOLONHA_MANAGER);
            }
        }
        Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);
        for (User user : Sets.difference(newMembers, originalMembers)) {
            Person person = user.getPerson();
            if (!person.hasRole(RoleType.BOLONHA_MANAGER)) {
                person.addPersonRoles(bolonhaRole);
            }
        }
    }

    private static boolean belongsToOtherGroupsWithSameRole(DegreeCurricularPlan dcpWhoAsks, Person person) {
        for (Degree bolonhaDegree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : bolonhaDegree.getDegreeCurricularPlans()) {
                if (dcp != dcpWhoAsks) {
                    if (dcp.getCurricularPlanMembersGroup().isMember(person.getUser())) {
                        return true;
                    }
                }
            }
        }

        Collection<Department> departments = Bennu.getInstance().getDepartmentsSet();
        for (Department department : departments) {
            Group group = department.getCompetenceCourseMembersGroup();
            if (group != null && group.isMember(person.getUser())) {
                return true;
            }
        }

        return false;
    }

}