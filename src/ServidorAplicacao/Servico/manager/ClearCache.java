/*
 * Created on 2003/08/08
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ClearCache implements IService {

	public Boolean run() throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		sp.clearCache();
		return new Boolean(true);
	}

}