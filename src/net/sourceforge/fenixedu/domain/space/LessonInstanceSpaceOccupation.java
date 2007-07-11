package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.joda.time.YearMonthDay;

public class LessonInstanceSpaceOccupation extends LessonInstanceSpaceOccupation_Base {
    
    public LessonInstanceSpaceOccupation(LessonInstance lessonInstance, AllocatableSpace allocatableSpace) {
        
	super();
        
	setLessonInstance(lessonInstance);
	
	if(allocatableSpace != null && !allocatableSpace.isFree(this)) {
	    throw new DomainException("error.LessonInstanceSpaceOccupation.room.is.not.free");
	}
	
	setResource(allocatableSpace);		
    }
    
    public void delete() {
	super.setLessonInstance(null);
	super.delete();
    }
    
    @Override
    public void setLessonInstance(LessonInstance lessonInstance) {
	if(lessonInstance == null) {
	    throw new DomainException("error.LessonInstanceSpaceOccupation.empty.lessonInstance");
	}
	super.setLessonInstance(lessonInstance);
    }    
    
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasLessonInstance();	
    }
            
    public Lesson getLesson() {
	return getLessonInstance().getLesson();
    }

    @Override
    public boolean isLessonInstanceSpaceOccupation() {
        return true;
    }
                    
    @Override
    public YearMonthDay getBeginDate() {
	return getLessonInstance().getDay();
    }

    @Override
    public YearMonthDay getEndDate() {
	return getLessonInstance().getDay();
    }
    
    @Override
    public HourMinuteSecond getStartTimeDateHourMinuteSecond() {
	return getLessonInstance().getStartTime();
    }
   
    @Override
    public HourMinuteSecond getEndTimeDateHourMinuteSecond() {
	return getLessonInstance().getEndTime();
    }
    
    @Override
    public FrequencyType getFrequency() {
	return null;
    }
    
    @Override
    public DiaSemana getDayOfWeek() {
	return null;
    }   
    
    @Override
    public Boolean getDailyFrequencyMarkSaturday() {
	return null;
    }

    @Override
    public Boolean getDailyFrequencyMarkSunday() {
	return null;
    }
    
    @Override
    public Integer getWeekOfQuinzenalStart() {
	return null;
    }            
    
    @Override
    public Group getAccessGroup() {
	return null;
    }              
}
