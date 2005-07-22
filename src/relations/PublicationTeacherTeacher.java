package relations;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PublicationTeacherTeacher extends PublicationTeacherTeacher_Base {

	public static void add(net.sourceforge.fenixedu.domain.publication.IPublicationTeacher teacherPublications, int index1, net.sourceforge.fenixedu.domain.ITeacher teacher) throws DomainException {
		if (teacher.canAddPublicationToTeacherInformationSheet(teacherPublications.getPublicationArea())) {
			PublicationTeacherTeacher_Base.add(teacherPublications, index1, teacher);
		} else {
			throw new DomainException("error.teacherSheetFull", teacherPublications.getPublicationArea().getName());
		}
	}
}
