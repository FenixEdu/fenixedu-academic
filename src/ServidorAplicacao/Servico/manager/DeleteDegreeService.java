package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
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
public class DeleteDegreeService implements IServico{

	private static DeleteDegreeService service = new DeleteDegreeService();

	public static DeleteDegreeService getService() {
		return service;
	}

	private DeleteDegreeService() {
	}

	public final String getNome() {
		return "DeleteDegreeService";
	}

	public Boolean run(InfoDegree infoDegree)
		throws FenixServiceException	 {

		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
			String sigla =infoDegree.getSigla();
			
			ICurso degree = persistentDegree.readBySigla(sigla);
			
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