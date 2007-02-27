package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnrolmentContext {

    private StudentCurricularPlan studentCurricularPlan;

    private ExecutionPeriod executionPeriod;

    private Set<IDegreeModuleToEvaluate> degreeModulesToEvaluate;

    private List<CurriculumModule> curriculumModulesToRemove;

    private CurricularRuleLevel curricularRuleLevel;

    public EnrolmentContext(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionPeriod executionPeriod,
	    final List<CurriculumModule> curriculumModulesToRemove,
	    final CurricularRuleLevel curricularRuleLevel) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.degreeModulesToEvaluate = new HashSet<IDegreeModuleToEvaluate>();
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

    public List<CurriculumModule> getCurriculumModulesToRemove() {
	return curriculumModulesToRemove;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return curricularRuleLevel;
    }

    public void setCurricularRuleLevel(CurricularRuleLevel curricularRuleLevel) {
        this.curricularRuleLevel = curricularRuleLevel;
    }
    
    

}
