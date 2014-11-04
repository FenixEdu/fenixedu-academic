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
package org.fenixedu.academic.ui.struts.action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.vigilancy.VigilantGroup;

public class VigilancyCourseGroupBean implements Serializable {

    private List<ExecutionCourse> courses;

    private List<ExecutionCourse> coursesToAdd;

    private ExecutionCourse externalCourse;

    private Unit selectedUnit;

    private Department selectedDepartment;

    private VigilantGroup selectedGroup;

    private CompetenceCourseGroupUnit selectedCompetenceCourseGroupUnit;

    public VigilancyCourseGroupBean() {

        setSelectedUnit(null);
        setSelectedVigilantGroup(null);
        setSelectedDepartment(null);
        setSelectedCompetenceCourseGroupUnit(null);
        setExternalCourse(null);
        courses = new ArrayList<ExecutionCourse>();
        coursesToAdd = new ArrayList<ExecutionCourse>();
    }

    public ExecutionCourse getExternalCourse() {
        return externalCourse;
    }

    public void setExternalCourse(ExecutionCourse course) {
        externalCourse = course;
    }

    public VigilantGroup getSelectedVigilantGroup() {
        return this.selectedGroup;
    }

    public void setSelectedVigilantGroup(VigilantGroup group) {
        this.selectedGroup = group;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = unit;
    }

    public CompetenceCourseGroupUnit getSelectedCompetenceCourseGroupUnit() {
        return selectedCompetenceCourseGroupUnit;
    }

    public void setSelectedCompetenceCourseGroupUnit(CompetenceCourseGroupUnit unit) {
        this.selectedCompetenceCourseGroupUnit = unit;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = department;
    }

    public List<ExecutionCourse> getCourses() {
        return this.courses;
    }

    public void setCourses(List<ExecutionCourse> courses) {
        this.courses = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse course : courses) {
            this.courses.add(course);
        }
    }

    public List<ExecutionCourse> getCoursesToAdd() {
        return coursesToAdd;
    }

    public void setCoursesToAdd(List<ExecutionCourse> courses) {
        this.coursesToAdd = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse course : courses) {
            this.coursesToAdd.add(course);
        }
    }

}
