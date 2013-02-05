package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

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
