/*
 * Created on 2003/07/29
 *
 *
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 * 
 */
public class ReadExecutionCourseByOID implements IServico {

	private static ReadExecutionCourseByOID service =
		new ReadExecutionCourseByOID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionCourseByOID getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionCourseByOID";
	}

	public InfoExecutionCourse run(Integer oid) throws FenixServiceException {

		InfoExecutionCourse result = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionDegreeDAO =
				sp.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) executionDegreeDAO.readByOID(
					DisciplinaExecucao.class,
					oid);
			if (executionCourse != null) {
				result =
					Cloner.copyIExecutionCourse2InfoExecutionCourse(
						executionCourse);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
