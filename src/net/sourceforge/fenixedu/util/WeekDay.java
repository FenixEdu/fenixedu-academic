/**
 * Nov 9, 2005
 */
package net.sourceforge.fenixedu.util;

import org.joda.time.DateTime;

/**
 * @author Ricardo Rodrigues
 * 
 */

public enum WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public String getName() {
	return name();
    }

    public static WeekDay getWeekDay(DiaSemana weekDay) {
	switch (weekDay.getDiaSemana()) {
	case DiaSemana.DOMINGO:
	    return WeekDay.SUNDAY;
	case DiaSemana.SEGUNDA_FEIRA:
	    return WeekDay.MONDAY;
	case DiaSemana.TERCA_FEIRA:
	    return WeekDay.TUESDAY;
	case DiaSemana.QUARTA_FEIRA:
	    return WeekDay.WEDNESDAY;
	case DiaSemana.QUINTA_FEIRA:
	    return WeekDay.THURSDAY;
	case DiaSemana.SEXTA_FEIRA:
	    return WeekDay.FRIDAY;
	case DiaSemana.SABADO:
	    return WeekDay.SATURDAY;
	default:
	    return null;
	}
    }

    // Return a WeekDay converted from JodaTime DateTime.
    public static WeekDay fromJodaTimeToWeekDay(DateTime date) {
	int dayOfWeek = date.dayOfWeek().get();
	switch (dayOfWeek) {
	case 1:
	    return WeekDay.MONDAY;
	case 2:
	    return WeekDay.TUESDAY;
	case 3:
	    return WeekDay.WEDNESDAY;
	case 4:
	    return WeekDay.THURSDAY;
	case 5:
	    return WeekDay.FRIDAY;
	case 6:
	    return WeekDay.SATURDAY;
	case 7:
	    return WeekDay.SUNDAY;
	default:
	    return null;
	}
    }

}
