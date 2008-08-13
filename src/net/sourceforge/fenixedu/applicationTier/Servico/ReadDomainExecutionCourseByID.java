/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 19:48:00,26/Set/2005
 * @version $Id: ReadDomainExecutionCourseByID.java,v 1.2 2005/12/16 16:05:59
 *          lepc Exp $
 */
public class ReadDomainExecutionCourseByID extends Service {
    public ExecutionCourse run(Integer idInternal) throws FenixServiceException{

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(idInternal);
        if (executionCourse == null) {
            throw new NonExistingServiceException();
        }

        return executionCourse;
    }
}
