/*
 * Created on Apr 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

/**
 * @author Luis Cruz
 *  
 */
public class Group extends Group_Base {

    public String toString() {
        String result = "[Group";
        result += ", idInternal=" + getIdInternal();
        result += ", executionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IGroup) {
            final IGroup group = (IGroup) obj;
            return this.getIdInternal().equals(group.getIdInternal());
        }
        return false;
    }

}
