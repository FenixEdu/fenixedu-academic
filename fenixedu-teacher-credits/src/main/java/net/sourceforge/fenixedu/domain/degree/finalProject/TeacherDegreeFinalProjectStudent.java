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
package org.fenixedu.academic.domain.degree.finalProject;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;

public class TeacherDegreeFinalProjectStudent extends TeacherDegreeFinalProjectStudent_Base {

    public TeacherDegreeFinalProjectStudent() {
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherDegreeFinalProjectStudent(ExecutionSemester executionSemester, Teacher teacher, Registration registration) {
        this();
        setExecutionPeriod(executionSemester);
        setTeacher(teacher);
        setStudent(registration);
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setExecutionPeriod(null);
        setTeacher(null);
        setStudent(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
