/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditDegree implements IServico {

	private static EditDegree service = new EditDegree();

	/**
	 * The singleton access method of this class.
	 */
	public static EditDegree getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private EditDegree() {
	}

	/**
	 * Service name
	 */
	public final String getNome() {
		return "EditDegree";
	}

	/**
	 * Executes the service.
	 */

	public void run(InfoDegree newInfoDegree) throws FenixServiceException {

		ISuportePersistente persistentSuport = null;
		ICursoPersistente persistentDegree = null;
		ICurso oldDegree = null;

		try {
			
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentDegree = persistentSuport.getICursoPersistente();
			oldDegree = persistentDegree.readByIdInternal(newInfoDegree.getIdInternal());
			
			if(oldDegree == null)
				throw new NonExistingServiceException();
			
			oldDegree.setNome(newInfoDegree.getNome());
			oldDegree.setSigla(newInfoDegree.getSigla());
			oldDegree.setTipoCurso(newInfoDegree.getTipoCurso());

			persistentDegree.lockWrite(oldDegree);
						
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}