package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable, IDegreeModuleToEvaluate {

    private static final long serialVersionUID = -6337658191828772384L;

    private CurriculumGroup curriculumGroup;

    private Context context;

    private ExecutionSemester executionSemester;

    protected DegreeModuleToEnrol() {
    }

    public DegreeModuleToEnrol(final CurriculumGroup curriculumGroup, final Context context,
	    final ExecutionSemester executionSemester) {
	this.curriculumGroup = curriculumGroup;
	this.context = context;
	this.executionSemester = executionSemester;
    }

    public CurriculumGroup getCurriculumGroup() {
	return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = curriculumGroup;
    }

    public Context getContext() {
	return this.context;
    }

    public void setContext(Context context) {
	this.context = context;
    }

    public ExecutionSemester getExecutionPeriod() {
	return this.executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public String getKey() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(this.getContext().getClass().getName()).append(":").append(this.getContext().getExternalId())
		.append(",").append(this.getCurriculumGroup().getClass().getName()).append(":")
		.append(this.getCurriculumGroup().getExternalId()).append(",")
		.append(this.getExecutionPeriod().getClass().getName()).append(":")
		.append(this.getExecutionPeriod().getExternalId());
	return stringBuilder.toString();
    }

    @Override
    public boolean isLeaf() {
	return getDegreeModule().isLeaf();
    }

    @Override
    final public boolean isEnroling() {
	return true;
    }

    @Override
    final public boolean isEnroled() {
	return false;
    }

    @Override
    public boolean isOptional() {
	return false;
    }

    @Override
    public boolean isDissertation() {
	return getDegreeModule().isDissertation();
    }

    public boolean canCollectRules() {
	return true;
    }

    public DegreeModule getDegreeModule() {
	return getContext().getChildDegreeModule();
    }

    public Double getEctsCredits(final ExecutionSemester executionSemester) {
	return isLeaf() ? ((CurricularCourse) getDegreeModule()).getEctsCredits(executionSemester) : Double.valueOf(0d);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof DegreeModuleToEnrol) {
	    final DegreeModuleToEnrol degreeModuleToEnrol = (DegreeModuleToEnrol) obj;
	    return (this.getContext().equals(degreeModuleToEnrol.getContext()) && (this.getCurriculumGroup()
		    .equals(degreeModuleToEnrol.getCurriculumGroup())));
	}
	return false;
    }

    @Override
    public int hashCode() {
	return getContext().hashCode() + getCurriculumGroup().hashCode();
    }

    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester) {
	return getDegreeModule().getCurricularRules(getContext(), executionSemester);
    }

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(final ExecutionSemester executionSemester) {
	return getCurriculumGroup().getCurricularRules(executionSemester);
    }

    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
	if (isLeaf()) {
	    return getCurriculumGroup().getStudentCurricularPlan().getAccumulatedEctsCredits(executionSemester,
		    (CurricularCourse) getDegreeModule());
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
	if (getDegreeModule().isLeaf()) {
	    CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
	    return curricularCourse.isOptionalCurricularCourse();
	}
	return false;
    }

    public Double getEctsCredits() {
	return getEctsCredits(getExecutionPeriod());
    }

    public boolean isFor(DegreeModule degreeModule) {
	return getDegreeModule() == degreeModule;
    }

    public boolean isAnnualCurricularCourse(ExecutionYear executionYear) {
	if (getDegreeModule().isLeaf()) {
	    return ((CurricularCourse) getDegreeModule()).isAnual(executionYear);
	}
	return false;
    }

    protected StudentCurricularPlan getStudentCurricularPlan() {
	return getCurriculumGroup().getStudentCurricularPlan();
    }
}
