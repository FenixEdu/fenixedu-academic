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
import net.sourceforge.fenixedu.util.kerberos.Script;
import net.sourceforge.fenixedu.applicationTier.IService;

public class ChangePasswordKerberos implements IService {

	public void run(IUserView userView, String oldPassword, String newPassword)	
		throws Exception {
		Person person = userView.getPerson();

		try {
			if(person.getIstUsername() != null) {
				if(person.getIsPassInKerberos()) {
	        		Script.verifyPass(person.getIstUsername(), oldPassword);
	        		person.changePassword(oldPassword, newPassword);
	        		Script.changeKerberosPass(person.getIstUsername(), newPassword);
				} else {
					person.changePassword(oldPassword, newPassword);
					Script.createUser(person.getIstUsername(), newPassword);
					person.setIsPassInKerberos(true);
				}
			} else {
				person.changePassword(oldPassword, newPassword);	
			}
		} catch(DomainException de) {
			throw new InvalidPasswordServiceException(de.getKey());
		} catch(ExcepcaoPersistencia e) {
			throw new FenixServiceException("error.person.impossible.change");
		} catch(KerberosException ke) {
			if(ke.getExitCode() == 1) {
				String returnCode = ke.getReturnCode();
				if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
						|| returnCode
								.equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
						|| returnCode
								.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)) {
					throw new InvalidPasswordServiceException(returnCode);
				} else {
					throw new InvalidPasswordServiceException(
							"error.person.impossible.change");
				}
			}
			else {
				throw new FenixServiceException("error.person.impossible.change");
			}
		}		
	}
}
