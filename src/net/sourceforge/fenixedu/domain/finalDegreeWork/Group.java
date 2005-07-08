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

}
