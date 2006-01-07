package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePasswordKerberos implements IService {

	public void run(IUserView userView, String oldPassword, String newPassword)
			throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		Person person = suportePersistente.getIPessoaPersistente()
				.lerPessoaPorUsername(userView.getUtilizador());

		try {
			person.changePassword(oldPassword, newPassword);
		} catch (DomainException e) {
			throw new InvalidPasswordServiceException(e.getKey());
		}

		try {
			if(person.getIstUsername() != null) {
				if (person.getIsPassInKerberos()) {
					UpdateKerberos.changeKerberosPass(person.getIstUsername(), newPassword);
				} else {
					UpdateKerberos.createUser(person.getIstUsername(), newPassword);
				}
			}
			person.setIsPassInKerberos(true);
		}catch(ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}catch(KerberosException ke) {
			if(ke.getExitCode() == 1) {
				String returnCode = ke.getReturnCode();
				if (returnCode.equals("CHANGE_PASSWORD_TOO_SHORT")
						|| returnCode
								.equals("CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES")
						|| returnCode
								.equals("CHANGE_PASSWORD_CANNOT_REUSE")) {
					throw new InvalidPasswordServiceException(returnCode);
				} else {
					throw new InvalidPasswordServiceException(
							"error.person.impossible.change");
				}
			}
			else {
				throw new FenixServiceException(ke);
			}
		}
	}
}
