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
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class ExecutionCourseAuditFile extends ExecutionCourseAuditFile_Base {

    public ExecutionCourseAuditFile(ExecutionCourseAudit executionCourseAudit, String filename, byte[] file) {
        super();
        setExecutionCourseAudit(executionCourseAudit);
        super.init(filename, filename, file, getPermissionGroup());
    }

    private Group getPermissionGroup() {
        Group teacherGroup = UserGroup.of(getExecutionCourseAudit().getTeacherAuditor().getPerson().getUser());
        Group studentGroup = UserGroup.of(getExecutionCourseAudit().getStudentAuditor().getPerson().getUser());
        Group pedagogicalCouncil = RoleGroup.get(RoleType.PEDAGOGICAL_COUNCIL);
        return teacherGroup.or(studentGroup).or(pedagogicalCouncil);
    }

    @Override
    public void delete() {
        setExecutionCourseAudit(null);
        super.delete();
    }

    @Deprecated
    public boolean hasExecutionCourseAudit() {
        return getExecutionCourseAudit() != null;
    }

}
