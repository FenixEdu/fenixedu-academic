package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolmentContext {
    
    private StudentCurricularPlan studentCurricularPlan;
    
    private ExecutionPeriod executionPeriod;
    
    private Set<DegreeModuleToEnrol> degreeModulesToEnrol;
    
    private List<CurriculumModule> curriculumModulesToRemove;
    
    public EnrolmentContext(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, Set<DegreeModuleToEnrol> degreeModulesToEnrol, List<CurriculumModule> curriculumModulesToRemove) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.degreeModulesToEnrol = degreeModulesToEnrol;
	this.executionPeriod = executionPeriod;
	this.curriculumModulesToRemove = curriculumModulesToRemove;
    }
    
    public EnrolmentContext(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, Set<DegreeModuleToEnrol> degreeModulesToEnrol) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.degreeModulesToEnrol = degreeModulesToEnrol;
	this.executionPeriod = executionPeriod;
	this.curriculumModulesToRemove = Collections.EMPTY_LIST;
    }

    public Set<DegreeModuleToEnrol> getDegreeModuleToEnrol() {
        return degreeModulesToEnrol;
    }

    public void setDegreeModuleToEnrol(Set<DegreeModuleToEnrol> degreeModulesToEnrol) {
        this.degreeModulesToEnrol = degreeModulesToEnrol;
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<CurriculumModule> getCurriculumModulesToRemove() {
        return curriculumModulesToRemove;
    }

}
