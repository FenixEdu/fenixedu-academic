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
package net.sourceforge.fenixedu.domain.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;

public class DepartmentForum extends DepartmentForum_Base {

    public DepartmentForum() {
        super();
    }

    @Override
    public Group getReadersGroup() {
        return getDepartmentForumGroup();
    }

    @Override
    public Group getWritersGroup() {
        return getDepartmentForumGroup();
    }

    @Override
    public Group getAdminGroup() {
        return UserGroup.of(Person.convertToUsers(getDepartmentManagers()));
    }

    @Override
    public Department getDepartment() {
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (this.equals(department.getDepartmentForum())) {
                return department;
            }
        }
        return null;
    }

    public Group getDepartmentForumGroup() {
        Set<Group> groups = new HashSet<Group>();
        Department department = getDepartment();
        groups.add(UnitGroup.recursiveWorkers(department.getDepartmentUnit()));
        groups.add(UserGroup.of(Person.convertToUsers(getPersonsFromTeachers(department))));
        return UnionGroup.of(groups);
    }

    private Collection<Person> getDepartmentManagers() {
        return getDepartment().getDepartmentUnit().getSite().getManagers();
    }

    private List<Person> getPersonsFromTeachers(Department department) {
        List<Person> persons = new ArrayList<Person>();
        for (Teacher teacher : department.getAllCurrentTeachers()) {
            persons.add(teacher.getPerson());
        }
        return persons;
    }
}
