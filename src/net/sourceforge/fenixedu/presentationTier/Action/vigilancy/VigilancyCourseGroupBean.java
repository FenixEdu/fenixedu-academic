package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilancyCourseGroupBean implements Serializable {

    private List<DomainReference<ExecutionCourse>> courses;

    private List<DomainReference<ExecutionCourse>> coursesToAdd;

    private DomainReference<ExecutionCourse> externalCourse;

    private DomainReference<Unit> selectedUnit;

    private DomainReference<Department> selectedDepartment;

    private DomainReference<VigilantGroup> selectedGroup;

    private DomainReference<Unit> selectedCompetenceCourseGroupUnit;

    public VigilancyCourseGroupBean() {

        setSelectedUnit(null);
        setSelectedVigilantGroup(null);
        setSelectedDepartment(null);
        setSelectedCompetenceCourseGroupUnit(null);
        setExternalCourse(null);
        courses = new ArrayList<DomainReference<ExecutionCourse>>();
        coursesToAdd = new ArrayList<DomainReference<ExecutionCourse>>();
    }

    public ExecutionCourse getExternalCourse() {
        return externalCourse.getObject();
    }

    public void setExternalCourse(ExecutionCourse course) {
        externalCourse = new DomainReference<ExecutionCourse>(course);
    }

    public VigilantGroup getSelectedVigilantGroup() {
        return this.selectedGroup.getObject();
    }

    public void setSelectedVigilantGroup(VigilantGroup group) {
        this.selectedGroup = new DomainReference<VigilantGroup>(group);
    }

    public Unit getSelectedUnit() {
        return selectedUnit.getObject();
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = new DomainReference<Unit>(unit);
    }

    public Unit getSelectedCompetenceCourseGroupUnit() {
        return selectedCompetenceCourseGroupUnit.getObject();
    }

    public void setSelectedCompetenceCourseGroupUnit(Unit unit) {
        this.selectedCompetenceCourseGroupUnit = new DomainReference<Unit>(unit);
    }

    public Department getSelectedDepartment() {
        return selectedDepartment.getObject();
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = new DomainReference<Department>(department);
    }

    public List getCourses() {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        for (DomainReference reference : this.courses) {
            courses.add((ExecutionCourse) reference.getObject());
        }
        return courses;
    }

    public void setCourses(List<ExecutionCourse> courses) {
        this.courses = new ArrayList<DomainReference<ExecutionCourse>>();
        for (ExecutionCourse course : courses) {
            this.courses.add(new DomainReference<ExecutionCourse>(course));
        }
    }

    public List getCoursesToAdd() {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        for (DomainReference reference : this.coursesToAdd) {
            courses.add((ExecutionCourse) reference.getObject());
        }
        return courses;
    }

    public void setCoursesToAdd(List<ExecutionCourse> courses) {
        this.coursesToAdd = new ArrayList<DomainReference<ExecutionCourse>>();
        for (ExecutionCourse course : courses) {
            this.coursesToAdd.add(new DomainReference<ExecutionCourse>(course));
        }
    }

}
