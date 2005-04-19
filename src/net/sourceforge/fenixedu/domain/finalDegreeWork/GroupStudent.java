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

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGroupStudent) {
            IGroupStudent groupStudent = (IGroupStudent) obj;

            result = getIdInternal() != null && groupStudent != null
                    && getIdInternal().equals(groupStudent.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[GroupStudent";
        result += ", idInternal=" + getIdInternal();
        result += "]";
        return result;
    }
}