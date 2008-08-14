/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.ProposalState;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseWaitingAnswer extends Service {

    public boolean run(Integer executionCourseID) throws FenixServiceException {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
	if (executionCourse == null)
	    throw new InvalidArgumentsServiceException();

	List<Grouping> groupings = executionCourse.getGroupings();
	for (final Grouping grouping : groupings) {
	    final List<ExportGrouping> groupingExecutionCourses = grouping.getExportGroupings();
	    for (final ExportGrouping groupingExecutionCourse : groupingExecutionCourses) {
		if (groupingExecutionCourse.getProposalState().getState().intValue() == ProposalState.EM_ESPERA) {
		    return true;
		}
	    }
	}
	return false;
    }
}
