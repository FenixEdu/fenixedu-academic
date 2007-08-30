/*
 * DiaSemana.java
 *
 * Created on 11 de Outubro de 2002, 17:01
 */

package net.sourceforge.fenixedu.util;

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

    private Integer _diaSemana;

    public DiaSemana() {
    }

    public DiaSemana(int diaSemana) {
	this._diaSemana = new Integer(diaSemana);
    }

    public DiaSemana(Integer diaSemana) {
	this._diaSemana = diaSemana;
    }

    public Integer getDiaSemana() {
	return this._diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
	this._diaSemana = new Integer(diaSemana);
    }

    public void setDiaSemana(Integer diaSemana) {
	this._diaSemana = diaSemana;
    }

    public String getDiaSemanaString() {
	return toString();
    }

    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof DiaSemana) {
	    DiaSemana diaSemana = (DiaSemana) obj;
	    resultado = (this.getDiaSemana().intValue() == diaSemana.getDiaSemana().intValue());
	}
	return resultado;
    }

    public String toString() {
	int diaSemana = this._diaSemana.intValue();
	switch (diaSemana) {                   
	case SEGUNDA_FEIRA:
	    return new DateTime().withDayOfWeek(1).toString("E");
	case TERCA_FEIRA:
	    return new DateTime().withDayOfWeek(2).toString("E");
	case QUARTA_FEIRA:
	    return new DateTime().withDayOfWeek(3).toString("E");
	case QUINTA_FEIRA:
	    return new DateTime().withDayOfWeek(4).toString("E");
	case SEXTA_FEIRA:
	    return new DateTime().withDayOfWeek(5).toString("E");
	case SABADO:
	    return new DateTime().withDayOfWeek(6).toString("E");
	case DOMINGO:
	    return new DateTime().withDayOfWeek(7).toString("E");
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
}