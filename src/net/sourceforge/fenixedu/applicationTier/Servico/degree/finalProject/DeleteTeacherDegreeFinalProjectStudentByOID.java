package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;

public class DeleteTeacherDegreeFinalProjectStudentByOID extends FenixService {

	public void run(Integer teacherDegreeFinalProjectStudentID) throws FenixServiceException {
		final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
				rootDomainObject.readTeacherDegreeFinalProjectStudentByOID(teacherDegreeFinalProjectStudentID);
		if (teacherDegreeFinalProjectStudent == null) {
			throw new FenixServiceException("message.noTeacherDegreeFinalProjectStudent");
		}
		teacherDegreeFinalProjectStudent.delete();
	}

}