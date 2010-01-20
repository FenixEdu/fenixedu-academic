/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class AffiliatedTeacherDTO extends TeacherDTO {

    private Person person;

    public AffiliatedTeacherDTO(Person person) {
	this.person = person;
    }

    @Override
    public Teacher getTeacher() {
	return person.getTeacher();
    }

    @Override
    public String getName() {
	return person.getName();
    }

    @Override
    public boolean isPhotoAvailable() {
	final Person pPerson = person;
	final Boolean availablePhoto = pPerson.getAvailablePhoto();
	return availablePhoto != null && availablePhoto && pPerson.hasPersonalPhoto();
    }

    @Override
    public Integer getPersonID() {
	return person.getIdInternal();
    }
}
