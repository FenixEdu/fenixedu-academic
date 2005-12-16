/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 19:48:00,26/Set/2005
 * @version $Id$
 */
public class ReadDomainExecutionCourseByID implements IService
{
    public IExecutionCourse run(Integer idInternal) throws FenixServiceException {

        IExecutionCourse executionCourse = null;
        InfoExecutionCourseWithNonAffiliatedTeachers infoExecutionCourse = new InfoExecutionCourseWithNonAffiliatedTeachers();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOID(
                    ExecutionCourse.class, idInternal);

            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }
            
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return executionCourse;
    }
}


