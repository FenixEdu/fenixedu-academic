/*
 * Created on 15/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.CantDeletePersistentException;

/**
 * @author lmac1
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
		throws FenixServiceException {

		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
			String sigla =infoDegree.getSigla();
			
			ICurso degree = persistentDegree.readBySigla(sigla);
			
			if (degree != null)
				persistentDegree.delete(degree);
		
			return new Boolean(true);	
		   
		}catch (CantDeletePersistentException e) {
			throw new FenixServiceException(e);
		}catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
			}
		
	}

}