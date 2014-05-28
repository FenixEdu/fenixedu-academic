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
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Student;

public class SpecialSeasonStudentEnrollmentBean implements Serializable {

    private Student student;
    private StudentCurricularPlan scp;
    private ExecutionSemester executionSemester;

    public SpecialSeasonStudentEnrollmentBean() {
        super();
    }

    public SpecialSeasonStudentEnrollmentBean(Student student) {
        this();
        setStudent(student);
    }

    public SpecialSeasonStudentEnrollmentBean(Student student, StudentCurricularPlan scp) {
        this();
        setStudent(student);
        setScp(scp);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentCurricularPlan getScp() {
        return scp;
    }

    public void setScp(StudentCurricularPlan scp) {
        this.scp = scp;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

}
