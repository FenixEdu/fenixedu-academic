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

import net.sourceforge.fenixedu.domain.Teacher;

public class TutorTutorshipsHistoryBean implements Serializable {

    private Teacher teacher;

    private List<StudentsByTutorBean> activeTutorshipsByEntryYear;

    private List<StudentsByTutorBean> pastTutorshipsByEntryYear;

    private Integer numberOfCurrentTutorships;

    private Integer numberOfPastTutorships;

    public TutorTutorshipsHistoryBean(Teacher teacher) {
        setTeacher(teacher);
        this.activeTutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>();
        this.pastTutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>();
    }

    public List<StudentsByTutorBean> getActiveTutorshipsByEntryYear() {
        return this.activeTutorshipsByEntryYear;
    }

    public void setActiveTutorshipsByEntryYear(List<StudentsByTutorBean> tutorshipsByEntryYear) {
        this.activeTutorshipsByEntryYear = tutorshipsByEntryYear;
        setNumberOfCurrentTutorships();
    }

    public List<StudentsByTutorBean> getPastTutorshipsByEntryYear() {
        return this.pastTutorshipsByEntryYear;
    }

    public void setPastTutorshipsByEntryYear(List<StudentsByTutorBean> tutorshipsByEntryYear) {
        this.pastTutorshipsByEntryYear = tutorshipsByEntryYear;
        setNumberOfPastTutorships();
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getNumberOfCurrentTutorships() {
        return this.numberOfCurrentTutorships;
    }

    private void setNumberOfCurrentTutorships() {
        Integer numberOfCurrentTutorships = 0;
        for (StudentsByTutorBean studentsByEntryYear : this.activeTutorshipsByEntryYear) {
            numberOfCurrentTutorships += studentsByEntryYear.getStudentsList().size();
        }
        this.numberOfCurrentTutorships = numberOfCurrentTutorships;
    }

    public Integer getNumberOfPastTutorships() {
        return this.numberOfPastTutorships;
    }

    private void setNumberOfPastTutorships() {
        Integer numberOfPastTutorships = 0;
        for (StudentsByTutorBean studentsByEntryYear : this.pastTutorshipsByEntryYear) {
            numberOfPastTutorships += studentsByEntryYear.getStudentsList().size();
        }
        this.numberOfPastTutorships = numberOfPastTutorships;
    }

    public Integer getNumberOfTutorships() {
        return this.numberOfCurrentTutorships + this.numberOfPastTutorships;
    }
}
