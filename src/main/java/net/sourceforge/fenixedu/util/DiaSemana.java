/*
 * DiaSemana.java
 *
 * Created on 11 de Outubro de 2002, 17:01
 */

package net.sourceforge.fenixedu.util;

import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author tfc130
 */
public class DiaSemana extends FenixUtil {

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
            return new DateTime().withDayOfWeek(1).dayOfWeek().getAsShortText(Language.getLocale());
        case TERCA_FEIRA:
            return new DateTime().withDayOfWeek(2).dayOfWeek().getAsShortText(Language.getLocale());
        case QUARTA_FEIRA:
            return new DateTime().withDayOfWeek(3).dayOfWeek().getAsShortText(Language.getLocale());
        case QUINTA_FEIRA:
            return new DateTime().withDayOfWeek(4).dayOfWeek().getAsShortText(Language.getLocale());
        case SEXTA_FEIRA:
            return new DateTime().withDayOfWeek(5).dayOfWeek().getAsShortText(Language.getLocale());
        case SABADO:
            return new DateTime().withDayOfWeek(6).dayOfWeek().getAsShortText(Language.getLocale());
        case DOMINGO:
            return new DateTime().withDayOfWeek(7).dayOfWeek().getAsShortText(Language.getLocale());
        }
        return "Error: Invalid week day";
    }

    public int getDiaSemanaInDayOfWeekJodaFormat() {
        return (getDiaSemana().intValue() == 1) ? 7 : getDiaSemana().intValue() - 1;
    }

    public static int getDiaSemana(DateTime dateTime) {
        return dateTime.getDayOfWeek() == 7 ? 1 : dateTime.getDayOfWeek() + 1;
    }

    public static int getDiaSemana(YearMonthDay date) {
        DateTime dateTime = date.toDateTimeAtMidnight();
        return dateTime.getDayOfWeek() == 7 ? 1 : dateTime.getDayOfWeek() + 1;
    }

    public static DiaSemana fromJodaWeekDay(int jodaWeekDay) {
        final int i = jodaWeekDay == 7 ? 1 : jodaWeekDay + 1;
        return new DiaSemana(i);
    }
}