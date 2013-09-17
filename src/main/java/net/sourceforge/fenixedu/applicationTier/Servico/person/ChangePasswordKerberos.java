package net.sourceforge.fenixedu.applicationTier.Servico.person;


import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ChangePasswordKerberos {

    @Atomic
    public static void run(IUserView userView, String oldPassword, String newPassword) throws Exception {
        check(RolePredicates.PERSON_PREDICATE);
        Person person = userView.getPerson();

        try {
            if (person.hasIstUsername()) {
                Script.verifyPass(person.getIstUsername(), oldPassword);
                person.changePassword(oldPassword, newPassword);
                Script.changeKerberosPass(person.getIstUsername(), newPassword);
            } else {
                person.changePassword(oldPassword, newPassword);
            }
        } catch (DomainException de) {
            throw new InvalidPasswordServiceException(de.getKey());
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("error.person.impossible.change");
        } catch (KerberosException ke) {
            if (ke.getExitCode() == 1) {
                String returnCode = ke.getReturnCode();
                if (returnCode.equals(KerberosException.CHANGE_PASSWORD_TOO_SHORT)
                        || returnCode.equals(KerberosException.CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES)
                        || returnCode.equals(KerberosException.CHANGE_PASSWORD_CANNOT_REUSE)
                        || returnCode.equals(KerberosException.ADD_NOT_ENOUGH_CHARACTER_CLASSES)
                        || returnCode.equals(KerberosException.ADD_TOO_SHORT)
                        || returnCode.equals(KerberosException.CHECK_PASSWORD_LOW_QUALITY)) {
                    throw new InvalidPasswordServiceException(returnCode);
                } else if (returnCode.equals(KerberosException.WRONG_PASSWORD)) {
                    throw new InvalidPasswordServiceException("error.person.invalidExistingPassword");
                } else {
                    throw new InvalidPasswordServiceException("error.person.impossible.change");
                }
            } else {
                throw new FenixServiceException("error.person.impossible.change");
            }
        }
    }
}