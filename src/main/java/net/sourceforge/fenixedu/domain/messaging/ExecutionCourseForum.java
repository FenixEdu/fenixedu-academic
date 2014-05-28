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

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;

import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
    }

    public ExecutionCourseForum(MultiLanguageString name, MultiLanguageString description) {
        this();
        init(name, description);
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (this.getExecutionCourse() != null) {
            getExecutionCourse().checkIfCanAddForum(name);
        }

        super.setName(name);
    }

    @Override
    public Group getReadersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getWritersGroup() {
        return getExecutionCourseMembersGroup();
    }

    @Override
    public Group getAdminGroup() {
        return TeacherGroup.get(getExecutionCourse());
    }

    private Group getExecutionCourseMembersGroup() {
        ExecutionCourse executionCourse = getExecutionCourse();
        return TeacherGroup.get(executionCourse).or(StudentGroup.get(executionCourse));
    }
}
