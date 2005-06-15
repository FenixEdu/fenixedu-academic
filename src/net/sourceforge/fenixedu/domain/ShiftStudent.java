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
        boolean result = false;
        if (obj instanceof IShiftStudent) {
            IShiftStudent shiftStudent = (IShiftStudent) obj;
            result = getShift().equals(shiftStudent.getShift())
                    && getStudent().equals(shiftStudent.getStudent());
        }
        return result;
    }

}
