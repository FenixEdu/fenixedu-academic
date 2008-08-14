package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class UpdateNonAffiliatedTeachersProfessorship extends Service {

    public void run(List<Integer> nonAffiliatedTeachersIds, Integer executionCourseId) throws FenixServiceException {

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
	}

	List<NonAffiliatedTeacher> nonAffiliatedTeachersToRemove = new ArrayList<NonAffiliatedTeacher>();
	for (NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachers()) {
	    if (!nonAffiliatedTeachersIds.contains(nonAffiliatedTeacher.getIdInternal())) {
		nonAffiliatedTeachersToRemove.add(nonAffiliatedTeacher);
	    }
	}

	for (NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachersToRemove) {
	    executionCourse.removeNonAffiliatedTeachers(nonAffiliatedTeacher);
	}
    }

}
