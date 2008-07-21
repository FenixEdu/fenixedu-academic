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

    private DomainReference<Teacher> teacher;

    public AffiliatedTeacherDTO(Teacher teacher) {
	this.teacher = new DomainReference<Teacher>(teacher);
    }

    @Override
    public Teacher getTeacher() {
	return teacher.getObject();
    }

    @Override
    public String getName() {
	return getTeacher().getPerson().getName();
    }

    @Override
    public boolean isPhotoAvailable() {
	final Person person = getTeacher().getPerson();
	final Boolean availablePhoto = person.getAvailablePhoto();
	return availablePhoto != null && availablePhoto && person.hasPersonalPhoto();
    }

    @Override
    public Integer getPersonID() {
	return getTeacher().getPerson().getIdInternal();
    }
}
