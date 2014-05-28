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

import net.sourceforge.fenixedu.domain.Teacher;

public class TutorBean implements Serializable {
    private Teacher teacher;

    private String teacherId;

    private String teacherName;

    private String executionDegreeID;

    private String degreeCurricularPlanID;

    private TutorBean() {
    }

    public TutorBean(String executionDegreeID, String degreeCurricularPlanID, Teacher teacher) {
        this();
        setExecutionDegreeID(executionDegreeID);
        setDegreeCurricularPlanID(degreeCurricularPlanID);
        setTeacher(teacher);
        setTeacherId(teacher.getPerson().getIstUsername());
        setTeacherName(teacher.getPerson().getName());
    }

    public String getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(String executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String string) {
        this.teacherId = string;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
