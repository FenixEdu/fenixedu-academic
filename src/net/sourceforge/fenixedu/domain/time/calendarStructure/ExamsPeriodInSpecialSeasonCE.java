package net.sourceforge.fenixedu.domain.time.calendarStructure;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExamsPeriodInSpecialSeasonCE extends ExamsPeriodInSpecialSeasonCE_Base {

	public ExamsPeriodInSpecialSeasonCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title,
			MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

		super();
		super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
	}

	private ExamsPeriodInSpecialSeasonCE(AcademicCalendarEntry parentEntry, ExamsPeriodCE examsPeriodCE) {
		super();
		super.initVirtualEntry(parentEntry, examsPeriodCE);
	}

	@Override
	protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
		return new ExamsPeriodInSpecialSeasonCE(parentEntry, this);
	}
}
