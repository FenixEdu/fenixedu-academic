/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Ago/2003, 7:57:10
 * 
 */
public class ReadStudentById extends Service {

	public Object run(Integer id) throws FenixServiceException, ExcepcaoPersistencia {
		InfoStudent infoStudent = null;

		Registration registration = rootDomainObject.readRegistrationByOID(id);
		if (registration != null) {
			infoStudent = InfoStudent.newInfoFromDomain(registration);
		}

		return infoStudent;
	}
}