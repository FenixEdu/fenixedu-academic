/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePersonPassword extends Service {

	public void run(Integer personID, String password) throws ExcepcaoInexistente,
			FenixServiceException, InvalidPasswordServiceException, ExcepcaoPersistencia {

		// Check if the old password is equal

		Person person = null;

		person = (Person) rootDomainObject.readPartyByOID(personID);

		if (person == null)
			throw new ExcepcaoInexistente("Unknown Person!");

		person.setPassword(PasswordEncryptor.encryptPassword(password));
	}
}