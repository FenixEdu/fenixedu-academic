package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.List;

import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.CantDeleteServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.CantDeletePersistentException;

/**
 * @author lmac1
 */
public class DeleteDegreesService implements IServico {

	private static DeleteDegreesService service = new DeleteDegreesService();

	public static DeleteDegreesService getService() {
		return service;
	}

	private DeleteDegreesService() {
	}

	public final String getNome() {
		return "DeleteDegreesService";
	}
	
// delete a set of degrees
	public Boolean run(List degreesInternalIds) throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoPersistente persistentDegree = sp.getICursoPersistente();

			Iterator iter = degreesInternalIds.iterator();

			while (iter.hasNext()) {
				try {

					Integer internalId = (Integer) iter.next();
					ICurso degree =
						persistentDegree.readByIdInternal(internalId);
					if (degree != null)
						persistentDegree.delete(degree);
				} catch (CantDeletePersistentException e) {
					throw new CantDeleteServiceException(e.getMessage(), e);
				}
			}
			return new Boolean(true);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}