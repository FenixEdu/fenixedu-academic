/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

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
import Util.TipoCurso;

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
	 * Executes the service. Returns the current infodegree.
	 */

	public void run(Integer oldDegreeId, InfoDegree newInfoDegree) throws FenixServiceException {

		ISuportePersistente persistentSuport = null;
		ICursoPersistente persistentDegree = null;
		ICurso oldDegree = null;
		String newCode = null;
		String newName = null;
		TipoCurso newType = null;

		try {
			persistentSuport = SuportePersistenteOJB.getInstance();
			persistentDegree = persistentSuport.getICursoPersistente();
			oldDegree = persistentDegree.readByIdInternal(oldDegreeId);

			List degrees = persistentDegree.readAll();
			degrees.remove((ICurso) oldDegree);

			newCode = newInfoDegree.getSigla();
			newName = newInfoDegree.getNome();
			newType = newInfoDegree.getTipoCurso();
			if (oldDegree != null) {
				oldDegree.setNome(newName);
				oldDegree.setSigla(newCode);
				oldDegree.setTipoCurso(newType);

				try {
					persistentDegree.lockWrite(oldDegree);
				} catch (ExistingPersistentException ex) {
					throw new ExistingServiceException("O curso com esses dados", ex);
				}
			}else
			throw new NonExistingServiceException();

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}