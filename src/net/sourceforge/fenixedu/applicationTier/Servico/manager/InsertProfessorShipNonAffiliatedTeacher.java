package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class InsertProfessorShipNonAffiliatedTeacher extends FenixService {

	@Checked("RolePredicates.GEP_PREDICATE")
	@Service
	public static void run(Integer nonAffiliatedTeacherID, Integer executionCourseID) {

		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
		if (executionCourse == null) {
			throw new DomainException("message.nonExisting.executionCourse");
		}

		final NonAffiliatedTeacher nonAffiliatedTeacher = rootDomainObject.readNonAffiliatedTeacherByOID(nonAffiliatedTeacherID);
		if (nonAffiliatedTeacher == null) {
			throw new DomainException("message.non.existing.nonAffiliatedTeacher");
		}

		if (nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
			throw new DomainException("error.invalid.executionCourse");
		} else {
			nonAffiliatedTeacher.addExecutionCourses(executionCourse);
		}
	}

}
