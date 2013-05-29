/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;

import pt.ist.fenixframework.DomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class TeacherDTO implements Serializable {

    public abstract String getName();

    public abstract DomainObject getTeacher();

    public String getPersonID() {
        return null;
    }

}
