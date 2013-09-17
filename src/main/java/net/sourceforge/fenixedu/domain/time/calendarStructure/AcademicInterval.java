package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.base.AbstractInterval;

import pt.ist.fenixframework.FenixFramework;

public class AcademicInterval extends AbstractInterval implements Serializable {

    private static final String RESUMED_SEPARATOR = "_";

    private static final String FULL_SEPARATOR = ":";

    private static final String NAME_SEPARATOR = " - ";

    public static final Comparator<AcademicInterval> COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicInterval>() {

        @Override
        public int compare(AcademicInterval o1, AcademicInterval o2) {
            final int c = o1.getBeginYearMonthDayWithoutChronology().compareTo(o2.getBeginYearMonthDayWithoutChronology());
            return c == 0 ? o2.getEndDateTimeWithoutChronology().compareTo(o1.getEndDateTimeWithoutChronology()) : c;
        }

    };

    public static final Comparator<AcademicInterval> REVERSE_COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicInterval>() {

        @Override
        public int compare(AcademicInterval o1, AcademicInterval o2) {
            final int c = o2.getBeginYearMonthDayWithoutChronology().compareTo(o1.getBeginYearMonthDayWithoutChronology());
            return c == 0 ? o1.getEndDateTimeWithoutChronology().compareTo(o2.getEndDateTimeWithoutChronology()) : c;
        }

    };

    private final String academicCalendarExternalId;
    private final String entryExternalId;

    private transient AcademicCalendarEntry academicCalendarEntry;
    private transient AcademicCalendarRootEntry academicCalendarRootEntry;
    private transient AcademicChronology academicChronology;

    private AcademicInterval(String entryExternalId, String academicCalendarExternalId) {
        if (entryExternalId == null) {
            throw new DomainException("error.AcademicInterval.empty.entry.externalId");
        }
        this.entryExternalId = entryExternalId;
        if (academicCalendarExternalId == null) {
            throw new DomainException("error.AcademicInterval.empty.academic.chronology.externalId");
        }
        this.academicCalendarExternalId = academicCalendarExternalId;
    }

    public AcademicInterval(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntry) {
        String entryExternalId = entry.getExternalId();
        if (entryExternalId == null) {
            throw new DomainException("error.AcademicInterval.empty.entry.externalId");
        }
        this.entryExternalId = entryExternalId;
        String academicCalendarExternalId = rootEntry.getExternalId();
        if (academicCalendarExternalId == null) {
            throw new DomainException("error.AcademicInterval.empty.academic.chronology.externalId");
        }
        this.academicCalendarExternalId = academicCalendarExternalId;
        academicCalendarEntry = entry;
        academicCalendarRootEntry = rootEntry;
    }

    public AcademicChronology getAcademicChronology() {
        return (AcademicChronology) getChronology();
    }

    @Override
    public Chronology getChronology() {
        if (academicChronology == null) {
            academicChronology = getAcademicCalendar().getAcademicChronology();
        }
        return academicChronology;
    }

    @Override
    public long getStartMillis() {
        return getAcademicCalendarEntry().getBegin().getMillis();
    }

    @Override
    public long getEndMillis() {
        return getAcademicCalendarEntry().getEnd().getMillis();
    }

    public String getPresentationName() {
        return getAcademicCalendarEntry().getPresentationName();
    }

    public String getPathName() {
        String result = "";

        AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntry();
        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
            result += academicCalendarEntry.getTitle() + NAME_SEPARATOR;
            academicCalendarEntry = academicCalendarEntry.getParentEntry();
        }

        if (result.endsWith(NAME_SEPARATOR)) {
            result = result.substring(0, result.length() - 3);
        }

        return result;
    }

    public AcademicCalendarEntry getAcademicCalendarEntryInIntervalChronology() {
        return getAcademicChronology().findSameEntry(getAcademicCalendarEntry());
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {
        if (academicCalendarRootEntry == null) {
            academicCalendarRootEntry = FenixFramework.getDomainObject(getAcademicCalendarExternalId());
        }
        return academicCalendarRootEntry;
    }

    public AcademicCalendarEntry getAcademicCalendarEntry() {
        if (academicCalendarEntry == null) {
            academicCalendarEntry = FenixFramework.getDomainObject(getEntryExternalId());
        }
        return academicCalendarEntry;
    }

    private AcademicCalendarEntry getAcademicCalendarEntryIntervalWithoutClassNameCheck() {
        if (academicCalendarEntry == null) {
            academicCalendarEntry = FenixFramework.getDomainObject(getEntryExternalId());
        }
        return academicCalendarEntry;
    }

    public YearMonthDay getBeginYearMonthDayWithoutChronology() {
        return getAcademicCalendarEntry().getBegin().toYearMonthDay();
    }

    public YearMonthDay getEndYearMonthDayWithoutChronology() {
        return getAcademicCalendarEntry().getEnd().toYearMonthDay();
    }

    public DateTime getStartDateTimeWithoutChronology() {
        return getAcademicCalendarEntry().getBegin();
    }

    public DateTime getEndDateTimeWithoutChronology() {
        return getAcademicCalendarEntry().getEnd();
    }

    public boolean isEqualOrEquivalent(AcademicInterval interval) {
        return getAcademicCalendarEntry().isEqualOrEquivalent(interval.getAcademicCalendarEntry());
    }

    public String getEntryExternalId() {
        return entryExternalId;
    }

    public String getAcademicCalendarExternalId() {
        return academicCalendarExternalId;
    }

    public String getRepresentationInStringFormat() {
        return getEntryExternalId() + FULL_SEPARATOR + getAcademicCalendarExternalId();
    }

    public static AcademicInterval getAcademicIntervalFromString(String representationInStringFormat) {
        String[] split = representationInStringFormat.split(FULL_SEPARATOR);
        String entryExternalId = split[0];
        String academicCalendarExternalId = split[1];
        return new AcademicInterval(entryExternalId, academicCalendarExternalId);
    }

    public String getResumedRepresentationInStringFormat() {
        return getEntryExternalId() + RESUMED_SEPARATOR + getAcademicCalendarExternalId();
    }

    public static AcademicInterval getAcademicIntervalFromResumedString(String representationInStringFormat) {
        String[] split = representationInStringFormat.split(RESUMED_SEPARATOR);
        String entryExternalId = split[0];
        String academicCalendarExternalId = split[1];
        return new AcademicInterval(entryExternalId, academicCalendarExternalId);
    }

    // Operations for get periods.

    public TeacherCreditsFillingForTeacherCE getTeacherCreditsFillingForTeacher() {
        return getAcademicCalendarEntry().getTeacherCreditsFillingForTeacher(getAcademicChronology());
    }

    public TeacherCreditsFillingForDepartmentAdmOfficeCE getTeacherCreditsFillingForDepartmentAdmOffice() {
        return getAcademicCalendarEntry().getTeacherCreditsFillingForDepartmentAdmOffice(getAcademicChronology());
    }

    public int getAcademicSemesterOfAcademicYear() {
        return getAcademicCalendarEntry().getAcademicSemesterOfAcademicYear(getAcademicChronology());
    }

    public AcademicSemesterCE plusSemester(int amount) {
        int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
        return getAcademicChronology().getAcademicSemesterIn(index + amount);
    }

    public AcademicSemesterCE minusSemester(int amount) {
        int index = getStart().get(AcademicSemesterDateTimeFieldType.academicSemester());
        return getAcademicChronology().getAcademicSemesterIn(index - amount);
    }

    public AcademicYearCE plusYear(int amount) {
        int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
        return getAcademicChronology().getAcademicYearIn(index + amount);
    }

    public AcademicYearCE minusYear(int amount) {
        int index = getStart().get(AcademicYearDateTimeFieldType.academicYear());
        return getAcademicChronology().getAcademicYearIn(index - amount);
    }

    // ///////

    public AcademicInterval getChildAcademicInterval(AcademicPeriod period, int cardinal) {
        AcademicCalendarEntry entry = getAcademicCalendarEntry().getChildAcademicCalendarEntry(period, cardinal);
        return new AcademicInterval(entry, entry.getRootEntry());
    }

    public static int getCardinalityOfAcademicInterval(AcademicInterval child) {
        return child.getAcademicCalendarEntry().getParentEntry().getCardinalityOfCalendarEntry(child.getAcademicCalendarEntry());
    }

    public static AcademicInterval getDefaultAcademicInterval(List<AcademicInterval> academicIntervals) {
        DateTime now = new DateTime();

        AcademicInterval closest = null;
        for (AcademicInterval academicInterval : academicIntervals) {
            if (closest == null
                    || Math.abs(academicInterval.getStart().getMillis() - now.getMillis()) > Math.abs(closest.getStart()
                            .getMillis() - now.getMillis())) {
                closest = academicInterval;
            }
        }
        return closest;
    }

    public static AcademicInterval readDefaultAcademicInterval(AcademicPeriod academicPeriod) {
        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            return ExecutionSemester.readActualExecutionSemester().getAcademicInterval();
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            return ExecutionYear.readCurrentExecutionYear().getAcademicInterval();
        }

        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    @Deprecated
    public static List<AcademicInterval> readAcademicIntervals(AcademicPeriod academicPeriod) {
        RootDomainObject rootDomainObject = RootDomainObject.getInstance();

        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionSemester semester : rootDomainObject.getExecutionPeriods()) {
                result.add(semester.getAcademicInterval());
            }

            return result;
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionYear executionYear : rootDomainObject.getExecutionYears()) {
                result.add(executionYear.getAcademicInterval());
            }

            return result;
        }
        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    @Deprecated
    public static List<AcademicInterval> readActiveAcademicIntervals(AcademicPeriod academicPeriod) {
        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionSemester semester : ExecutionSemester.readNotClosedExecutionPeriods()) {
                result.add(semester.getAcademicInterval());
            }

            return result;
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
                result.add(executionYear.getAcademicInterval());
            }

            return result;
        }
        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    public static List<AcademicInterval> readFutureAcademicIntervals(AcademicPeriod academicPeriod) {
        if (academicPeriod.equals(AcademicPeriod.SEMESTER)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionSemester semester : ExecutionSemester.readNotOpenExecutionPeriods()) {
                result.add(semester.getAcademicInterval());
            }

            return result;
        } else if (academicPeriod.equals(AcademicPeriod.YEAR)) {
            List<AcademicInterval> result = new ArrayList<AcademicInterval>();
            for (ExecutionYear executionYear : ExecutionYear.readNotOpenExecutionYears()) {
                result.add(executionYear.getAcademicInterval());
            }

            return result;
        }
        throw new UnsupportedOperationException("Unknown AcademicPeriod " + academicPeriod);
    }

    public AcademicInterval getNextAcademicInterval() {
        AcademicCalendarEntry nextAcademicCalendarEntry = getAcademicCalendarEntry().getNextAcademicCalendarEntry();
        if (nextAcademicCalendarEntry != null) {
            return new AcademicInterval(nextAcademicCalendarEntry, getAcademicCalendarEntry().getRootEntry());
        }

        return null;
    }

    public AcademicInterval getPreviousAcademicInterval() {
        AcademicCalendarEntry previousAcademicCalendarEntry = getAcademicCalendarEntry().getPreviousAcademicCalendarEntry();
        if (previousAcademicCalendarEntry != null) {
            return new AcademicInterval(previousAcademicCalendarEntry, getAcademicCalendarEntry().getRootEntry());
        }

        return null;
    }
}
