package net.sourceforge.fenixedu.applicationTier.Servico;

import jvstm.TransactionalCommand;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixframework.pstm.Transaction;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;
import net.sourceforge.fenixedu.util.kerberos.Script;

public class AuthenticateKerberos extends Authenticate {

    public static abstract class WorkerThread extends Thread implements TransactionalCommand {

        private final Integer personID;

        public WorkerThread(final Person person) {
            personID = person.getIdInternal();
        }

        public void run() {
            Transaction.withTransaction(this);
        }

        public void doIt() {
            final Person person = (Person) rootDomainObject.readPartyByOID(personID);
            doIt(person);
        }

        protected abstract void doIt(final Person person);

        protected static void runThread(final WorkerThread workerThread) {
            workerThread.start();
            try {
                workerThread.join();
            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }
    }

    public static class SetPasswordThread extends WorkerThread {

        private final String password;

        public SetPasswordThread(final Person person, final String password) {
            super(person);
            this.password = password;
        }

        @Override
        public void doIt(final Person person) {
            person.setPassword(PasswordEncryptor.encryptPassword(password));
        }

        public static void setPassword(final Person person, final String password) {
            runThread(new SetPasswordThread(person, password));
        }
    }

    public static class SetIsPassKerberos extends WorkerThread {

        public SetIsPassKerberos(final Person person) {
            super(person);
        }

        @Override
        public void doIt(final Person person) {
            person.setIsPassInKerberos(true);
        }

        public static void setIsPassKerberos(final Person person) {
            runThread(new SetIsPassKerberos(person));
        }
    }


    public IUserView run(final String username, final String password, final String requestURL, final String remoteHost)
            throws ExcepcaoPersistencia, ExcepcaoAutenticacao, FenixServiceException {

        Person person = Person.readPersonByUsernameWithOpenedLogin(username);
        if (person == null) {
            throw new ExcepcaoAutenticacao("bad.authentication");
        }
        
        if (person.hasIstUsername()) {
            if (person.getIsPassInKerberos()) {
                try {
                    Script.verifyPass(person.getIstUsername(), password);
                    if (!PasswordEncryptor.areEquals(person.getPassword(), password)) {
                        SetPasswordThread.setPassword(person, password);
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
                        SetIsPassKerberos.setIsPassKerberos(person);
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
