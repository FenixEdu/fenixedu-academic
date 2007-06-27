package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

public class LessonSpaceOccupation extends LessonSpaceOccupation_Base {
        
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public LessonSpaceOccupation(AllocatableSpace allocatableSpace, HourMinuteSecond startTime, HourMinuteSecond endTime,
	    DiaSemana dayOfWeek, OccupationPeriod occupationPeriod, Lesson lesson) {	       
	
	super();
	
	if(lesson.getLessonSpaceOccupation() != null) {
	    throw new DomainException("error.lesson.already.has.lessonSpaceOccupation");
	}
	
	if(occupationPeriod != null && allocatableSpace != null && dayOfWeek != null && startTime != null && endTime != null) {
	    if(!allocatableSpace.isFree(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(),
		    startTime, endTime, dayOfWeek, lesson.getFrequency(), lesson.getWeekOfQuinzenalStart(),
		    Boolean.TRUE, Boolean.TRUE, this)) {
		
		throw new DomainException("error.LessonSpaceOccupation.room.is.not.free");
	    }
	}
		
        setResource(allocatableSpace);
        setOccupationInterval(startTime, endTime);
        setDayOfWeek(dayOfWeek);        
        setPeriod(occupationPeriod);
        setLesson(lesson);
    }   
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void edit(AllocatableSpace allocatableSpace, HourMinuteSecond startTime, HourMinuteSecond endTime, DiaSemana dayOfWeek) {
	
	if(allocatableSpace != null && dayOfWeek != null && startTime != null && endTime != null) {
	    if(!allocatableSpace.isFree(getPeriod().getStartYearMonthDay(), getPeriod().getEndYearMonthDay(),
		    startTime, endTime, dayOfWeek, getLesson().getFrequency(), getLesson().getWeekOfQuinzenalStart(),
		    Boolean.TRUE, Boolean.TRUE, this)) {
		
		throw new DomainException("error.LessonSpaceOccupation.room.is.not.free");
	    }
	}
	
	setResource(allocatableSpace);
	setOccupationInterval(startTime, endTime);
	setDayOfWeek(dayOfWeek);        	        
    }
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void delete() {
	super.setLesson(null);
	super.delete();
    }
        
    public void setOccupationInterval(final HourMinuteSecond begin, final HourMinuteSecond end) {
	if(begin == null || end == null || !begin.isBefore(end)) {
	    throw new DomainException("error.EventSpaceOccupation.invalid.time");
	}	
	super.setStartTimeDateHourMinuteSecond(begin);
	super.setEndTimeDateHourMinuteSecond(end);
    }
    
    @Override
    public boolean isLessonSpaceOccupation() {
	return true;
    }
               
    @Override
    public void setLesson(Lesson lesson) {
	if(lesson == null) {
	    throw new DomainException("error.LessonSpaceOccupation.empty.lesson");
	}
	super.setLesson(lesson);
    }

    @Override
    public Integer getFrequency() {
	 return getLesson().getFrequency();
    }

    @Override
    public void setFrequency(Integer frequency) {
	getLesson().setFrequency(frequency); 
    }

    @Override
    public void setFrequency(FrequencyType type) {
	// Do Nothing
    }

    @Override
    public Integer getWeekOfQuinzenalStart() {
	return getLesson().getWeekOfQuinzenalStart();
    }

    @Override
    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
	getLesson().setWeekOfQuinzenalStart(weekOfQuinzenalStart); 
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getLessonOccupationsAccessGroupWithChainOfResponsibility();
    }    
}
