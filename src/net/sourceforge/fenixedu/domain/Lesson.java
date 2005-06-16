package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.Calendar;

import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.date.TimePeriod;

public class Lesson extends Lesson_Base {

    /**
     * Construtor sem argumentos público requerido pela moldura de objectos OJB
     */
    public Lesson() {
    }

    public Lesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo, IRoom sala,
            IRoomOccupation roomOccupation, IShift shift) {
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setSala(sala);
        setRoomOccupation(roomOccupation);
        setShift(shift);
    }

    public Calendar getInicio() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getBegin());
        return result;
    }

    public void setInicio(Calendar inicio) {
        this.setBegin(inicio.getTime());
    }

    public Calendar getFim() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnd());
        return result;
    }

    public void setFim(Calendar fim) {
        this.setEnd(fim.getTime());
    }

    public String toString() {
        String result = "[AULA";
        result += ", codInt=" + getIdInternal();
        result += ", diaSemana=" + getDiaSemana();
        if (getInicio() != null)
            result += ", inicio=" + getInicio().get(Calendar.HOUR_OF_DAY) + ":"
                    + getInicio().get(Calendar.MINUTE);
        if (getFim() != null)
            result += ", fim=" + getFim().get(Calendar.HOUR_OF_DAY) + ":"
                    + getFim().get(Calendar.MINUTE);
        result += ", tipo=" + getTipo();
        result += ", chaveSala=" + getChaveSala();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ILesson) {
            ILesson aula = (ILesson) obj;
            resultado = getIdInternal().equals(aula.getIdInternal());
        }

        return resultado;
    }

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getInicio(), this.getFim());
        return timePeriod.hours().doubleValue();
    }

}
