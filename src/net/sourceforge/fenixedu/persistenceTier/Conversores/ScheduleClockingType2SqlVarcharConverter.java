package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ScheduleClockingType2SqlVarcharConverter implements FieldConversion {

	public Object javaToSql(Object source) {
		if (source instanceof JustificationType) {
			ScheduleClockingType scheduleClockingType = (ScheduleClockingType) source;
			return scheduleClockingType.toString();
		}
		return source;
	}

	public Object sqlToJava(Object source) {
		if (source instanceof String) {
			return ScheduleClockingType.valueOf((String) source);
		}
		return source;
	}

}
