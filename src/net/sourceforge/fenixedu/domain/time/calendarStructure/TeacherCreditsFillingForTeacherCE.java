package net.sourceforge.fenixedu.domain.time.calendarStructure;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TeacherCreditsFillingForTeacherCE extends TeacherCreditsFillingForTeacherCE_Base {

	public TeacherCreditsFillingForTeacherCE(AcademicCalendarEntry parentEntry, MultiLanguageString title,
			MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

		super();
		super.initEntry(parentEntry, title, description, begin, end, rootEntry);
	}
}
