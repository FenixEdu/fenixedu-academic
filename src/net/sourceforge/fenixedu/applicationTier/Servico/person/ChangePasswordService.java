/*
 * Created on 13/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePasswordService implements IService {

    /**
     * The actor of this class.
     */
    public ChangePasswordService() {
    }

    public void run(IUserView userView, String oldPassword, String newPassword)
            throws ExcepcaoInexistente, FenixServiceException, InvalidPasswordServiceException,
            ExistingPersistentException, ExcepcaoPersistencia {

        // Check if the old password is equal

        ISuportePersistente sp = null;

        String username = new String(userView.getUtilizador());
        IPerson person = null;
        IPessoaPersistente personDAO = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            personDAO = sp.getIPessoaPersistente();
            person = personDAO.lerPessoaPorUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person!");
        }
        if (newPassword == null
                || newPassword.equals("")
                || person.getNumeroDocumentoIdentificacao().equalsIgnoreCase(newPassword)
                || (person.getCodigoFiscal() != null && person.getCodigoFiscal().equalsIgnoreCase(
                        newPassword))
                || (person.getNumContribuinte() != null && person.getNumContribuinte().equalsIgnoreCase(
                        newPassword)) || PasswordEncryptor.areEquals(person.getPassword(), newPassword)) {
            throw new InvalidPasswordServiceException("Invalid New Password!");
        }
        if (!PasswordEncryptor.areEquals(person.getPassword(), oldPassword)) {
            throw new InvalidPasswordServiceException("Invalid Existing Password!");
        }
        if (!PasswordEncryptor.areEquals(person.getPassword(), oldPassword)) {
            throw new InvalidPasswordServiceException("Invalid Existing Password!");
        }

        // Change the Password
        sp.getIPessoaPersistente().simpleLockWrite(person);
        person.setPassword(PasswordEncryptor.encryptPassword(newPassword));

    }
}