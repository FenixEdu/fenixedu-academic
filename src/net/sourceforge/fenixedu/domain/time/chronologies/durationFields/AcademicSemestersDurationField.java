package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.field.BaseDurationField;
import org.joda.time.field.FieldUtils;

public class AcademicSemestersDurationField extends BaseDurationField {

    private final AcademicChronology chronology;

    public AcademicSemestersDurationField(AcademicChronology chronology_) {
	super(AcademicSemestersDurationFieldType.academicSemesters());
	this.chronology = chronology_;
    }

    private UnsupportedOperationException unsupported() {
	return new UnsupportedOperationException(AcademicSemestersDurationFieldType.academicSemesters() + " field is unsupported");
    }

    @Override
    public long add(long instant, int value) {
	int academicSemester = chronology.getAcademicSemester(instant);
	if (academicSemester != 0) {
	    AcademicSemesterCE academicSemesterCE = chronology.getAcademicSemesterIn(academicSemester);
	    AcademicSemesterCE academicSemesterCEAfter = chronology.getAcademicSemesterIn(academicSemester + value);
	    if (academicSemesterCEAfter != null) {
		long result = academicSemesterCEAfter.getBegin().getMillis()
			+ new Duration(academicSemesterCE.getBegin().getMillis(), instant).getMillis();
		return result < academicSemesterCE.getEnd().getMillis() ? result : academicSemesterCE.getEnd().getMillis();
	    }
	}
	throw unsupported();
    }

    @Override
    public long add(long instant, long value) {
	return add(instant, FieldUtils.safeToInt(value));
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
	int minuendAcademicSemester = chronology.getAcademicSemester(minuendInstant);
	int subtrahendAcademicSemester = chronology.getAcademicSemester(subtrahendInstant);
	if (minuendAcademicSemester != 0 && subtrahendAcademicSemester != 0) {
	    return minuendAcademicSemester - subtrahendAcademicSemester;
	}
	throw unsupported();
    }

    @Override
    public long getMillis(int value, long instant) {
	int academicSemester = chronology.getAcademicSemester(instant);
	if (academicSemester != 0) {
	    AcademicSemesterCE academicSemesterCE = chronology.getAcademicSemesterIn(academicSemester);
	    AcademicSemesterCE academicSemesterCEAfter = chronology.getAcademicSemesterIn(academicSemester + value);
	    if (academicSemesterCEAfter != null) {
		long result = academicSemesterCEAfter.getBegin().getMillis()
			+ new Duration(academicSemesterCE.getBegin().getMillis(), instant).getMillis();
		return result < academicSemesterCE.getEnd().getMillis() ? result - instant : academicSemesterCE.getEnd()
			.getMillis()
			- instant;
	    }
	}
	throw unsupported();
    }

    @Override
    public long getMillis(long value, long instant) {
	return getMillis(FieldUtils.safeToInt(value), instant);
    }

    @Override
    public long getUnitMillis() {
	return DurationFieldType.years().getField(chronology).getUnitMillis() / 2;
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
	int valueBegin = chronology.getAcademicSemester(instant);
	int valueEnd = chronology.getAcademicSemester(instant + duration);
	if (valueBegin != 0 && valueEnd != 0) {
	    return valueEnd - valueBegin;
	}
	throw unsupported();
    }

    @Override
    public boolean isPrecise() {
	return false;
    }
}
