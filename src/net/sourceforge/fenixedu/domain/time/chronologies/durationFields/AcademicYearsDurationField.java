package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicYearsDurationField extends DurationField {

    private final Chronology chronology;

    public AcademicYearsDurationField(Chronology chronology_) {
	super();
	this.chronology = chronology_;
    }
    
    @Override
    public long add(long instant, int value) {
	
	throw unsupported();
    }

    @Override
    public long add(long instant, long value) {
	
	throw unsupported();
    }

    @Override
    public int compareTo(Object durationField) {
	
	throw unsupported();
    }

    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
	
	throw unsupported();
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
	
	throw unsupported();
    }

    @Override
    public long getMillis(int value) {
	
	throw unsupported();
    }

    @Override
    public long getMillis(long value) {
	
	throw unsupported();
    }

    @Override
    public long getMillis(int value, long instant) {
	
	throw unsupported();
    }

    @Override
    public long getMillis(long value, long instant) {
	
	throw unsupported();
    }

    @Override
    public String getName() {	
	return AcademicYearsDurationFieldType.ACADEMIC_YEARS_TYPE.getName();
    }

    @Override
    public DurationFieldType getType() {
	return AcademicYearsDurationFieldType.ACADEMIC_YEARS_TYPE;	
    }

    @Override
    public long getUnitMillis() {
	
	throw unsupported();
    }

    @Override
    public int getValue(long duration) {
	
	throw unsupported();
    }

    @Override
    public int getValue(long duration, long instant) {
	
	throw unsupported();
    }

    @Override
    public long getValueAsLong(long duration) {
	
	throw unsupported();
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
	
	throw unsupported();
    }

    @Override
    public boolean isPrecise() {
	return true;
    }

    @Override
    public boolean isSupported() {
	return true;
    }

    @Override
    public String toString() {
	
	throw unsupported();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(AcademicSemestersDurationFieldType.ACADEMIC_SEMESTERS_TYPE + " field is unsupported");
    }
}
