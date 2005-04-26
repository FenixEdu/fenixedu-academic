/*
 * TurnoAluno.java
 *
 * Created on 21 de Outubro de 2002, 18:50
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public class ShiftStudent extends ShiftStudent_Base {
    
    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ShiftStudent() {
    }

    public ShiftStudent(IShift shift, IStudent student) {
        setShift(shift);
        setStudent(student);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IShiftStudent) {
            IShiftStudent shiftStudent = (IShiftStudent) obj;
            result = getShift().equals(shiftStudent.getShift())
                    && getStudent().equals(shiftStudent.getStudent());
        }
        return result;
    }

    public String toString() {
        String result = "[SHIFT_STUDENT";
        result += ", shift=" + getShift();
        result += ", student=" + getStudent();
        result += ", keyShift=" + getKeyShift();
        result += ", keyStudent=" + getKeyStudent();
        result += "]";
        return result;
    }

}