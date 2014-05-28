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
package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;

public class TutorshipManagementByEntryYearBean implements Serializable {
    private List<Tutorship> studentsList;

    private ExecutionYear executionYear;

    private Teacher teacher;

    public TutorshipManagementByEntryYearBean(ExecutionYear executionYear, Teacher teacher) {
        this.studentsList = new ArrayList<Tutorship>();
        this.executionYear = executionYear;
        this.teacher = teacher;
    }

    public List<Tutorship> getStudentsList() {
        List<Tutorship> students = new ArrayList<Tutorship>();
        for (Tutorship tutor : this.studentsList) {
            students.add(tutor);
        }
        return students;
    }

    public void setStudentsList(List<Tutorship> students) {
        this.studentsList = new ArrayList<Tutorship>();
        for (Tutorship tutor : students) {
            this.studentsList.add(tutor);
        }
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

}
