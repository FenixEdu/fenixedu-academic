package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeFieldType;
import net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeFieldType;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.base.AbstractInterval;

public class AcademicInterval extends AbstractInterval implements Serializable {

    public static final Comparator<AcademicInterval> COMPARATOR_BY_BEGIN_DATE = new Comparator<AcademicInterval>() {

	@Override
	public int compare(AcademicInterval o1, AcademicInterval o2) {
	    final int c = o1.getBeginYearMonthDayWithoutChronology().compareTo(o2.getBeginYearMonthDayWithoutChronology());
	    return c == 0 ? o2.getEndDateTimeWithoutChronology().compareTo(o1.getEndDateTimeWithoutChronology()) : c;
	}

    };

    private Integer academicCalendarIdInternal;
    private Integer entryIdInternal;
    private String entryClassName;

    private transient AcademicCalendarEntry academicCalendarEntry;
    private transient AcademicCalendarRootEntry academicCalendarRootEntry;
    private transient AcademicChronology academicChronology;

    public AcademicInterval(Integer entryIdInternal, String entryClassName, Integer academicCalendarIdInternal) {
	setEntryIdInternal(entryIdInternal);
	setEntryClassName(entryClassName);
	setAcademicCalendarIdInternal(academicCalendarIdInternal);
    }

    private AcademicInterval(Integer entryIdInternal, Integer academicCalendarIdInternal) {
	setEntryIdInternal(entryIdInternal);
	AcademicCalendarEntry entry = getAcademicCalendarEntryIntervalWithoutClassNameCheck();
	setEntryClassName(entry.getClass().getName());
	setAcademicCalendarIdInternal(academicCalendarIdInternal);
    }

    public AcademicInterval(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntry) {
	setEntryIdInternal(entry.getIdInternal());
	setEntryClassName(entry.getClass().getName());
	setAcademicCalendarIdInternal(rootEntry.getIdInternal());
	academicCalendarEntry = entry;
	academicCalendarRootEntry = rootEntry;
    }

    public AcademicChronology getAcademicChronology() {
	return (AcademicChronology) getChronology();
    }

    public Chronology getChronology() {
	if (academicChronology == null) {
	    academicChronology = getAcademicCalendar().getAcademicChronology();
	}
	return academicChronology;
    }

    public long getStartMillis() {
	return getAcademicCalendarEntry().getBegin().getMillis();
    }

    public long getEndMillis() {
	return getAcademicCalendarEntry().getEnd().getMillis();
    }

    public String getPresentationName() {
	return getAcademicCalendarEntry().getPresentationName();
    }

    public AcademicCalendarEntry getAcademicCalendarEntryInIntervalChronology() {
	return getAcademicChronology().findSameEntry(getAcademicCalendarEntry());
    }

    public AcademicCalendarRootEntry getAcademicCalendar() {
	if (academicCalendarRootEntry == null) {
	    academicCalendarRootEntry = (AcademicCalendarRootEntry) RootDomainObject.getInstance()
		    .readAcademicCalendarEntryByOID(getAcademicCalendarIdInternal());
	}
	return academicCalendarRootEntry;
    }

    public AcademicCalendarEntry getAcademicCalendarEntry() {
	if (academicCalendarEntry == null) {
	    academicCalendarEntry = RootDomainObject.getInstance().readAcademicCalendarEntryByOID(getEntryIdInternal());
	}
	if (!academicCalendarEntry.getClass().getName().equals(getEntryClassName())) {
	    throw new DomainException("error.AcademicInterval.invalid.class.names");
	}
	return academicCalendarEntry;
    }

    private AcademicCalendarEntry getAcademicCalendarEntryIntervalWithoutClassNameCheck() {
	if (academicCalendarEntry == null) {
	    academicCalendarEntry = RootDomainObject.getInstance().readAcademicCalendarEntryByOID(getEntryIdInternal());
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

    public Integer getEntryIdInternal() {
	return entryIdInternal;
    }

    public void setEntryIdInternal(Integer entryIdInternal) {
	if (entryIdInternal == null) {
	    throw new DomainException("error.AcademicInterval.empty.entry.idInternal");
	}
	this.entryIdInternal = entryIdInternal;
    }

    public String getEntryClassName() {
	return entryClassName;
    }

    public void setEntryClassName(String clazz) {
	if (clazz == null || StringUtils.isEmpty(clazz)) {
	    throw new DomainException("error.AcademicInterval.empty.entry.class");
	}
	this.entryClassName = clazz;
    }

    public Integer getAcademicCalendarIdInternal() {
	return academicCalendarIdInternal;
    }

    public void setAcademicCalendarIdInternal(Integer academicCalendarIdInternal) {
	if (academicCalendarIdInternal == null) {
	    throw new DomainException("error.AcademicInterval.empty.academic.chronology.idInternal");
	}
	this.academicCalendarIdInternal = academicCalendarIdInternal;
    }

    public String getRepresentationInStringFormat() {
	return getEntryClassName() + ":" + getEntryIdInternal() + ":" + getAcademicCalendarIdInternal();
    }

    public static AcademicInterval getAcademicIntervalFromString(String representationInStringFormat) {
	String[] split = representationInStringFormat.split(":");
	String entryClassName = split[0];
	Integer entryIdInternal = Integer.valueOf(split[1]);
	Integer academicCalendarIdInternal = Integer.valueOf(split[2]);
	return new AcademicInterval(entryIdInternal, entryClassName, academicCalendarIdInternal);
    }

    public String getResumedRepresentationInStringFormat() {
	return getEntryIdInternal() + ":" + getAcademicCalendarIdInternal();
    }

    public static AcademicInterval getAcademicIntervalFromResumedString(String representationInStringFormat) {
	String[] split = representationInStringFormat.split(":");
	Integer entryIdInternal = Integer.valueOf(split[0]);
	Integer academicCalendarIdInternal = Integer.valueOf(split[1]);
	return new AcademicInterval(entryIdInternal, academicCalendarIdInternal);
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
}
