/*
 * SchoolClassShift.java
 *
 * Created on 19 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public class SchoolClassShift extends SchoolClassShift_Base {

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public SchoolClassShift() {
    }

    public SchoolClassShift(ISchoolClass schoolClass, IShift shift) {
        this.setTurma(schoolClass);
        this.setTurno(shift);
    }

    public String toString() {
        String result = "[TURMA_TURNO";
        result += ", turma=" + getTurma();
        result += ", turno=" + getTurno();
        result += ", chaveTurma=" + getChaveTurma();
        result += ", chaveTurno=" + getChaveTurno();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ISchoolClassShift) {
            final ISchoolClassShift schoolClassShift = (ISchoolClassShift) obj;
            return this.getIdInternal().equals(schoolClassShift.getIdInternal());
        }
        return false;
    }

}
