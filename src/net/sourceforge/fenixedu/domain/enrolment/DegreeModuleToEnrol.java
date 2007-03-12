package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.GetStudentTest;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable, IDegreeModuleToEvaluate {

    private DomainReference<CurriculumGroup> curriculumGroup;

    private DomainReference<Context> context;
    
    private DomainReference<ExecutionPeriod> executionPeriod;

    public DegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context, ExecutionPeriod executionPeriod) {
	this.curriculumGroup = new DomainReference<CurriculumGroup>(curriculumGroup);
	this.context = new DomainReference<Context>(context);
	this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
    }

    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup == null) ? null : this.curriculumGroup.getObject();
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(
		curriculumGroup) : null;
    }

    public Context getContext() {
	return (this.context == null) ? null : this.context.getObject();
    }

    public void setContext(Context context) {
	this.context = (context != null) ? new DomainReference<Context>(context) : null;
    }
    
    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }

    public String getKey() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(this.getContext().getClass().getName()).append(":").append(this.getContext().getIdInternal())
		.append(",").append(this.getCurriculumGroup().getClass().getName()).append(":").append(this.getCurriculumGroup().getIdInternal())
		.append(",").append(this.getExecutionPeriod().getClass().getName()).append(":").append(this.getExecutionPeriod().getIdInternal());
	return stringBuilder.toString();
    }

    public boolean isLeaf() {
	return getDegreeModule().isLeaf();
    }

    public boolean isEnroled() {
	return false;
    }

    public boolean isOptional() {
	return false;
    }

    public boolean canCollectRules() {
	return true;
    }

    public DegreeModule getDegreeModule() {
	return getContext().getChildDegreeModule();
    }

    public Double getEctsCredits(final ExecutionPeriod executionPeriod) {
	return isLeaf() ? ((CurricularCourse) getDegreeModule()).getEctsCredits(executionPeriod) : Double.valueOf(0d);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof DegreeModuleToEnrol) {
	    final DegreeModuleToEnrol degreeModuleToEnrol = (DegreeModuleToEnrol) obj;
	    return (this.getContext().equals(degreeModuleToEnrol.getContext()) && (this
		    .getCurriculumGroup().equals(degreeModuleToEnrol.getCurriculumGroup())));
	}
	return false;
    }

    @Override
    public int hashCode() {
	final StringBuilder builder = new StringBuilder();
	builder.append("@").append(getContext().hashCode()).append("@");
	builder.append("@").append(getCurriculumGroup().hashCode()).append("@");
	return builder.hashCode();
    }

    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionPeriod executionPeriod) {
	return getDegreeModule().getCurricularRules(executionPeriod);
    }

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(
	    final ExecutionPeriod executionPeriod) {
	return getCurriculumGroup().getCurricularRules(executionPeriod);
    }
    
    public double getAccumulatedEctsCredits(final ExecutionPeriod executionPeriod) {
	if(isLeaf()) {
	    return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionPeriod, (CurricularCourse) getDegreeModule());
	} else {
	    return 0d;
	}
    }

    public String getName() {
	return getDegreeModule().getName();
    }

    public String getYearFullLabel() {
	return getContext().getCurricularPeriod().getFullLabel();
    }

    public boolean isOptionalCurricularCourse() {
	if(getDegreeModule().isLeaf()) {
	    CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
	    return curricularCourse.isOptionalCurricularCourse();
	} 
	return false;
    }

    public Double getEctsCredits() {
	return getEctsCredits(null);
    }
    
}
