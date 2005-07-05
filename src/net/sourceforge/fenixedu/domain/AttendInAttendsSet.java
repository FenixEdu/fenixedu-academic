/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author joaosa & rmalo
 */

public class AttendInAttendsSet extends AttendInAttendsSet_Base {

    public AttendInAttendsSet() {
    }

    public AttendInAttendsSet(IAttends attend, IAttendsSet attendsSet) {
        super.setAttend(attend);
        super.setAttendsSet(attendsSet);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[AttendInAttendsSet";
        result += ", attend=" + getAttend();
        result += ", attendsSet=" + getAttendsSet();
        result += "]";
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof IAttendInAttendsSet) {
            final IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet) obj;
            return this.getIdInternal().equals(attendInAttendsSet.getIdInternal());
        }
        return false;
    }

}
