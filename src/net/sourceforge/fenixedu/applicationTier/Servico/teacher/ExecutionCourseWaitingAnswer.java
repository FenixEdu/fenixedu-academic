/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseWaitingAnswer implements IService {

    public boolean run(Integer executionCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        List<IGrouping> groupings = executionCourse.getGroupings();
        for (final IGrouping grouping : groupings) {
            final List<IExportGrouping> groupingExecutionCourses = grouping
                    .getExportGroupings();
            for (final IExportGrouping groupingExecutionCourse : groupingExecutionCourses) {
                if (groupingExecutionCourse.getProposalState().getState().intValue() == ProposalState.EM_ESPERA) {
                    return true;
                }
            }
        }
        return false;
    }
}
