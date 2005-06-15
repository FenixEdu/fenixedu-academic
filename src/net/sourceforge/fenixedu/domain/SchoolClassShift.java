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
        setTurma(schoolClass);
        setTurno(shift);
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
        boolean result = false;
        if (obj instanceof ISchoolClassShift) {
            ISchoolClassShift schoolClass_shift = (ISchoolClassShift) obj;
            result = getTurma().equals(schoolClass_shift.getTurma())
                    && getTurno().equals(schoolClass_shift.getTurno());
        }
        return result;
    }

}
