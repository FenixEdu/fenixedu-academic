/*
 * Created on 30/Mai/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import Dominio.Curso;
import Dominio.ICurso;
import Dominio.ISection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertDegreeService implements IServico {

	private static InsertDegreeService service = new InsertDegreeService();

	public static InsertDegreeService getService() {
		return service;
	}

	private InsertDegreeService() {
	}

	public final String getNome() {
		return "InsertDegreeService";
	}
	

	public Boolean run(InfoDegree infoDegree) throws FenixServiceException {

		ICurso degree = null;
		ISection section = null;

		ICursoPersistente persistentDegree = null;
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			persistentDegree = persistentSuport.getICursoPersistente();

			degree =new Curso(
							infoDegree.getSigla(),
							infoDegree.getNome(),
							infoDegree.getTipoCurso());
	
			persistentDegree.lockWrite(degree);

		} catch (ExistingPersistentException e) {

			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}
}