/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class TeacherDTO implements Serializable {

    public abstract String getName();

    public abstract DomainObject getTeacher();

    public boolean isPhotoAvailable() {
	return false;
    }

    public Integer getPersonID() {
	return null;
    }

}
