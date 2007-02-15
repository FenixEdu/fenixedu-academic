package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class CurriculumModuleEnroledWrapper implements Serializable, IDegreeModuleToEvaluate {
    
    private DomainReference<CurriculumModule> curriculumModule;
    
    private DomainReference<Context> context;
    private DomainReference<ExecutionPeriod> executionPeriod;
    
    public CurriculumModuleEnroledWrapper(final CurriculumModule curriculumModule, final ExecutionPeriod executionPeriod) {
	setCurriculumModule(curriculumModule);
	setExecutionPeriod(executionPeriod);
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
		for (final Context context : parentCurriculumGroup.getDegreeModule().getChildContexts(getExecutionPeriod())) {
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
    
    public ExecutionPeriod getExecutionPeriod() {
	return (this.executionPeriod != null) ? this.executionPeriod.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
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
    
    public Double getEctsCredits(final ExecutionPeriod executionPeriod) {
	return getDegreeModule().getEctsCredits(executionPeriod);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CurriculumModuleEnroledWrapper) {
            final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) obj;
            return getCurriculumModule() == moduleEnroledWrapper.getCurriculumModule();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return getCurriculumModule().hashCode();
    }
}
