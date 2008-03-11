package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TSDCurricularLoad extends TSDCurricularLoad_Base {
    
    public enum SchoolClassCalculationMethod {
	SHIFT_BASED,
	SHIFT_AND_FREQUENCY_BASED;
    }

    private  TSDCurricularLoad() {
        super();
    }
    
    protected  TSDCurricularLoad(TSDCourse course, ShiftType type, Double hoursManual, TSDValueType hoursType, 
    		Integer studentsPerShiftManual, TSDValueType studentsPerShiftType, 
    		Double weightFirstTimeEnrolledStudentsPerShiftManual, 
    		TSDValueType weightFirstTimeEnrolledStudentsPerShiftType,
    		Double weightSecondTimeEnrolledStudentsPerShiftManual,
    		TSDValueType weightSecondTimeEnrolledStudentsPerShiftType){
    	
    	this();
    	
    	if(course == null){
    		throw new DomainException("TSDCourse.required");
    	}
    	
        super.setTSDCourse(course);
        super.setType(type);
        super.setHoursManual(hoursManual);
        super.setHoursType(hoursType);
        super.setStudentsPerShiftManual(studentsPerShiftManual);
        super.setStudentsPerShiftType(studentsPerShiftType);
        super.setWeightFirstTimeEnrolledStudentsPerShiftManual(weightFirstTimeEnrolledStudentsPerShiftManual);
        super.setWeightFirstTimeEnrolledStudentsPerShiftType(weightFirstTimeEnrolledStudentsPerShiftType);
        super.setWeightSecondTimeEnrolledStudentsPerShiftManual(weightSecondTimeEnrolledStudentsPerShiftManual);
        super.setWeightSecondTimeEnrolledStudentsPerShiftType(weightSecondTimeEnrolledStudentsPerShiftType);
        super.setFrequency(1d);
    }
    
    public void delete(){
    	removeTSDCourse();
    	super.deleteDomainObject();
    }
    
    public Double getHoursPerShift(){
		Double hoursPerShift = super.getHoursPerShiftManual();
		
		if(hoursPerShift == null){
			TSDCourse course = getTSDCourse();
			List<CurricularCourse> courseList = course == null ? null : course.getAssociatedCurricularCourses();
			
			if(courseList != null && !courseList.isEmpty()){
				BigDecimal shiftHours = courseList.get(0).getTotalHoursByShiftType(getType(), course.getExecutionPeriod());
				
				return shiftHours == null ? 0d : shiftHours.doubleValue() / CompetenceCourseLoad.NUMBER_OF_WEEKS;
			}
		} 
		
		return hoursPerShift;
    }
	
    public Integer getTimeTableSlotsNumber(){
		Integer slots = super.getTimeTableSlots();
		ShiftType type = getType();
		TSDCourse course = getTSDCourse();
		Double frequency = getFrequency();
		
		double numberOfShifts = StrictMath.ceil(((new Double (course.getTotalNumberOfStudents(type)) / course.getStudentsPerShift(type))));
					
		Double slotsCalculated = Math.ceil(new Double(frequency * numberOfShifts));
		
		if(slots == null || slots.equals(0) || slots > slotsCalculated){
			return slotsCalculated.intValue();
		} else {
			return slots;
		}
    }
    
    public Integer getStudentsPerShift(){
    	return getTSDCourse().getStudentsPerShift(getType());
    }
    
    public Double getHours(){
    	return getTSDCourse().getHours(getType());
    }
    
    public Double getWeightFirstTimeEnrolledStudentsPerShift(){
    	return getTSDCourse().getWeightFirstTimeEnrolledStudentsPerShift(getType());
    }
    
    public Double getWeightSecondTimeEnrolledStudentsPerShift(){
    	return getTSDCourse().getWeightSecondTimeEnrolledStudentsPerShift(getType());
    }
    
    public Integer getNumberOfShifts() {
	return getTSDCourse().getNumberOfShifts(getType());
    }
    
    public double getNumberOfHoursForStudents() {
	return getTSDCourse().getNumberOfHoursForStudents(getType());
    }
    
    public double getNumberOfHoursForTeachers() {
	return getTSDCourse().getNumberOfHoursForTeachers(getType());
    }

    public Integer getNumberOfSchoolClasses() {
	return new Double(Math.ceil((getSchoolClassCalculationMethod().equals(SchoolClassCalculationMethod.SHIFT_BASED)) ? getNumberOfShifts() : getNumberOfShifts() * getFrequency())).intValue();
    }
}
