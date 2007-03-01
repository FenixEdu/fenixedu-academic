package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExpectationEvaluationGroup extends ExpectationEvaluationGroup_Base {
    
    private ExpectationEvaluationGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ExpectationEvaluationGroup(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {	
	this();	
	
	if(appraiser != null && evaluated != null) {	
	    if(appraiser.equals(evaluated)) {
		throw new DomainException("error.ExpectationEvaluationGroup.equals.teachers");
	    }
	    checkIfEvaluatedTeacherAlreadyExists(appraiser, evaluated, executionYear);                       
            checkTeachersDepartments(appraiser, evaluated, executionYear);
	}
		
	setAppraiser(appraiser);
	setEvaluated(evaluated);
	setExecutionYear(executionYear);
    }
    
    @Override
    public void setAppraiser(Teacher appraiser) {
	if(appraiser == null) {
	    throw new DomainException("error.ExpectationEvaluationGroup.empty.appraiser");
	}
	super.setAppraiser(appraiser);
    }

    @Override
    public void setEvaluated(Teacher evaluated) {
	if(evaluated == null) {
	    throw new DomainException("error.ExpectationEvaluationGroup.empty.evaluated");
	}
	super.setEvaluated(evaluated);
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	if(executionYear == null) {
	    throw new DomainException("error.ExpectationEvaluationGroup.empty.executionYear");
	}
	super.setExecutionYear(executionYear);
    }

    public void delete() {
	super.setAppraiser(null);
	super.setEvaluated(null);
	super.setExecutionYear(null);
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private void checkIfEvaluatedTeacherAlreadyExists(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
	List<ExpectationEvaluationGroup> groups = appraiser.getEvaluatedExpectationEvaluationGroups(executionYear);
	for (ExpectationEvaluationGroup group : groups) {
	    if(group.getEvaluated().equals(evaluated)) {
		 throw new DomainException("error.ExpectationEvaluationGroup.evaluatedTeacher.alreadyExists");	
	    }
	}
    }
    
    private void checkTeachersDepartments(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
	Department appraiserDepartment = appraiser.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());
	Department evaluatedDepartment = evaluated.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(), executionYear.getEndDateYearMonthDay());	           
	if(appraiserDepartment == null || evaluatedDepartment == null || !appraiserDepartment.equals(evaluatedDepartment)) {
	    throw new DomainException("error.ExpectationEvaluationGroup.invalid.departments");
	}
    }
}
