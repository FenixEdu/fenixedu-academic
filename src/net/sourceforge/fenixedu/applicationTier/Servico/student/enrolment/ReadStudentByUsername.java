/*
 * Created on Sep 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentByUsername implements IService {
    
    public IStudent run(String studentUsername) throws ExcepcaoPersistencia, FenixServiceException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IStudent student = sp.getIPersistentStudent().readByUsername(studentUsername);
        if (student == null) {
            throw new FenixServiceException("error.noStudent");
        }
        return student;
    }

}
