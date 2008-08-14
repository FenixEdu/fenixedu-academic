package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class JavaPartial2SqlStringFieldConversion implements FieldConversion {

    private static DateTimeFieldType[] DATE_TIME_FIELDS = new DateTimeFieldType[] { DateTimeFieldType.era(),
	    DateTimeFieldType.yearOfEra(), DateTimeFieldType.centuryOfEra(), DateTimeFieldType.yearOfCentury(),
	    DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth(),
	    DateTimeFieldType.weekyearOfCentury(), DateTimeFieldType.weekyear(), DateTimeFieldType.weekOfWeekyear(),
	    DateTimeFieldType.dayOfWeek(), DateTimeFieldType.halfdayOfDay(), DateTimeFieldType.hourOfHalfday(),
	    DateTimeFieldType.clockhourOfHalfday(), DateTimeFieldType.clockhourOfDay(), DateTimeFieldType.hourOfDay(),
	    DateTimeFieldType.minuteOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfDay(),
	    DateTimeFieldType.secondOfMinute(), DateTimeFieldType.millisOfDay(), DateTimeFieldType.millisOfSecond() };

    /** Serilization version id */
    private static final long serialVersionUID = 1L;

    public Object javaToSql(Object object) {
	if (object != null && object instanceof Partial) {
	    Partial partial = (Partial) object;

	    StringBuilder sqlValue = new StringBuilder();

	    for (int i = 0; i < DATE_TIME_FIELDS.length; i++) {
		DateTimeFieldType field = DATE_TIME_FIELDS[i];

		if (partial.isSupported(field)) {
		    if (sqlValue.length() > 0) {
			sqlValue.append(",");
		    }

		    sqlValue.append(field.getName() + "=" + partial.get(field));
		}
	    }

	    return sqlValue.toString();
	} else {
	    return null;
	}
    }

    public Object sqlToJava(Object object) {
	if (object == null) {
	    return null;
	}

	if (object instanceof String) {
	    String sqlValue = (String) object;

	    String[] fieldValues = sqlValue.split(",");

	    DateTimeFieldType[] usedFields = new DateTimeFieldType[fieldValues.length];
	    int[] usedValues = new int[fieldValues.length];

	    for (int i = 0; i < fieldValues.length; i++) {
		String fieldValue = fieldValues[i];

		String[] fieldValueParts = fieldValue.split("=");
		if (fieldValueParts.length != 2) {
		    throw new ConversionException("invalid field format '" + fieldValue + "', should be '<name>=<value>'");
		}

		String name = fieldValueParts[0];
		DateTimeFieldType fieldType = getFieldByName(name);
		if (fieldType == null) {
		    throw new ConversionException("invalid partial field name '" + name + "'");
		}

		int value;
		try {
		    value = Integer.parseInt(fieldValueParts[1]);
		} catch (NumberFormatException e) {
		    throw new ConversionException("value for field '" + name + "' is not a number: " + fieldValueParts[1], e);
		}

		usedFields[i] = fieldType;
		usedValues[i] = value;
	    }

	    return new Partial(usedFields, usedValues);
	} else {
	    return object;
	}

    }

    private DateTimeFieldType getFieldByName(String name) {
	for (int i = 0; i < DATE_TIME_FIELDS.length; i++) {
	    DateTimeFieldType fieldType = DATE_TIME_FIELDS[i];

	    if (name.equals(fieldType.getName())) {
		return fieldType;
	    }
	}

	return null;
    }

}