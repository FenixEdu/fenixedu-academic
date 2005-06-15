/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    /**
     * Construtor
     */
    public StudentGroup() {
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer groupNumber, IAttendsSet attendsSet) {
        super.setGroupNumber(groupNumber);
        super.setAttendsSet(attendsSet);
    }

    /**
     * Construtor
     */
    public StudentGroup(Integer groupNumber, IAttendsSet attendsSet, IShift shift) {
        super.setGroupNumber(groupNumber);
        super.setAttendsSet(attendsSet);
        super.setShift(shift);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[STUDENT_GROUP";
        result += ", groupNumber=" + getGroupNumber();
        result += ", attendsSet=" + getAttendsSet();
        result += ", shift =" + getShift();
        result += "]";
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof IStudentGroup) {
            result = (getAttendsSet().equals(((IStudentGroup) arg0).getAttendsSet()))
                    && (getGroupNumber().equals(((IStudentGroup) arg0).getGroupNumber()));

        }
        return result;
    }

}
