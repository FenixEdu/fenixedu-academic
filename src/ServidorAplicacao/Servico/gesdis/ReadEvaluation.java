/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis;

import DataBeans.InfoCurriculum;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
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

	public InfoCurriculum run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
		try {
			
			InfoCurriculum infoCurriculum = null;
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
			
			ICurriculum curriculum = sp.getIPersistentCurriculum().readCurriculumByCurricularCourse((ICurricularCourse) executionCourse);			
			
			if(curriculum != null){
				infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
			}
			return infoCurriculum;

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
