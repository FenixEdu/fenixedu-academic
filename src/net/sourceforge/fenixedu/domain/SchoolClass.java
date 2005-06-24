/*
 * SchoolClass.java
 *
 * Created on 17 de Outubro de 2002, 19:07
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class SchoolClass extends SchoolClass_Base {

    public String toString() {
        String result = "[TURMA";
        result += ", codigoInterno=" + getIdInternal();
        result += ", nome=" + getNome();
        result += ", executionPeriod=" + getExecutionPeriod();
        result += ", executionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISchoolClass) {
            ISchoolClass turma = (ISchoolClass) obj;
            resultado = getIdInternal().equals(turma.getIdInternal());
        }
        return resultado;
    }

    public void associateShift(IShift shift) {
        if (shift == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedShifts().contains(shift)) {
            this.getAssociatedShifts().add(shift);
        }
        if (!shift.getAssociatedClasses().contains(this)) {
            shift.getAssociatedClasses().add(this);
        }
    }

}
