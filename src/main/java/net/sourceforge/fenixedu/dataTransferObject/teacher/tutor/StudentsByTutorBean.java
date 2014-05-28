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
package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;

public class StudentsByTutorBean implements Serializable {
    private Teacher teacher;
    private ExecutionYear studentsEntryYear = null;
    private List<Tutorship> studentsList = new ArrayList<Tutorship>();

    public StudentsByTutorBean(Teacher teacher) {
        setTeacher(teacher);
    }

    public StudentsByTutorBean(Teacher teacher, ExecutionYear studentsEntryYear, List<Tutorship> studentsList) {
        setTeacher(teacher);
        setStudentsEntryYear(studentsEntryYear);
        setStudentsList(studentsList);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ExecutionYear getStudentsEntryYear() {
        return studentsEntryYear;
    }

    public void setStudentsEntryYear(ExecutionYear studentsEntryYear) {
        this.studentsEntryYear = studentsEntryYear;
    }

    public List<Tutorship> getStudentsList() {
        return studentsList;
    }

    public List<Tutorship> getActiveTutorshipsMatchingEntryYear() {
        List<Tutorship> matchingTutorships = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTeacher().getActiveTutorships()) {
            if (getStudentsEntryYear() == null
                    || (tutorship.getStudentCurricularPlan().getRegistration().getIngressionYear() == getStudentsEntryYear())) {
                matchingTutorships.add(tutorship);
            }
        }

        return matchingTutorships;
    }

    public void setStudentsList(List<Tutorship> students) {
        studentsList = new ArrayList<Tutorship>();
        studentsList.addAll(students);
    }
}