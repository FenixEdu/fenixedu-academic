/*
 * Created on Sep 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByUsername extends Service {
    
    public Registration run(String studentUsername) throws ExcepcaoPersistencia, FenixServiceException {
        final Registration student = Registration.readByUsername(studentUsername);
        if (student == null) {
            throw new FenixServiceException("error.noStudent");
        }
        return student;
    }

}
