/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePersonPassword implements IService {

	public void run(Integer personID, String password) throws ExcepcaoInexistente,
			FenixServiceException, InvalidPasswordServiceException, ExcepcaoPersistencia {

		// Check if the old password is equal

		ISuportePersistente sp = null;

		Person person = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPessoaPersistente personDAO = sp.getIPessoaPersistente();

		person = (Person) personDAO.readByOID(Person.class, personID, true);

		if (person == null)
			throw new ExcepcaoInexistente("Unknown Person!");

		person.setPassword(PasswordEncryptor.encryptPassword(password));
	}
}