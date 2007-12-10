package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;

public class TSDProfessorship extends TSDProfessorship_Base {
    
    private  TSDProfessorship() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public TSDProfessorship(TSDCourse tsdCourse, TSDTeacher tsdTeacher, ShiftType type) {
		this();
		
		this.setTSDCourse(tsdCourse);
		this.setTSDTeacher(tsdTeacher);
		this.setType(type);
	}
        
    public Double getHours() {
    	if(getHoursType() == TSDValueType.MANUAL_VALUE) {
    		return super.getHoursManual();
    	} else if(getHoursType() == TSDValueType.LAST_YEAR_REAL_VALUE) {
    		return getTSDTeacher().getRealHoursByShiftTypeAndExecutionCourses(getType(), getTSDCourse().getAssociatedExecutionCoursesLastYear());
        } else if(getHoursType() == TSDValueType.REAL_VALUE) {
        	return getTSDTeacher().getRealHoursByShiftTypeAndExecutionCourses(getType(), getTSDCourse().getAssociatedExecutionCourses());
        }
    	
    	return 0d;
    }
    	
	public ExecutionPeriod getExecutionPeriod() {
		return getTSDCourse().getExecutionPeriod();
	}
	
	public Boolean getIsActive() {
		return getTSDCourse().getIsActive();
	}
	
	
	public void delete(){
		removeTSDCourse();
		removeTSDTeacher();
		removeRootDomainObject();
		super.deleteDomainObject();
	}
}
