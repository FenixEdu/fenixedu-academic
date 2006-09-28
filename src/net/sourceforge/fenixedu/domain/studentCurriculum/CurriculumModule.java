package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.LanguageUtils;

public abstract class CurriculumModule extends CurriculumModule_Base {

    public CurriculumModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public void delete() {
	removeDegreeModule();
	removeCurriculumGroup();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public CurriculumGroup getRootCurriculumGroup() {
	return hasCurriculumGroup() ? getCurriculumGroup().getRootCurriculumGroup() : (CurriculumGroup) this;
    }

    public StudentCurricularPlan getRootStudentCurricularPlan() {
	return getRootCurriculumGroup().getRootStudentCurricularPlan();
    }

    public abstract boolean isLeaf();

    public abstract StringBuilder print(String tabs);
    
    public abstract List<Enrolment> getEnrolments();
    
    public abstract StudentCurricularPlan getStudentCurricularPlan();
    
        public String getName() {
	final DegreeModule degreeModule = getDegreeModule();
	return LanguageUtils.getUserLanguage() == Language.en ? degreeModule.getNameEn() : degreeModule.getName(); 
    }
    
    public abstract boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod);
    
    public abstract boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod);
    
    public boolean hasDegreModule(final DegreeModule degreeModule) {
	return this.getDegreeModule().equals(degreeModule);
    }
    
    public Set<CurricularRule> getCurricularRules(ExecutionPeriod executionPeriod){
	Set<CurricularRule> result = null;
	if(this.getCurriculumGroup() != null) {
	    result = this.getCurriculumGroup().getCurricularRules(executionPeriod);
	} else {
	    result = new HashSet<CurricularRule>();
	}
	result.addAll(this.getDegreeModule().getCurricularRules(executionPeriod));
	return result;
    }
}
