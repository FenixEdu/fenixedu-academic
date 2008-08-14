/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jdnf, mrsp and Luis Cruz
 * 
 */
public class DeleteExecutionCourses extends Service {

    public List<String> run(final List<Integer> executionCourseIDs) throws FenixServiceException {
	final List<String> undeletedExecutionCoursesCodes = new ArrayList<String>();

	for (final Integer executionCourseID : executionCourseIDs) {
	    final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

	    if (!executionCourse.canBeDeleted()) {
		undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
	    } else {
		executionCourse.delete();
	    }
	}
	return undeletedExecutionCoursesCodes;
    }

}
