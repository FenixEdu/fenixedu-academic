/*
 * Created on 2004/07/18
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *
 */
public class ConfirmManagerIdentity implements IService {

	public ConfirmManagerIdentity() {
	}

	public Boolean run() {
		// Authentication is taken care of by the filters.
		return new Boolean(true);
	}

}
