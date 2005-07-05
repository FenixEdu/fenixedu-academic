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

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGroup) {
            IGroup group = (IGroup) obj;

            if (group.getIdInternal() != null && getIdInternal() != null) {
                result = group.getIdInternal().equals(getIdInternal());
            }
        }
        return result;
    }

    public String toString() {
        String result = "[Group";
        result += ", idInternal=" + getIdInternal();
        result += ", executionDegree=" + getExecutionDegree();
        result += "]";
        return result;
    }
    
}
