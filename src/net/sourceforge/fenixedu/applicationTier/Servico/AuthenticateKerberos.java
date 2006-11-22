package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;

public class AuthenticateKerberos extends Authenticate {

    public IUserView run(final String username, final String password, final String requestURL, final String remoteHost)
            throws ExcepcaoPersistencia, ExcepcaoAutenticacao, FenixServiceException {

        Person person = Person.readPersonByUsername(username);

        if (person == null || !person.canLogin()) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }
        if (person.hasIstUsername()) {
            if (person.getIsPassInKerberos()) {
                try {
                    Script.verifyPass(person.getIstUsername(), password);
                    if (!PasswordEncryptor.areEquals(person.getPassword(), password)) {
                        person.setPassword(PasswordEncryptor.encryptPassword(password));
                    }
                    return getUserView(person, requestURL);
                } catch (KerberosException ke) {
                    if (ke.getReturnCode().equals(KerberosException.CHANGE_PASSWORD_EXPIRED)) {
                        throw new PasswordExpiredServiceException(ke.getMessage());
                    }
                    if (ke.getReturnCode().equals(KerberosException.WRONG_PASSWORD)) {
                        throw new ExcepcaoAutenticacao("bad.authentication");
                    } else {
                        return super.run(username, password, requestURL, remoteHost);
                    }
                } catch (ExcepcaoPersistencia ep) {
                    return super.run(username, password, requestURL, remoteHost);
                }
            } else {
                final IUserView userView = super.run(username, password, requestURL, remoteHost);
                if (userView != null) {
                    try {
                        Script.createUser(person.getIstUsername(), password);
                        person.setIsPassInKerberos(true);
                    } catch (ExcepcaoPersistencia e) {

                    } catch (KerberosException ke) {
                        String returnCode = ke.getReturnCode();
                        if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
                                || returnCode
                                        .equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
                                || returnCode.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
                                || returnCode.equals(KerberosException.ADD_NOT_ENOUGH_CHARACTER_CLASSES)
                                || returnCode.equals(KerberosException.ADD_TOO_SHORT)
                                || returnCode.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY)) {
                            throw new InvalidPasswordServiceException(ke.getReturnCode(), userView);
                        }
                    }
                }
                return userView;
            }
        } else {
            return super.run(username, password, requestURL, remoteHost);
        }
    }
}