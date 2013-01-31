package net.sourceforge.fenixedu.domain.time.calendarStructure;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AcademicTrimesterCE extends AcademicTrimesterCE_Base {

	public AcademicTrimesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description,
			DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

		super();
		super.initEntry(parentEntry, title, description, begin, end, rootEntry);
	}

	private AcademicTrimesterCE(AcademicCalendarEntry parentEntry, AcademicTrimesterCE academicTrimesterCE) {
		super();
		super.initVirtualEntry(parentEntry, academicTrimesterCE);
	}

	@Override
	public boolean isOfType(AcademicPeriod period) {
		return period.equals(AcademicPeriod.TRIMESTER);
	}

	@Override
	public boolean isAcademicTrimester() {
		return true;
	}

	@Override
	protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
		return !parentEntry.isAcademicYear() && !parentEntry.isAcademicSemester();
	}

	@Override
	protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
		return false;
	}

	@Override
	protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
		return true;
	}

	@Override
	protected boolean isPossibleToChangeTimeInterval() {
		return true;
	}

	@Override
	protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
		return new AcademicTrimesterCE(parentEntry, this);
	}

	@Override
	protected boolean associatedWithDomainEntities() {
		return false;
	}
}
