package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class StudentEnrolmentBean implements Serializable{

    private DomainReference<StudentCurricularPlan> studentCurricularPlan;
    private DomainReference<ExecutionPeriod> executionPeriod;
    private List<DomainReference<CurriculumModule>> curriculumModules;
    private List<DegreeModuleToEnrol> degreeModulesToEnrol;
    private CurriculumModuleBean curriculumModuleBean;
    
    public StudentCurricularPlan getStudentCurricularPlan() {
        return (this.studentCurricularPlan == null) ? null : this.studentCurricularPlan.getObject();
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = (studentCurricularPlan != null) ? new DomainReference<StudentCurricularPlan>(studentCurricularPlan) : null; 
    }

    public ExecutionPeriod getExecutionPeriod() {
    	return (this.executionPeriod == null) ? null : this.executionPeriod.getObject();
    }
    
    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
    	this.executionPeriod = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }

    public List<CurriculumModule> getCurriculumModules() {
	if (this.curriculumModules == null) {
	    return new ArrayList<CurriculumModule>();
	}
	
	List<CurriculumModule> result = new ArrayList<CurriculumModule>();
	for (DomainReference<CurriculumModule> curriculumModule : this.curriculumModules) {
	    result.add(curriculumModule.getObject());
	}
	
        return result;
    }

    public void setCurriculumModules(List<CurriculumModule> curriculumModules) {
	if (curriculumModules == null) {
	    this.curriculumModules = null;
	}
	else {
	    this.curriculumModules = new ArrayList<DomainReference<CurriculumModule>>();
	    for (CurriculumModule curriculumModule : curriculumModules) {
		this.curriculumModules.add(new DomainReference<CurriculumModule>(curriculumModule));
	    }
	}
    }

    public List<DegreeModuleToEnrol> getDegreeModulesToEnrol() {
        return degreeModulesToEnrol;
    }

    public void setDegreeModulesToEnrol(List<DegreeModuleToEnrol> degreeModulesToEnrol) {
        this.degreeModulesToEnrol = degreeModulesToEnrol;
    }

    public CurriculumModuleBean getCurriculumModuleBean() {
        return curriculumModuleBean;
    }

    public void setCurriculumModuleBean(CurriculumModuleBean curriculumModuleBean) {
        this.curriculumModuleBean = curriculumModuleBean;
    }
    
    public Set<CurriculumModule> getInitialCurriculumModules() {
	return getInitialCurriculumModules(getCurriculumModuleBean());
    }

    private Set<CurriculumModule> getInitialCurriculumModules(CurriculumModuleBean curriculumModuleBean) {
	Set<CurriculumModule> result = new HashSet<CurriculumModule>();
	if(curriculumModuleBean.getCurricularCoursesEnroled().isEmpty() && curriculumModuleBean.getGroupsEnroled().isEmpty()) {
	    result.add(curriculumModuleBean.getCurriculumModule());
	}
		
	for (CurriculumModuleBean moduleBean : curriculumModuleBean.getCurricularCoursesEnroled()) {
	    result.add(moduleBean.getCurriculumModule());
	}
	
	for (CurriculumModuleBean moduleBean : curriculumModuleBean.getGroupsEnroled()) {
	    result.addAll(getInitialCurriculumModules(moduleBean));
	}
	
	return result;
    }
}
