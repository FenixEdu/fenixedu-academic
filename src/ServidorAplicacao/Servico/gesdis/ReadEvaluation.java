/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis;

import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ReadEvaluation implements IServico {

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */

	private static ReadEvaluation service = new ReadEvaluation();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadEvaluation getService() {
		return service;
	}

	/**
	 * The ctor of this class.
	 **/
	private ReadEvaluation() {
	}
	public String getNome() {
		return "ReadEvaluation";
	}

	public InfoEvaluation run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
		try {
			InfoEvaluation infoEvaluation = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
			IEvaluation  evaluation = sp.getIPersistentEvaluation().readByExecutionCourse(executionCourse);
			if (evaluation != null) {
				infoEvaluation=Cloner.copyIEvaluation2InfoEvaluation(evaluation);
			}
		
			return infoEvaluation;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
