package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.publico.LoginRequestBean;
import pt.ist.fenixWebFramework.services.Service;

public class EnableExternalLoginWithKerberos extends FenixService {

	@Service
	public static void run(LoginRequestBean bean) throws FenixServiceException {
		Person person = bean.getPerson();
		if (person.getUser().getLoginRequest() != null) {
			// try {
			person.setGender(bean.getGender());
			person.setDefaultPhoneNumber(bean.getPhone());
			person.setPassword(PasswordEncryptor.encryptPassword(bean.getPassword()));
			// Script.createUser(person.getIstUsername(),
			// bean.getPassword());
			// person.setIsPassInKerberos(true);
			person.getUser().getLoginRequest().delete();
			// } catch (ExcepcaoPersistencia e) {
			// throw new
			// FenixServiceException("error.person.impossible.change");
			// } catch (KerberosException ke) {
			// if (ke.getExitCode() == 1) {
			// String returnCode = ke.getReturnCode();
			// if
			// (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
			// || returnCode.equals(KerberosException.
			// CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
			// ||
			// returnCode.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
			// || returnCode.equals(KerberosException.
			// ADD_NOT_ENOUGH_CHARACTER_CLASSES)
			// || returnCode.equals(KerberosException.ADD_TOO_SHORT)
			// ||
			// returnCode.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY))
			// {
			// throw new InvalidPasswordServiceException(returnCode);
			// } else if (returnCode.equals(KerberosException.WRONG_PASSWORD)) {
			// throw new InvalidPasswordServiceException(
			// "error.person.invalidExistingPassword");
			// } else {
			// throw new
			// InvalidPasswordServiceException("error.person.impossible.change"
			// );
			// }
			// }
			// }
		} else {
			throw new FenixServiceException("error.request.already.used");
		}
	}
}