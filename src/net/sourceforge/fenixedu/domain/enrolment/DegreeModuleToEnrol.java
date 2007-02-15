package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable, IDegreeModuleToEvaluate {
    
    private DomainReference<CurriculumGroup> curriculumGroup;
    private DomainReference<Context> context;
    
    public DegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context) {
	this.curriculumGroup = new DomainReference<CurriculumGroup>(curriculumGroup);
	this.context = new DomainReference<Context>(context);
    }
    
    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup == null) ? null : this.curriculumGroup.getObject();
    }
    
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }

    public Context getContext() {
	return (this.context == null) ? null : this.context.getObject();
    }

    public void setContext(Context context) {
        this.context = (context != null) ? new DomainReference<Context>(context) : null;
    }

    public String getKey() {
	return this.getContext().getClass().getName() + ":" + this.getContext().getIdInternal() + ","
		+ this.getCurriculumGroup().getClass().getName() + ":"
		+ this.getCurriculumGroup().getIdInternal(); 
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
	return getDegreeModule().getEctsCredits(executionPeriod);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DegreeModuleToEnrol) {
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
}
