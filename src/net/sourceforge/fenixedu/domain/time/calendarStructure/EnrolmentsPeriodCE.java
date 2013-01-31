package net.sourceforge.fenixedu.domain.time.calendarStructure;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EnrolmentsPeriodCE extends EnrolmentsPeriodCE_Base {

	public EnrolmentsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title,
			MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

		super();
		super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
	}

	private EnrolmentsPeriodCE(AcademicCalendarEntry parentEntry, EnrolmentsPeriodCE enrolmentsPeriodCE) {
		super();
		super.initVirtualEntry(parentEntry, enrolmentsPeriodCE);
	}

	@Override
	public boolean isOfType(AcademicPeriod period) {
		return false;
	}

	@Override
	public boolean isEnrolmentsPeriod() {
		return true;
	}

	@Override
	protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
		return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();
	}

	@Override
	protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
		return true;
	}

	@Override
	protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
		return false;
	}

	@Override
	protected boolean isPossibleToChangeTimeInterval() {
		return false;
	}

	@Override
	protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
		return new EnrolmentsPeriodCE(parentEntry, this);
	}

	@Override
	protected boolean associatedWithDomainEntities() {
		return false;
	}
}
