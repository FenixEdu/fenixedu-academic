/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Nov 9, 2005
 */
package org.fenixedu.academic.util;

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

    /**
     * Get the day of week property
     * (Monday will return 1, Tuesday 2, ...)
     * 
     * @return index of day of week
     */
    public int getDayOfWeek() {
        return ordinal() + 1;
    }

    public static WeekDay getWeekDay(final DiaSemana weekDay) {
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

    public static WeekDay fromJodaTimeToWeekDay(final DateTime date) {
        final int dayOfWeek = date.dayOfWeek().get();
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

    public String getLabel() {
        return org.fenixedu.bennu.core.i18n.BundleUtil.getString(Bundle.ENUMERATION, name());
    }

    public String getLabelShort() {
        return org.fenixedu.bennu.core.i18n.BundleUtil.getString(Bundle.ENUMERATION, name() + ".short");
    }

}
