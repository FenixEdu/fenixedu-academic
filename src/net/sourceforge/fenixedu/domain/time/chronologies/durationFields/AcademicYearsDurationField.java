package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.field.BaseDurationField;
import org.joda.time.field.FieldUtils;

public class AcademicYearsDurationField extends BaseDurationField {

    private final AcademicChronology chronology;

    public AcademicYearsDurationField(AcademicChronology chronology_) {
	super(AcademicYearsDurationFieldType.academicYears());
	this.chronology = chronology_;
    }

    @Override
    public long add(long instant, int value) {
	int academicYear = chronology.getAcademicYear(instant);
	if (academicYear != 0) {
	    AcademicYearCE academicYearCE = chronology.getAcademicYearIn(academicYear);
	    AcademicYearCE academicYearCEAfter = chronology.getAcademicYearIn(academicYear + value);
	    if (academicYearCEAfter != null) {
		long result = academicYearCEAfter.getBegin().getMillis()
			+ new Duration(academicYearCE.getBegin().getMillis(), instant).getMillis();
		return result < academicYearCE.getEnd().getMillis() ? result : academicYearCE.getEnd().getMillis();
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
	int minuendAcademicYear = chronology.getAcademicYear(minuendInstant);
	int subtrahendAcademicYear = chronology.getAcademicYear(subtrahendInstant);
	if (minuendAcademicYear != 0 && subtrahendAcademicYear != 0) {
	    return minuendAcademicYear - subtrahendAcademicYear;
	}
	throw unsupported();
    }

    @Override
    public long getMillis(int value, long instant) {
	int academicYear = chronology.getAcademicYear(instant);
	if (academicYear != 0) {
	    AcademicYearCE academicYearCE = chronology.getAcademicYearIn(academicYear);
	    AcademicYearCE academicYearCEAfter = chronology.getAcademicYearIn(academicYear + value);
	    if (academicYearCEAfter != null) {
		long result = academicYearCEAfter.getBegin().getMillis()
			+ new Duration(academicYearCE.getBegin().getMillis(), instant).getMillis();
		return result < academicYearCE.getEnd().getMillis() ? result - instant : academicYearCE.getEnd().getMillis()
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
	return DurationFieldType.years().getField(chronology).getUnitMillis();
    }

    @Override
    public boolean isPrecise() {
	return false;
    }

    private UnsupportedOperationException unsupported() {
	return new UnsupportedOperationException(AcademicYearsDurationFieldType.academicYears() + " field is unsupported");
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
	int valueBegin = chronology.getAcademicYear(instant);
	int valueEnd = chronology.getAcademicYear(instant + duration);
	if (valueBegin != 0 && valueEnd != 0) {
	    return valueEnd - valueBegin;
	}
	throw unsupported();
    }
}
