package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class SpecialSeasonStatusTrackerBean implements Serializable{

    private static final long serialVersionUID = 7601169267648955212L;
    
    private ExecutionSemester executionSemester;
    private Department department;
    private CompetenceCourse competenceCourse;
    private List<Registration> registrations;
    
    public SpecialSeasonStatusTrackerBean() {
	super();
    }
    
    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }
    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }
    public List<Registration> getRegistrations() {
        return registrations;
    }
    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
    

}
