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
package org.fenixedu.academic.service.services.departmentAdmOffice;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.predicate.RolePredicates;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.collect.Sets;

public class UpdateDepartmentsCompetenceCourseManagementGroup {

    @Atomic
    public static void run(Department department, String[] add, String[] remove) {
        check(RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE);

        Group original = department.getCompetenceCourseMembersGroup();

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

        updateBolonhaManagerRoleToGroupDelta(department, original, changed);
        department.setCompetenceCourseMembersGroup(changed);
    }

    private static void updateBolonhaManagerRoleToGroupDelta(Department department, Group original, Group changed) {
        Set<User> originalMembers = original.getMembers();
        Set<User> newMembers = changed.getMembers();
        for (User user : Sets.difference(originalMembers, newMembers)) {
            Person person = user.getPerson();
            if (person.hasRole(RoleType.BOLONHA_MANAGER) && !belongsToOtherGroupsWithSameRole(department, person)) {
                RoleType.revoke(RoleType.BOLONHA_MANAGER, user);
            }
        }
        for (User user : Sets.difference(newMembers, originalMembers)) {
            Person person = user.getPerson();
            if (!person.hasRole(RoleType.BOLONHA_MANAGER)) {
                RoleType.grant(RoleType.BOLONHA_MANAGER, user);
            }
        }
    }

    private static boolean belongsToOtherGroupsWithSameRole(Department departmentWhoAsks, Person person) {
        Collection<Department> departments = Bennu.getInstance().getDepartmentsSet();
        for (Department department : departments) {
            if (department != departmentWhoAsks) {
                Group group = department.getCompetenceCourseMembersGroup();
                if (group != null && group.isMember(person.getUser())) {
                    return true;
                }
            }
        }

        for (Degree degree : Degree.readBolonhaDegrees()) {
            for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlansSet()) {
                if (dcp.getCurricularPlanMembersGroup().isMember(person.getUser())) {
                    return true;
                }
            }
        }

        return false;
    }

}