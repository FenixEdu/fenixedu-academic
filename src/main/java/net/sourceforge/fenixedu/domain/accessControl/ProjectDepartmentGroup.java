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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Teacher;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("project")
public class ProjectDepartmentGroup extends FenixGroup {
    private static final long serialVersionUID = -4923251690424095397L;

    @GroupArgument
    private Project project;

    private ProjectDepartmentGroup() {
        super();
    }

    private ProjectDepartmentGroup(Project project) {
        super();
        this.project = project;
    }

    public static ProjectDepartmentGroup get(Project project) {
        return new ProjectDepartmentGroup(project);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { project.getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Department department : project.getDeparmentsSet()) {
            for (Teacher teacher : department.getAllCurrentTeachers()) {
                User user = teacher.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getTeacher() == null) {
            return false;
        }
        final Teacher teacher = user.getPerson().getTeacher();
        final Department department = teacher.getCurrentWorkingDepartment();
        if (department != null) {
            return project.getDeparmentsSet().contains(department);
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentProjectDepartmentGroup.getInstance(project);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProjectDepartmentGroup) {
            return Objects.equal(project, ((ProjectDepartmentGroup) object).project);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(project);
    }
}
