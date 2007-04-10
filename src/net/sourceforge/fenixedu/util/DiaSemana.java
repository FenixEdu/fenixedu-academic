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
        case DOMINGO:
            return "Domingo";
        case SEGUNDA_FEIRA:
            return "2ª feira";
        case TERCA_FEIRA:
            return "3ª feira";
        case QUARTA_FEIRA:
            return "4ª feira";
        case QUINTA_FEIRA:
            return "5ª feira";
        case SEXTA_FEIRA:
            return "6ª feira";
        case SABADO:
            return "Sábado";
        }
        return "Erro: Invalid week day";
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