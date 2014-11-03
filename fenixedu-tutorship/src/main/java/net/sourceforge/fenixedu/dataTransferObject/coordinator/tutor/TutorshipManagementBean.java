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

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

public class TutorshipManagementBean implements Serializable {
    private String teacherId;

    private Teacher teacher;

    private String executionDegreeID;

    private String degreeCurricularPlanID;

    private Month tutorshipEndMonth;

    private Integer tutorshipEndYear;

    private Integer studentNumber;

    private Integer numberOfPastTutorships;

    private Integer numberOfCurrentTutorships;

    private List<Tutorship> studentsList;

    private TutorshipManagementBean() {
        this.studentsList = new ArrayList<Tutorship>();
        this.setDefaultFields();
    }

    public TutorshipManagementBean(String executionDegreeID, String degreeCurricularPlanID) {
        this();
        this.executionDegreeID = executionDegreeID;
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public TutorshipManagementBean(String executionDegreeID, String degreeCurricularPlanID, String teacherId) {
        this();
        this.executionDegreeID = executionDegreeID;
        this.degreeCurricularPlanID = degreeCurricularPlanID;
        this.teacherId = teacherId;
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getNumberOfCurrentTutorships() {
        return numberOfCurrentTutorships;
    }

    public void setNumberOfCurrentTutorships(Integer numberOfCurrentTutorships) {
        this.numberOfCurrentTutorships = numberOfCurrentTutorships;
    }

    public Integer getNumberOfPastTutorships() {
        return numberOfPastTutorships;
    }

    public void setNumberOfPastTutorships(Integer numberOfPastTutorships) {
        this.numberOfPastTutorships = numberOfPastTutorships;
    }

    public Month getTutorshipEndMonth() {
        return tutorshipEndMonth;
    }

    public void setTutorshipEndMonth(Month tutorshipEndMonth) {
        this.tutorshipEndMonth = tutorshipEndMonth;
    }

    public Integer getTutorshipEndYear() {
        return tutorshipEndYear;
    }

    public void setTutorshipEndYear(Integer tutorshipEndYear) {
        this.tutorshipEndYear = tutorshipEndYear;
    }

    private void setDefaultFields() {
        setTutorshipEndMonth(Month.SEPTEMBER);
        setTutorshipEndYear(Tutorship.getLastPossibleTutorshipYear());
    }
}
