/*
 * Created on 30/Mai/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegree;
import Dominio.Curso;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class InsertDegree implements IServico {

	private static InsertDegree service = new InsertDegree();

	public static InsertDegree getService() {
		return service;
	}

	private InsertDegree() {
	}

	public final String getNome() {
		return "InsertDegree";
	}
	

	public void run(InfoDegree infoDegree) throws FenixServiceException {

		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();
			
				String code = infoDegree.getSigla();
				String name = infoDegree.getNome();
				TipoCurso type = infoDegree.getTipoCurso();

				ICurso degree = new Curso(code, name, infoDegree.getTipoCurso());
	
				persistentDegree.lockWrite(degree);

		} catch(ExistingPersistentException existingException) {
			throw new ExistingServiceException(existingException);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}