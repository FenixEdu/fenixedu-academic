/*
 * Shift.java
 *
 * Created on 17 de Outubro de 2002, 19:28
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.util.List;

public class Shift extends Shift_Base {
    protected Integer ocupation;

    protected Double percentage;

    public Integer getOcupation() {
        return ocupation;
    }

    public void setOcupation(Integer ocupation) {
        this.ocupation = ocupation;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String toString() {
        String result = "[TURNO";
        result += ", codigoInterno=" + this.getIdInternal();
        result += ", nome=" + getNome();
        result += ", tipo=" + getTipo();
        result += ", lotacao=" + getLotacao();
        result += ", chaveDisciplinaExecucao=" + getChaveDisciplinaExecucao();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IShift) {
            IShift turno = (IShift) obj;
            resultado = getIdInternal().equals(turno.getIdInternal());
        }
        return resultado;
    }

    public double hours() {
        double hours = 0;
        List lessons = this.getAssociatedLessons();
        for (int i = 0; i < lessons.size(); i++) {
            ILesson lesson = (ILesson) lessons.get(i);
            hours += lesson.hours();
        }
        return hours;
    }

}
