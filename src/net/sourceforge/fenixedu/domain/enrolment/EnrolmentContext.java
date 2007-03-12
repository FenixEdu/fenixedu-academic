package net.sourceforge.fenixedu.domain.enrolment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolmentContext {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionPeriod executionPeriod;

    private Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<CurriculumModule> curriculumModulesToRemove;

    private CurricularRuleLevel curricularRuleLevel;

    public EnrolmentContext(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod,
	    final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol, 
	    final List<CurriculumModule> curriculumModulesToRemove,
	    final CurricularRuleLevel curricularRuleLevel) {
	
	this.studentCurricularPlan = studentCurricularPlan;
	
	this.degreeModulesToEvaluate = new HashSet<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate moduleToEnrol : degreeModulesToEnrol) {
	    if (curriculumModulesToRemove.contains(moduleToEnrol.getCurriculumGroup())) {
		throw new DomainException(
			"error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
			moduleToEnrol.getCurriculumGroup().getName().getContent());
	    }

	    this.addDegreeModuleToEvaluate(moduleToEnrol);
	}
	
	this.executionPeriod = executionPeriod;
	this.curriculumModulesToRemove = curriculumModulesToRemove;
	this.curricularRuleLevel = curricularRuleLevel;
    }

    public Set<IDegreeModuleToEvaluate> getDegreeModuleToEvaluate() {
	return degreeModulesToEvaluate;
    }

    public void addDegreeModuleToEvaluate(final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
	getDegreeModuleToEvaluate().add(degreeModuleToEvaluate);
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

    public List<CurriculumModule> getToRemove() {
	return curriculumModulesToRemove;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
        this.curricularRuleLevel = curricularRuleLevel;
    }
    
    

}
