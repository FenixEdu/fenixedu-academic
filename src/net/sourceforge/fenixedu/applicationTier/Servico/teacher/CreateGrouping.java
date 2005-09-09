/*
 * Created on 28/Jul/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class CreateGrouping implements IService {

    public boolean run(Integer executionCourseID, InfoGrouping infoGrouping)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();

        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Grouping.create(infoGrouping.getName(), infoGrouping.getEnrolmentBeginDay().getTime(),
                infoGrouping.getEnrolmentEndDay().getTime(), infoGrouping.getEnrolmentPolicy(),
                infoGrouping.getGroupMaximumNumber(), infoGrouping.getIdealCapacity(), infoGrouping
                        .getMaximumCapacity(), infoGrouping.getMinimumCapacity(), infoGrouping
                        .getProjectDescription(), infoGrouping.getShiftType(), executionCourse);
        return true;
    }
}
