/*
 * Created on 2004/04/25
 *
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

/**
 * @author Luis Cruz
 *  
 */
public class GroupStudent extends GroupStudent_Base {

    public GroupStudent() {
        super();
    }

    public String toString() {
        String result = "[GroupStudent";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }

}