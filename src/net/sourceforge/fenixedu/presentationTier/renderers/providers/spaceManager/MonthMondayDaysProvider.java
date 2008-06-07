package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.spaceManager.ViewEventSpaceOccupationsBean;
import net.sourceforge.fenixedu.domain.Lesson;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MonthMondayDaysProvider implements DataProvider {

    private static int MONDAY_IN_JODA_TIME = 1;
    
    public Object provide(Object source, Object currentValue) {
	
	List<YearMonthDay> result = new ArrayList<YearMonthDay>();	
	ViewEventSpaceOccupationsBean bean = (ViewEventSpaceOccupationsBean) source;
	Partial year = bean.getYear();
	Partial month = bean.getMonth();

	if(month != null && year != null) {	    
	    	  
	    int monthNumber = month.get(DateTimeFieldType.monthOfYear());
	    int yearNumber = year.get(DateTimeFieldType.year()); 
	    
	    YearMonthDay firstDayOfMonth = new YearMonthDay(yearNumber, monthNumber, 1);	   
	    YearMonthDay monday = firstDayOfMonth.toDateTimeAtMidnight().withDayOfWeek(MONDAY_IN_JODA_TIME).toYearMonthDay();
	        	    
	    if(monday.getMonthOfYear() < monthNumber) {
		monday = monday.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);
	    }
	    
	    while(monday.getMonthOfYear() == monthNumber) {				
		result.add(monday);
		monday = monday.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);
	    }	    
	}
	
	return result;		
    }

    public Converter getConverter() {
	return null;
    }
}
