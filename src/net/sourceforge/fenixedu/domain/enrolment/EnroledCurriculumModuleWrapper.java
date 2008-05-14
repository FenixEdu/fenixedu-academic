package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class EnroledCurriculumModuleWrapper implements Serializable, IDegreeModuleToEvaluate {

    private static final long serialVersionUID = 8730987603988026373L;

    private DomainReference<CurriculumModule> curriculumModule;

    protected DomainReference<Context> context;

    private DomainReference<ExecutionSemester> executionSemester;

    public EnroledCurriculumModuleWrapper(final CurriculumModule curriculumModule, final ExecutionSemester executionSemester) {
	setCurriculumModule(curriculumModule);
	setExecutionPeriod(executionSemester);
    }

    public CurriculumModule getCurriculumModule() {
	return (this.curriculumModule != null) ? this.curriculumModule.getObject() : null;
    }

    public void setCurriculumModule(CurriculumModule curriculumModule) {
	this.curriculumModule = (curriculumModule != null) ? new DomainReference<CurriculumModule>(curriculumModule) : null;
    }

    public Context getContext() {
	if (context == null) {
	    if (!getCurriculumModule().isRoot()) {
		final CurriculumGroup parentCurriculumGroup = getCurriculumModule().getCurriculumGroup();
		for (final Context context : parentCurriculumGroup.getDegreeModule().getValidChildContexts(getExecutionPeriod())) {
		    if (context.getChildDegreeModule() == getDegreeModule()) {
			setContext(context);
			break;
		    }
		}
	    }

	}
	return (context != null) ? context.getObject() : null;
    }

    public void setContext(Context context) {
	this.context = (context != null) ? new DomainReference<Context>(context) : null;
    }

    public ExecutionSemester getExecutionPeriod() {
	return (this.executionSemester != null) ? this.executionSemester.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	this.executionSemester = (executionSemester != null) ? new DomainReference<ExecutionSemester>(executionSemester) : null;
    }

    public CurriculumGroup getCurriculumGroup() {
	return getCurriculumModule().getCurriculumGroup();
    }

    public DegreeModule getDegreeModule() {
	return getCurriculumModule().getDegreeModule();
    }

    public boolean isLeaf() {
	if (!getCurriculumModule().isLeaf()) {
	    return false;
	}
	final CurriculumLine curriculumLine = (CurriculumLine) getCurriculumModule();
	return curriculumLine.isEnrolment();
    }

    public boolean isEnroled() {
	return true;
    }

    public boolean isOptional() {
	return false;
    }

    public boolean canCollectRules() {
	if (getCurriculumModule().isLeaf()) {
	    return true;
	} else {
	    final CurriculumGroup curriculumGroup = (CurriculumGroup) getCurriculumModule();
	    return !curriculumGroup.hasAnyCurriculumModules();
	}
    }

    public Double getEctsCredits(final ExecutionSemester executionSemester) {
	return getCurriculumModule().getEctsCredits();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof EnroledCurriculumModuleWrapper) {
	    final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) obj;
	    return getCurriculumModule() == moduleEnroledWrapper.getCurriculumModule();
	}
	return false;
    }

    @Override
    public int hashCode() {
	return getCurriculumModule().hashCode();
    }

    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
	return getDegreeModule() != null ? getDegreeModule().getCurricularRules(executionSemester) : Collections.EMPTY_LIST;
    }

    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionSemester executionSemester) {
	return getCurriculumModule().isRoot() ? Collections.EMPTY_SET : getCurriculumGroup().getCurricularRules(executionSemester);
    }

    public double getAccumulatedEctsCredits(final ExecutionSemester executionSemester) {
	if (getCurriculumModule().isEnrolment()) {
	    return ((Enrolment) getCurriculumModule()).getAccumulatedEctsCredits(executionSemester);
	} else {
	    return 0d;
	}
    }

    public String getName() {
	return getCurriculumModule().getName().getContent();
    }

    public String getYearFullLabel() {
	if (getExecutionPeriod() != null) {
	    return getExecutionPeriod().getQualifiedName();
	}
	return "";
    }

    public boolean isOptionalCurricularCourse() {
	return false;
    }

    public Double getEctsCredits() {
	return getCurriculumModule().getEctsCredits();
    }

    public String getKey() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(this.getCurriculumModule().getClass().getName()).append(":").append(
		this.getCurriculumModule().getIdInternal()).append(",").append(this.getExecutionPeriod().getClass().getName())
		.append(":").append(this.getExecutionPeriod().getIdInternal());
	return stringBuilder.toString();
    }

    public boolean isEnroling() {
	return false;
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
}
