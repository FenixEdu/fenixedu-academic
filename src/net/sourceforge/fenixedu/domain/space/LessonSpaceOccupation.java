package net.sourceforge.fenixedu.domain.space;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class LessonSpaceOccupation extends LessonSpaceOccupation_Base {
        
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public LessonSpaceOccupation(AllocatableSpace allocatableSpace, Lesson lesson) {	       
	
	super();
	
	if(lesson != null && lesson.getLessonSpaceOccupation() != null) {
	    throw new DomainException("error.lesson.already.has.lessonSpaceOccupation");
	}
	
	setLesson(lesson);        
	
	if(allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.LessonSpaceOccupation.room.is.not.free");
	}
	
        setResource(allocatableSpace);                                   	
    }   
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void edit(AllocatableSpace allocatableSpace) {	

	if(allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.LessonSpaceOccupation.room.is.not.free");
	}
	
	setResource(allocatableSpace);						       	  
    }
    
    @Checked("SpacePredicates.checkPermissionsToManageSpaceOccupationsWithoutCheckSpaceManagerRole")
    public void delete() {	
        super.setLesson(null);
	super.delete();
    }          
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasLesson();	
    }
    
    public OccupationPeriod getPeriod() {
	return getLesson().getPeriod();
    }
    
    @Override
    protected boolean intersects(YearMonthDay startDate, YearMonthDay endDate) {
        return getPeriod().nestedOccupationPeriodsIntersectDates(startDate, endDate);
    }
    
    @Override    
    public List<Interval> getEventSpaceOccupationIntervals() {

	List<Interval> result = new ArrayList<Interval>();
	OccupationPeriod occupationPeriod = getPeriod();        

	if(getPeriod() != null) {
	
	    result.addAll(getEventSpaceOccupationIntervals(occupationPeriod.getStartYearMonthDay(), occupationPeriod.getEndYearMonthDay(), 
		    getStartTimeDateHourMinuteSecond(), getEndTimeDateHourMinuteSecond(), getFrequency(), getWeekOfQuinzenalStart(), 
		    getDayOfWeek(), getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));

	    while(occupationPeriod.getNextPeriod() != null) {
		result.addAll(getEventSpaceOccupationIntervals(occupationPeriod.getNextPeriod().getStartYearMonthDay(), 
			occupationPeriod.getNextPeriod().getEndYearMonthDay(), getStartTimeDateHourMinuteSecond(),
			getEndTimeDateHourMinuteSecond(), getFrequency(), null, getDayOfWeek(),
			getDailyFrequencyMarkSaturday(), getDailyFrequencyMarkSunday()));

		occupationPeriod = occupationPeriod.getNextPeriod();             
	    }
	}
	
	return result;            			    
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
    public FrequencyType getFrequency() {
	 return getLesson().getFrequency();
    }
   
    @Override
    public Integer getWeekOfQuinzenalStart() {
	return getLesson().getWeekOfQuinzenalStart();
    }

    @Override
    public Group getAccessGroup() {
	return getSpace().getLessonOccupationsAccessGroupWithChainOfResponsibility();
    }
        
    @Override
    public YearMonthDay getBeginDate() {
	return getPeriod() != null ? getPeriod().getStartYearMonthDay() : null;
    }

    @Override
    public YearMonthDay getEndDate() {
	return getPeriod() != null ? getPeriod().getLastOccupationPeriodOfNestedPeriods().getEndYearMonthDay() : null;
    }
      
    @Override
    public DiaSemana getDayOfWeek() {
	return getLesson().getDiaSemana();
    }   

    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return getLesson().getBeginHourMinuteSecond();
    }
    
    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return getLesson().getEndHourMinuteSecond();
    }
    
    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return null;
    }
}
