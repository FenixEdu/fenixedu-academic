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
/*
 * DiaSemana.java
 *
 * Created on 11 de Outubro de 2002, 17:01
 */

package org.fenixedu.academic.util;

import java.io.Serializable;

import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author tfc130
 */
public class DiaSemana implements Serializable {

    public static final int DOMINGO = 1;

    public static final int SEGUNDA_FEIRA = 2;

    public static final int TERCA_FEIRA = 3;

    public static final int QUARTA_FEIRA = 4;

    public static final int QUINTA_FEIRA = 5;

    public static final int SEXTA_FEIRA = 6;

    public static final int SABADO = 7;

    private final Integer diaSemana;

    public DiaSemana(int diaSemana) {
        this.diaSemana = new Integer(diaSemana);
    }

    public DiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getDiaSemana() {
        return this.diaSemana;
    }

    public String getDiaSemanaString() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof DiaSemana) {
            DiaSemana diaSemana = (DiaSemana) obj;
            resultado = (this.getDiaSemana().intValue() == diaSemana.getDiaSemana().intValue());
        }
        return resultado;
    }

    @Override
    public String toString() {
        int diaSemana = this.diaSemana.intValue();
        switch (diaSemana) {
        case SEGUNDA_FEIRA:
            return new DateTime().withDayOfWeek(1).dayOfWeek().getAsShortText(I18N.getLocale());
        case TERCA_FEIRA:
            return new DateTime().withDayOfWeek(2).dayOfWeek().getAsShortText(I18N.getLocale());
        case QUARTA_FEIRA:
            return new DateTime().withDayOfWeek(3).dayOfWeek().getAsShortText(I18N.getLocale());
        case QUINTA_FEIRA:
            return new DateTime().withDayOfWeek(4).dayOfWeek().getAsShortText(I18N.getLocale());
        case SEXTA_FEIRA:
            return new DateTime().withDayOfWeek(5).dayOfWeek().getAsShortText(I18N.getLocale());
        case SABADO:
            return new DateTime().withDayOfWeek(6).dayOfWeek().getAsShortText(I18N.getLocale());
        case DOMINGO:
            return new DateTime().withDayOfWeek(7).dayOfWeek().getAsShortText(I18N.getLocale());
        }
        return "Error: Invalid week day";
    }

    public int getDiaSemanaInDayOfWeekJodaFormat() {
        return (getDiaSemana().intValue() == 1) ? 7 : getDiaSemana().intValue() - 1;
    }

    public static int getDiaSemana(YearMonthDay date) {
        DateTime dateTime = date.toDateTimeAtMidnight();
        return dateTime.getDayOfWeek() == 7 ? 1 : dateTime.getDayOfWeek() + 1;
    }

    public static DiaSemana fromJodaWeekDay(int jodaWeekDay) {
        final int i = jodaWeekDay == 7 ? 1 : jodaWeekDay + 1;
        return new DiaSemana(i);
    }

    public static DiaSemana fromWeekDay(final WeekDay weekDay) {
        switch (weekDay) {
        case SUNDAY:
            return new DiaSemana(DiaSemana.DOMINGO);
        case MONDAY:
            return new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
        case TUESDAY:
            return new DiaSemana(DiaSemana.TERCA_FEIRA);
        case WEDNESDAY:
            return new DiaSemana(DiaSemana.QUARTA_FEIRA);
        case THURSDAY:
            return new DiaSemana(DiaSemana.QUINTA_FEIRA);
        case FRIDAY:
            return new DiaSemana(DiaSemana.SEXTA_FEIRA);
        case SATURDAY:
            return new DiaSemana(DiaSemana.SABADO);
        default:
            return null;
        }
    }

}