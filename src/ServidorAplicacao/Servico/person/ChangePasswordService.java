/*
 * Created on 13/Mar/2003
 */
package ServidorAplicacao.Servico.person;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

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

    public void run(UserView userView, String oldPassword, String newPassword)
            throws ExcepcaoInexistente, FenixServiceException,
            InvalidPasswordServiceException, ExistingPersistentException,
            ExcepcaoPersistencia {

        // Check if the old password is equal

        ISuportePersistente sp = null;

        String username = new String(userView.getUtilizador());
        IPessoa person = null;
        IPessoaPersistente personDAO = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            personDAO = sp.getIPessoaPersistente();
            person = personDAO.lerPessoaPorUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

      
        if (person == null) { throw new ExcepcaoInexistente("Unknown Person!"); }
        if (newPassword == null || newPassword.equals("")
                //|| person.getNumeroDocumentoIdentificacao() == null //is always not null
                || person.getNumeroDocumentoIdentificacao().equalsIgnoreCase(newPassword)
                || (person.getCodigoFiscal() != null //it can be null 
                && person.getCodigoFiscal().equals(newPassword))
                || (person.getNumContribuinte() != null  //it can be null
                && person.getNumContribuinte().equals(newPassword))
                || PasswordEncryptor.areEquals(person.getPassword(), newPassword)) { throw new InvalidPasswordServiceException(
                "Invalid New Password!"); }
        if (!PasswordEncryptor.areEquals(person.getPassword(), oldPassword))
        {
            throw new InvalidPasswordServiceException("Invalid Existing Password!");
        }
        if (!PasswordEncryptor.areEquals(person.getPassword(), oldPassword)) {
            throw new InvalidPasswordServiceException(
                    "Invalid Existing Password!");
        }

        // Change the Password
        sp.getIPessoaPersistente().simpleLockWrite(person);
        person.setPassword(PasswordEncryptor.encryptPassword(newPassword));

    }
}