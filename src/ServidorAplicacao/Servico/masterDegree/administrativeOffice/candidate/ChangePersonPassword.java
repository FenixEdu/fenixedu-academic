/*
 * Created on 13/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.candidate;

import DataBeans.InfoPerson;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.InvalidPasswordServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChangePersonPassword implements IServico {

	private static ChangePersonPassword servico =
		new ChangePersonPassword();

	/**
	 * The singleton access method of this class.
	 **/
	public static ChangePersonPassword getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ChangePersonPassword() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ChangePersonPassword";
	}

	public void run(InfoPerson infoPerson)
		throws
			ExcepcaoInexistente,
			FenixServiceException,
			InvalidPasswordServiceException {

		// Check if the old password is equal

		ISuportePersistente sp = null;


		String username = new String(infoPerson.getUsername());
		IPessoa person = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			IPessoaPersistente personDAO = sp.getIPessoaPersistente();			
			person = personDAO.lerPessoaPorUsername(username);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx =
				new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (person == null)
			throw new ExcepcaoInexistente("Unknown Person!");
			
		person.setPassword(PasswordEncryptor.encryptPassword(infoPerson.getPassword()));
	}
}
