package net.sourceforge.fenixedu.domain.time.dateTimeFields;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.field.PreciseDurationDateTimeField;

public class AcademicYearDateTimeField extends PreciseDurationDateTimeField {

    public AcademicYearDateTimeField(DateTimeFieldType type, DurationField unit) {
	super(type, unit);		
    }

    @Override
    public int get(long instant) {	
	return 0;
    }

    @Override
    public int getMaximumValue() {
	return 0;
    }

    @Override
    public DurationField getRangeDurationField() {	
	return null;
    }   
}
