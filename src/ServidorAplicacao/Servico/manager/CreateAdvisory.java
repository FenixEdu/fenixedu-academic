/*
 * Created on 2003/09/06
 * 
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoAdvisory;
import DataBeans.util.Cloner;
import Dominio.IAdvisory;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AdvisoryRecipients;

/**
 * @author Luis Crus
 */
public class CreateAdvisory implements IServico {

	private static CreateAdvisory _servico = new CreateAdvisory();
	/**
	 * The singleton access method of this class.
	 **/
	public static CreateAdvisory getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateAdvisory() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CreateAdvisory";
	}

	public Boolean run(
		InfoAdvisory infoAdvisory,
		AdvisoryRecipients advisoryRecipients)
		throws FenixServiceException {

		Boolean result = new Boolean(false);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAdvisory advisory = Cloner.copyInfoAdvisory2IAdvisory(infoAdvisory);
			
			sp.getIPersistentAdvisory().write(advisory, advisoryRecipients);

			result = new Boolean(true);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}

}