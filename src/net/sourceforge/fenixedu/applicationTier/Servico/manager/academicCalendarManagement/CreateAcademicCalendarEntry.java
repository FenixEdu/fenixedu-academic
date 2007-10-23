package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.EnrolmentsPeriodCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.ExamsPeriodCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.GradeSubmissionCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.LessonsPeriodCE;

public class CreateAcademicCalendarEntry extends Service {

    public AcademicCalendarEntry run(CalendarEntryBean bean, boolean toCreate) {

	if(toCreate) {

	    Class<? extends AcademicCalendarEntry> type = bean.getType();

	    if(type.equals(AcademicCalendarRootEntry.class)) {		
		return new AcademicCalendarRootEntry(bean.getTitle(), bean.getDescription(), bean.getTemplateEntry()); 

	    } else if(type.equals(AcademicYearCE.class)) {
		return new AcademicYearCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry());		

	    } else if(type.equals(AcademicSemesterCE.class)) {
		return new AcademicSemesterCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry());
		
	    } else if(type.equals(AcademicTrimesterCE.class)) {
		return new AcademicTrimesterCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry());
		
	    } else if(type.equals(LessonsPeriodCE.class)) {
		return new LessonsPeriodCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry());
		
	    } else if(type.equals(ExamsPeriodCE.class)) {
		return new ExamsPeriodCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getSeasonType(), bean.getRootEntry());
		
	    } else if(type.equals(GradeSubmissionCE.class)) {
		return new GradeSubmissionCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getSeasonType(), bean.getRootEntry());
		
	    } else if(type.equals(EnrolmentsPeriodCE.class)) {
		return new EnrolmentsPeriodCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry());
	    }

	} else {
	    AcademicCalendarEntry entry = bean.getEntry();	    	  
	    entry.edit(bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(), bean.getRootEntry(), bean.getSeasonType(), bean.getTemplateEntry());	   	   
	    return entry;
	}

	return null;
    }
}
