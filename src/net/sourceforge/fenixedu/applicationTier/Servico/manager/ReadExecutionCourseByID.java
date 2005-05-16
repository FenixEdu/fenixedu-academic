package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionCourseByID implements IService {

    public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException {

        IExecutionCourse executionCourse = null;
        InfoExecutionCourseWithNonAffiliatedTeachers infoExecutionCourse = new InfoExecutionCourseWithNonAffiliatedTeachers();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOID(
                    ExecutionCourse.class, idInternal);

            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }
            
            infoExecutionCourse.copyFromDomain(executionCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoExecutionCourse;
    }

}
