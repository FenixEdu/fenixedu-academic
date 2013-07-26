package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;
import pt.ist.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class AuthenticateExpiredKerberos extends Authenticate {

    protected User run(final String username, final String password, final String newPassword, final String requestURL,
            String remoteHostName) throws ExcepcaoPersistencia, ExcepcaoAutenticacao, FenixServiceException {

        Person person = Person.readPersonByUsernameWithOpenedLogin(username);
        if (person == null) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }

        if (person.getIstUsername() != null) {

            try {
                Script.verifyPass(person.getIstUsername(), password);
                return person.getBennuUser();
            } catch (KerberosException e) {
                if (e.getReturnCode().equals(KerberosException.CHANGE_PASSWORD_EXPIRED)) {
                    try {
                        person.changePassword(password, newPassword);
                        Script.changeKerberosPass(person.getIstUsername(), newPassword);
                        return person.getBennuUser();
                    } catch (DomainException de) {
                        throw new InvalidPasswordServiceException(de.getKey());
                    } catch (KerberosException ke) {
                        if (ke.getExitCode() == 1) {
                            String returnCode = ke.getReturnCode();
                            if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
                                    || returnCode.equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
                                    || returnCode.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
                                    || returnCode.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY)) {
                                throw new InvalidPasswordServiceException(returnCode);
                            } else {
                                throw new InvalidPasswordServiceException("error.person.impossible.change");
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
            throw new FenixServiceException("error.empty.istUsername");
        }
    }

    // Service Invokers migrated from Berserk

    private static final AuthenticateExpiredKerberos serviceInstance = new AuthenticateExpiredKerberos();

    @Atomic
    public static User runAuthenticationExpired(String username, String password, String newPassword, String requestURL,
            String remoteHostName) throws ExcepcaoPersistencia, ExcepcaoAutenticacao, FenixServiceException {
        return serviceInstance.run(username, password, newPassword, requestURL, remoteHostName);
    }

}