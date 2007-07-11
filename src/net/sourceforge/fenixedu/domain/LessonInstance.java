package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class LessonInstance extends LessonInstance_Base {

    public static final Comparator<LessonInstance> COMPARATOR_BY_BEGIN_DATE_TIME = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE_TIME).addComparator(new BeanComparator("beginDateTime"));	
	((ComparatorChain) COMPARATOR_BY_BEGIN_DATE_TIME).addComparator(DomainObject.COMPARATOR_BY_ID);
    }
    
    public LessonInstance(Lesson lesson, AllocatableSpace space, Summary summary) {
	
	super();

	if(summary == null) {
	    throw new DomainException("error.LessonInstance.empty.summary");
	}
	
	YearMonthDay day = summary.getSummaryDateYearMonthDay();
	
	LessonInstance lessonInstance = lesson.getLessonInstanceFor(day);
	if(lessonInstance != null) {
	    throw new DomainException("error.lessonInstance.already.exist");
	}
			
	HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
	HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();	    
	DateTime beginDateTime = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(), beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
	DateTime endDateTime = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(), endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);	    	    	   
	
	setRootDomainObject(RootDomainObject.getInstance());
	setBeginDateTime(beginDateTime);
	setEndDateTime(endDateTime);
	setLesson(lesson);      
	setSummary(summary);
		
	lesson.refreshPeriodAndInstances(day.plusDays(1));
	
	if(space != null) {	    
	    new LessonInstanceSpaceOccupation(this, space);
	}	
    }
    
    public LessonInstance(Lesson lesson, AllocatableSpace space, YearMonthDay day) {
	
	super();

	LessonInstance lessonInstance = lesson.getLessonInstanceFor(day);
	if(lessonInstance != null) {
	    throw new DomainException("error.lessonInstance.already.exist");
	}
			
	HourMinuteSecond beginTime = lesson.getBeginHourMinuteSecond();
	HourMinuteSecond endTime = lesson.getEndHourMinuteSecond();	    
	DateTime beginDateTime = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), beginTime.getHour(), beginTime.getMinuteOfHour(), beginTime.getSecondOfMinute(), 0);
	DateTime endDateTime = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), endTime.getHour(), endTime.getMinuteOfHour(), endTime.getSecondOfMinute(), 0);	    	    	   
	
	setRootDomainObject(RootDomainObject.getInstance());
	setBeginDateTime(beginDateTime);
	setEndDateTime(endDateTime);
	setLesson(lesson);      
		
	if(space != null) {	    
	    new LessonInstanceSpaceOccupation(this, space);
	}
    }    

    public void delete() {	
	
	if(!canBeDeleted()) {
	   throw new DomainException("error.LessonInstance.cannot.be.deleted");
	}
	
	super.setLesson(null);	
	if(hasLessonInstanceSpaceOccupation()) {
	    getLessonInstanceSpaceOccupation().delete();
	}	
	
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private boolean canBeDeleted() {
	return !hasSummary();
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateTimeInterval() {
	final DateTime start = getBeginDateTime();
	final DateTime end = getEndDateTime();
	return start != null && end != null && start.isBefore(end);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return hasLesson();	
    }
    
    @Override
    public void setSummary(Summary summary) {
	if(summary == null) {
	    throw new DomainException("error.LessonInstance.empty.summary");
	}
	super.setSummary(summary);
    }
    
    @Override
    public void setLesson(Lesson lesson) {
	if (lesson == null) {
	    throw new DomainException("error.lessonInstance.empty.lesson");
	}
	super.setLesson(lesson);
    }   
    
    public YearMonthDay getDay() {
	return getBeginDateTime().toYearMonthDay();	
    }

    public HourMinuteSecond getStartTime() {
	return new HourMinuteSecond(getBeginDateTime().getHourOfDay(), getBeginDateTime().getMinuteOfHour(), getBeginDateTime().getSecondOfMinute());
    }
    
    public HourMinuteSecond getEndTime() {
	return new HourMinuteSecond(getEndDateTime().getHourOfDay(), getEndDateTime().getMinuteOfHour(), getEndDateTime().getSecondOfMinute());
    }
    
    public AllocatableSpace getRoom() {
	return hasLessonInstanceSpaceOccupation() ? getLessonInstanceSpaceOccupation().getRoom() : null;
    }
    
    public DiaSemana getDayOfweek() {
	return new DiaSemana(DiaSemana.getDiaSemana(getDay()));
    }      
}
