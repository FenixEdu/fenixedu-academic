/*
 * Created on 2003/08/08
 * 
 */
package ServidorAplicacao.Servico.manager;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ClearCache implements IServico {

	private ClearCache() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ClearCache";
	}

	private static ClearCache service =
		new ClearCache();

	public static ClearCache getService() {
		return service;
	}

	/**
	 * Returns info list of all execution periods.
	 */
	public Boolean run() throws FenixServiceException {

		Boolean result = new Boolean(false);
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.clearCache();
			result = new Boolean(true);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return result;
	}

}
