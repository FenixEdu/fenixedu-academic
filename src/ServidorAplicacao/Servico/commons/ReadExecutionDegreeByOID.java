/*
 * Created on 2003/07/29
 *
 *
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 * 
 */
public class ReadExecutionDegreeByOID implements IServico {

	private static ReadExecutionDegreeByOID service =
		new ReadExecutionDegreeByOID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionDegreeByOID getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionDegreeByOID";
	}

	public InfoExecutionDegree run(Integer oid) throws FenixServiceException {

		InfoExecutionDegree result = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucaoPersistente executionDegreeDAO =
				sp.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree =
				(ICursoExecucao) executionDegreeDAO.readByOID(
					CursoExecucao.class,
					oid);
			if (executionDegree != null) {
				result =
					Cloner.copyIExecutionDegree2InfoExecutionDegree(
						executionDegree);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
