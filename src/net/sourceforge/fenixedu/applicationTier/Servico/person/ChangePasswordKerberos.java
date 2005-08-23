package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePasswordKerberos implements IService {

	public void run(IUserView userView, String oldPassword, String newPassword)
			throws Exception {
		String returnCode = null;
		ISuportePersistente suportePersistente = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPerson person = suportePersistente.getIPessoaPersistente()
				.lerPessoaPorUsername(userView.getUtilizador());

		try {
			person.changePassword(oldPassword, newPassword);
		} catch (DomainException e) {
			throw new InvalidPasswordServiceException(e.getKey());
		}

		if (person.getIsPassInKerberos()) {
			try {
				if ((returnCode = UpdateKerberos.changeKerberosPass(person
						.getIstUsername(), newPassword)) != null) {
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
			} catch (ExcepcaoPersistencia e) {
				throw new FenixServiceException(e.getMessage());
			}
		}
	}
}
