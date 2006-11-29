package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;

public class AuthenticateExpiredKerberos extends Authenticate {

    public IUserView run(final String username, final String password, final String newPassword,
	    final String requestURL, String remoteHostName) throws ExcepcaoPersistencia,
	    ExcepcaoAutenticacao, FenixServiceException {

	Person person = Person.readPersonByUsernameWithOpenedLogin(username);
	if (person == null) {
	    throw new ExcepcaoAutenticacao("bad.authentication");
	}

	if (person.getIstUsername() != null) {
	    if (person.getIsPassInKerberos()) {
		try {
		    Script.verifyPass(person.getIstUsername(), password);
		} catch (KerberosException e) {
		    if (e.getReturnCode().equals(KerberosException.CHANGE_PASSWORD_EXPIRED)) {
			try {
			    person.changePassword(password, newPassword);
			    Script.changeKerberosPass(person.getIstUsername(), newPassword);
			    return getUserView(person, requestURL);
			} catch (DomainException de) {
			    throw new InvalidPasswordServiceException(de.getKey());
			} catch (KerberosException ke) {
			    if (ke.getExitCode() == 1) {
				String returnCode = ke.getReturnCode();
				if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
					|| returnCode
						.equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
					|| returnCode
						.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
					|| returnCode
						.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY)) {
				    throw new InvalidPasswordServiceException(returnCode);
				} else {
				    throw new InvalidPasswordServiceException(
					    "error.person.impossible.change");
				}
			    } else {
				throw new FenixServiceException(ke);
			    }
			}
		    }
		    if (e.getReturnCode().equals(KerberosException.WRONG_PASSWORD)) {
			throw new ExcepcaoAutenticacao("bad.authentication");
		    } else {
			throw new FenixServiceException("error.person.impossible.change");
		    }
		}
	    } else {
		final IUserView userView = super.run(username, password, requestURL, remoteHostName);

		if (userView == null) {
		    throw new InvalidPasswordServiceException("error.person.impossible.change");
		}

		try {
		    person.changePassword(password, newPassword);
		    Script.createUser(person.getIstUsername(), newPassword);
		    person.setIsPassInKerberos(true);

		} catch (DomainException de) {
		    throw new InvalidPasswordServiceException(de.getKey());
		} catch (ExcepcaoPersistencia ex) {
		    throw new InvalidPasswordServiceException("error.person.impossible.change");
		} catch (KerberosException ke) {
		    String returnCode = ke.getReturnCode();
		    if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
			    || returnCode
				    .equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
			    || returnCode.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
			    || returnCode.equals(KerberosException.ADD_NOT_ENOUGH_CHARACTER_CLASSES)
			    || returnCode.equals(KerberosException.ADD_TOO_SHORT)
			    || returnCode.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY)) {
			throw new InvalidPasswordServiceException(returnCode);
		    } else {
			throw new InvalidPasswordServiceException("error.person.impossible.change");
		    }
		}

		return userView;

	    }
	}
	return super.run(username, password, requestURL, remoteHostName);
    }
}
