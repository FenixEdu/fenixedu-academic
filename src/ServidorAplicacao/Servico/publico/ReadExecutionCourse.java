package ServidorAplicacao.Servico.publico;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class ReadExecutionCourse implements IServico {

	private static ReadExecutionCourse _servico = new ReadExecutionCourse();

	/**
	  * The actor of this class.
	  **/

	private ReadExecutionCourse() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ReadExecutionCourse";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ReadExecutionCourse getService() {
		return _servico;
	}

	public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) throws ExcepcaoInexistente, NotExecutedException {

		IDisciplinaExecucao iExecCourse = null;
		InfoExecutionCourse infoExecCourse = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
 			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
			iExecCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(code, executionPeriod);
			infoExecCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(iExecCourse);
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			NotExecutedException newEx = new NotExecutedException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoExecCourse;
	}

}
