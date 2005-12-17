package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class GenerateNewPasswordService implements IService {

	public String run(Integer personID) throws Exception {
		ISuportePersistente sp;
		IPerson person;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPessoaPersistente personDAO = sp.getIPessoaPersistente();

		person = (IPerson) personDAO.readByOID(Person.class, personID, true);

		if (person == null) {
			throw new ExcepcaoInexistente("Unknown Person!");
		}

		String password = GeneratePassword.getInstance().generatePassword(person);

		person.setIsPassInKerberos(true);
		person.setPassword(PasswordEncryptor.encryptPassword(password));

		return password;
	}

}
