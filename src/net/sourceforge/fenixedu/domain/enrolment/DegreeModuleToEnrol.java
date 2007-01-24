package net.sourceforge.fenixedu.domain.enrolment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class DegreeModuleToEnrol implements Serializable{
    
    private DomainReference<Context> context;
    
    private DomainReference<CurriculumGroup> curriculumGroup;
    
    public DegreeModuleToEnrol(CurriculumGroup curriculumGroup, Context context) {
	this.curriculumGroup = new DomainReference<CurriculumGroup>(curriculumGroup);
	this.context = new DomainReference<Context>(context);
    }

    public Context getContext() {
	return (this.context == null) ? null : this.context.getObject();
    }

    public void setContext(Context context) {
        this.context = (context != null) ? new DomainReference<Context>(context) : null;
    }

    public CurriculumGroup getCurriculumGroup() {
	return (this.curriculumGroup == null) ? null : this.curriculumGroup.getObject();
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	this.curriculumGroup = (curriculumGroup != null) ? new DomainReference<CurriculumGroup>(curriculumGroup) : null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DegreeModuleToEnrol) {
            DegreeModuleToEnrol degreeModuleToEnrol = (DegreeModuleToEnrol) obj;
            return (this.getContext().equals(degreeModuleToEnrol.getContext()) && (this.getCurriculumGroup().equals(degreeModuleToEnrol.getCurriculumGroup())));
        }
        return false;
    }
    
    public String getKey() {
	return this.getContext().getClass().getName() + ":" + this.getContext().getIdInternal() + "," + this.getCurriculumGroup().getClass().getName() + ":" + this.getCurriculumGroup().getIdInternal(); 
    }
}
