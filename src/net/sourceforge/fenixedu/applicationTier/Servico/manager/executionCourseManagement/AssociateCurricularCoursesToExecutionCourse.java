package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse extends Service {

    public void run(Integer executionCourseId, List curricularCourseIds) throws FenixServiceException {
	if (executionCourseId == null) {
	    throw new FenixServiceException("nullExecutionCourseId");
	}

	if (curricularCourseIds != null) {
	    ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

	    if (executionCourse == null) {
		throw new NonExistingServiceException("noExecutionCourse");
	    }

	    Iterator iter = curricularCourseIds.iterator();
	    while (iter.hasNext()) {
		Integer curricularCourseId = (Integer) iter.next();

		CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
		if (curricularCourse == null) {
		    throw new NonExistingServiceException("noCurricularCourse");
		}
		if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
		    curricularCourse.addAssociatedExecutionCourses(executionCourse);
		}
	    }
	}
    }
}
