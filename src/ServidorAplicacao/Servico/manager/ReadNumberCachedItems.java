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
public class ReadNumberCachedItems implements IServico {

	private ReadNumberCachedItems() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadNumberCachedItems";
	}

	private static ReadNumberCachedItems service =
		new ReadNumberCachedItems();

	public static ReadNumberCachedItems getService() {
		return service;
	}

	/**
	 * Returns info list of all execution periods.
	 */
	public Integer run() throws FenixServiceException {
		Integer numberCachedObjects = null;
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			numberCachedObjects = sp.getNumberCachedItems();
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return numberCachedObjects;
	}

}
