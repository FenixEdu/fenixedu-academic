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
        
        checkQuantities();
    }

    public void edit(BigDecimal unitQuantity, BigDecimal totalQuantity) {	
	setUnitQuantity(unitQuantity);
        setTotalQuantity(totalQuantity);
        
        checkQuantities();
    }
    
    public void delete() {	
	if(!canBeDeleted()) {
	    throw new DomainException("error.CourseLoad.cannot.be.deleted");
	}
	super.setExecutionCourse(null);
	getShifts().clear();
	getLessonInstances().clear();
	removeRootDomainObject();
	deleteDomainObject();
    }
           
    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if(executionCourse == null) {
            throw new DomainException("error.CourseLoad.empty.executionCourse");
        }
	super.setExecutionCourse(executionCourse);
    }
    
    @Override
    public void setType(ShiftType type) {
	if(type == null) {
	    throw new DomainException("error.CourseLoad.empty.type");
	}
	super.setType(type);
    }
    
    public boolean isEmpty() {
	return getTotalQuantity().compareTo(BigDecimal.ZERO) != 1; 
    }
    
    private boolean canBeDeleted() {
	return !hasAnyLessonInstances() && !hasAnyShifts();
    }

    public BigDecimal getWeeklyHours() {
	return getTotalQuantity().divide(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS), BigDecimal.ROUND_FLOOR);
    }
    
    @Override
    public void setTotalQuantity(BigDecimal totalQuantity) {
	if(totalQuantity == null) {
	    throw new DomainException("error.CourseLoad.empty.totalQuantity");
	}
	super.setTotalQuantity(totalQuantity);
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
	return !isEmpty() && isTotalQuantityValid() && getType() != null;		 
    }
    
    private void checkQuantities() {
	if(!isTotalQuantityValid()){
            throw new DomainException("error.CourseLoad.totalQuantity.less.than.unitQuantity");
        }
    }

    private boolean isTotalQuantityValid() {
	return getTotalQuantity().compareTo(getUnitQuantity()) != -1;
    }
}
