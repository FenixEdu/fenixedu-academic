package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DefineExamComment implements IService {

    public void run(InfoExecutionCourse infoExecutionCourse, String comment)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IExecutionCourse executionCourse = persistentSupport.getIPersistentExecutionCourse()
                .readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
                        infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        executionCourse.setComment(comment);
    }
}