package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
	public List run(List degreesInternalIds) throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoPersistente persistentDegree = sp.getICursoPersistente();

			Iterator iter = degreesInternalIds.iterator();
			String undeletedDegreeName = "null";
			List undeletedDegreesNames = new ArrayList();

			while (iter.hasNext()) {

					Integer internalId = (Integer) iter.next();
					ICurso degree =
						persistentDegree.readByIdInternal(internalId);
					if (degree != null)
						undeletedDegreeName = persistentDegree.delete(degree);
				
					undeletedDegreesNames.add((String) undeletedDegreeName);
				
			}
			
			return undeletedDegreesNames;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}