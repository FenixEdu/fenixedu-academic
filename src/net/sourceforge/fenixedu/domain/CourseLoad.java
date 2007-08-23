package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class CourseLoad extends CourseLoad_Base {
    
    public CourseLoad(ExecutionCourse executionCourse, ShiftType type, BigDecimal unitQuantity, BigDecimal totalQuantity) {
        super();
                
        if(executionCourse != null && type != null && executionCourse.getCourseLoadByShiftType(type) != null) {
            throw new DomainException("error.CourseLoad.executionCourse.already.contains.type");
        }
        
        setRootDomainObject(RootDomainObject.getInstance());        
        setUnitQuantity(unitQuantity);
        setTotalQuantity(totalQuantity);
        setExecutionCourse(executionCourse);
        setType(type);            
    }

    public void edit(BigDecimal unitQuantity, BigDecimal totalQuantity) {	
	setUnitQuantity(unitQuantity);
        setTotalQuantity(totalQuantity);
    }
    
    public void delete() {	
	if(!canbeDeleted()) {
	    throw new DomainException("error.CourseLoad.cannot.be.deleted");
	}
	removeExecutionCourse();
	getShifts().clear();
	getLessonInstances().clear();
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    public boolean isEmpty() {
	return getTotalQuantity().compareTo(BigDecimal.ZERO) != 1; 
    }
    
    private boolean canbeDeleted() {
	return !hasAnyLessonInstances() && !hasAnyShifts();
    }

    public BigDecimal getWeeklyHours() {
	return getTotalQuantity().divide(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS));
    }
    
    @Override
    public BigDecimal getUnitQuantity() {
        if(super.getUnitQuantity() == null) {
            return BigDecimal.ZERO;
        }
	return super.getUnitQuantity();
    }
    
    @Override
    public BigDecimal getTotalQuantity() {
	if(super.getTotalQuantity() == null) {
	    return BigDecimal.ZERO;
	}
	return super.getTotalQuantity();
    }
           
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasExecutionCourse() && !isEmpty() && getTotalQuantity().compareTo(getUnitQuantity()) != -1 && getType() != null;		 
    }  
}
