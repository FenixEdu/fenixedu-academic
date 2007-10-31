package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class ViewEventSpaceOccupationsBean implements Serializable {

    private Partial year;

    private Partial month;

    private YearMonthDay day;

    private DomainReference<AllocatableSpace> allocatableSpaceReference; 

    public static int MONDAY_IN_JODA_TIME = 1;
    public static int SATURDAY_IN_JODA_TIME = 6;

    public ViewEventSpaceOccupationsBean(YearMonthDay day, AllocatableSpace allocatableSpace) {

	setAllocatableSpace(allocatableSpace);

	if(day != null) {	    
	    setYear(new Partial(DateTimeFieldType.year(), day.getYear()));
	    setMonth(new Partial(DateTimeFieldType.monthOfYear(), day.getMonthOfYear()));	    	    
	    
	    YearMonthDay monday = day.toDateTimeAtMidnight().withDayOfWeek(MONDAY_IN_JODA_TIME).toYearMonthDay();	    
	    if(monday.getMonthOfYear() < day.getMonthOfYear()) {
		monday = monday.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);		
	    } 	         	 	  
	    
	    setDay(monday);
	}
    }

    public AllocatableSpace getAllocatableSpace() {
	return allocatableSpaceReference != null ? allocatableSpaceReference.getObject() : null;
    }

    public void setAllocatableSpace(AllocatableSpace space) {
	this.allocatableSpaceReference = space != null ? new DomainReference<AllocatableSpace>(space) : null;
    }

    public Partial getYear() {
	return year;
    }

    public void setYear(Partial year) {
	this.year = year;
    }

    public Partial getMonth() {
	return month;
    }

    public void setMonth(Partial month) {
	this.month = month;
    }

    public YearMonthDay getDay() {
	return day;
    }

    public void setDay(YearMonthDay firstDayOWeek) {
	this.day = firstDayOWeek;
    }
}
