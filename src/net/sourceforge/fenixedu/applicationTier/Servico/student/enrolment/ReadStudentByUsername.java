/*
 * Created on Sep 26, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentByUsername extends FenixService {

    public Registration run(String studentUsername) throws FenixServiceException {
	final Registration registration = Registration.readByUsername(studentUsername);
	if (registration == null) {
	    throw new FenixServiceException("error.noStudent");
	}
	return registration;
    }

}
