/*
 * Created on 13/Mar/2003
 *
 */
package ServidorAplicacao.Servico.person;

import java.lang.reflect.InvocationTargetException;

import Dominio.IPessoa;
import ServidorAplicacao.IServico;
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
public class ChangePasswordService implements IServico {

	private static ChangePasswordService servico =
		new ChangePasswordService();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangePasswordService getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangePasswordService() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ChangePasswordService";
	}

	public void run(UserView userView, String oldPassword, String newPassword)
		throws
			ExcepcaoInexistente,
			FenixServiceException,
			InvalidPasswordServiceException, ExistingPersistentException, ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException {

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
			FenixServiceException newEx =
				new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (person == null)
		{	
			throw new ExcepcaoInexistente("Unknown Person!");
		}
		if (newPassword == null || newPassword.equals("")
					|| person.getNumeroDocumentoIdentificacao() == null
					|| person.getNumeroDocumentoIdentificacao().equalsIgnoreCase(newPassword)
				    || person.getCodigoFiscal() == null
					|| person.getCodigoFiscal().equals(newPassword)
				    || person.getNumContribuinte() == null
					|| person.getNumContribuinte().equals(newPassword)
					|| PasswordEncryptor.areEquals(person.getPassword(), newPassword))
		{
			throw new InvalidPasswordServiceException("Invalid New Password!");
		}
		if (!PasswordEncryptor.areEquals(person.getPassword(), oldPassword)) {
			throw new InvalidPasswordServiceException("Invalid Existing Password!");
		} else {
			// Change the Password
			sp.getIPessoaPersistente().escreverPessoa(person);
			person.setPassword(PasswordEncryptor.encryptPassword(newPassword));
		}
	}
}
