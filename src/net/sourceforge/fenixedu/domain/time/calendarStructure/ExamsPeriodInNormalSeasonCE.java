package net.sourceforge.fenixedu.domain.time.calendarStructure;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.DateTime;

public class ExamsPeriodInNormalSeasonCE extends ExamsPeriodInNormalSeasonCE_Base {

    public ExamsPeriodInNormalSeasonCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title,
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
    }

    private ExamsPeriodInNormalSeasonCE(AcademicCalendarEntry parentEntry, ExamsPeriodCE examsPeriodCE) {
	super();
	super.initVirtualEntry(parentEntry, examsPeriodCE);
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
	return new ExamsPeriodInNormalSeasonCE(parentEntry, this);
    }
}
