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

    public String toString() {
        String result = "[TURMA_TURNO";
        result += ", turma=" + getTurma();
        result += ", turno=" + getTurno();
        result += ", chaveTurma=" + getChaveTurma();
        result += ", chaveTurno=" + getChaveTurno();
        result += "]";
        return result;
    }

}
