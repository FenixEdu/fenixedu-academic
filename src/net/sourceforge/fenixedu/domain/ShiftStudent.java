package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
public class ShiftStudent extends ShiftStudent_Base {

    public String toString() {
        String result = "[SHIFT_STUDENT";
        result += ", shift=" + getShift();
        result += ", student=" + getStudent();
        result += ", keyShift=" + getKeyShift();
        result += ", keyStudent=" + getKeyStudent();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IShiftStudent) {
            final IShiftStudent shiftStudent = (IShiftStudent) obj;
            return this.getIdInternal().equals(shiftStudent.getIdInternal());
        }
        return false;
    }

}
