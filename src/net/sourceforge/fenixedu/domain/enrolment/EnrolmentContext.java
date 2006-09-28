package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class EnrolmentContext {
    
    private StudentCurricularPlan studentCurricularPlan;
    
    private ExecutionPeriod executionPeriod;
    
    private Set<DegreeModuleToEnrol> degreeModulesToEnrol;
    
    public EnrolmentContext(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, Set<DegreeModuleToEnrol> degreeModulesToEnrol) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.degreeModulesToEnrol = degreeModulesToEnrol;
	this.executionPeriod = executionPeriod;
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

}
