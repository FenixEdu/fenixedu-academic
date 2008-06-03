package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.EnrolmentsPeriodCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.ExamsPeriodInNormalSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.ExamsPeriodInSpecialSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.GradeSubmissionInNormalSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.GradeSubmissionInSpecialSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.LessonsPeriodCE;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicCalendarEntryTypes implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	List<Class<? extends AcademicCalendarEntry>> result = new ArrayList<Class<? extends AcademicCalendarEntry>>();	
	CalendarEntryBean bean = (CalendarEntryBean) source;		
	
	if(bean.getEntry() != null && bean.getEntry().isRoot()) {
	    
	    result.add(AcademicYearCE.class);
	    result.add(AcademicSemesterCE.class);
	    result.add(AcademicTrimesterCE.class);
	
	} else if(bean.getEntry() != null) {
	    
	    result.add(AcademicSemesterCE.class);
	    result.add(AcademicTrimesterCE.class);
	    result.add(LessonsPeriodCE.class);
	    result.add(EnrolmentsPeriodCE.class);	   
	    result.add(ExamsPeriodInNormalSeasonCE.class);
	    result.add(ExamsPeriodInSpecialSeasonCE.class); 
	    result.add(GradeSubmissionInNormalSeasonCE.class);
	    result.add(GradeSubmissionInSpecialSeasonCE.class);
	}
	
	return result;
    }

    public Converter getConverter() {	
	return null;
    }   
}
