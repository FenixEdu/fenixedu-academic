package ServidorAplicacao.Servico.manager;

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
public class DeleteDegreesService implements IServico{

	private static DeleteDegreesService service = new DeleteDegreesService();

	public static DeleteDegreesService getService() {
		return service;
	}

	private DeleteDegreesService() {
	}

	public final String getNome() {
		return "DeleteDegreesService";
	}

	public Boolean run(Integer degreeIdInternal)
		throws FenixServiceException	 {

		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
			ICurso degree = persistentDegree.readByIdInternal(degreeIdInternal);
			System.out.println("DEGREE"+degree);
			if (degree != null)
				persistentDegree.delete(degree);
		
			return new Boolean(true);	
		   
		}catch (CantDeletePersistentException e) {
			throw new CantDeleteServiceException(e);
		}catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
			}
		
	}

}