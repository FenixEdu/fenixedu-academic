/*
 * Created on 17/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.Branch;
import Dominio.IBranch;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteBranches implements IServico {

	private static DeleteBranches service = new DeleteBranches();

	public static DeleteBranches getService() {
		return service;
	}

	private DeleteBranches() {
	}

	public final String getNome() {
		return "DeleteBranches";
	}
	
// delete a set of branches
	public List run(List internalIds) throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentBranch persistentBranch = sp.getIPersistentBranch();

			Iterator iter = internalIds.iterator();

			Boolean result = new Boolean(true);
			List undeletedCodes = new ArrayList();
			Integer internalId;
			IBranch branch;
			IBranch iterBranch = new Branch();

			while(iter.hasNext()) {
				internalId = (Integer) iter.next();
				iterBranch.setIdInternal(internalId);
				branch = (IBranch) persistentBranch.readByOId(iterBranch, false);
				if(branch != null)
					result = persistentBranch.delete(branch);			
				if(result.equals(new Boolean(false)))				
					undeletedCodes.add((String) branch.getCode());
				result = new Boolean(true);	
			}
			
			return undeletedCodes;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}