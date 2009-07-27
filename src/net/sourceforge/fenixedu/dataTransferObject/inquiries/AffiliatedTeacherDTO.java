/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class AffiliatedTeacherDTO extends TeacherDTO {

    private DomainReference<Person> person;

    public AffiliatedTeacherDTO(Person person) {
	this.person = new DomainReference<Person>(person);
    }

    @Override
    public Teacher getTeacher() {
	return person.getObject().getTeacher();
    }

    @Override
    public String getName() {
	return person.getObject().getName();
    }

    @Override
    public boolean isPhotoAvailable() {
	final Person pPerson = person.getObject();
	final Boolean availablePhoto = pPerson.getAvailablePhoto();
	return availablePhoto != null && availablePhoto && pPerson.hasPersonalPhoto();
    }

    @Override
    public Integer getPersonID() {
	return person.getObject().getIdInternal();
    }
}
