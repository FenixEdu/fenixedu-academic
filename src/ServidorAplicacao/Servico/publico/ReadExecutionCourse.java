package ServidorAplicacao.Servico.publico;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
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

	public Object run(InfoExecutionPeriod infoExecutionPeriod, String code) throws ExcepcaoInexistente, FenixServiceException {

		IExecutionCourse iExecCourse = null;
		InfoExecutionCourse infoExecCourse = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
 			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
			iExecCourse = executionCourseDAO.readByExecutionCourseInitialsAndExecutionPeriod(code, executionPeriod);
			if (iExecCourse != null)
				infoExecCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(iExecCourse);
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoExecCourse;
	}

}
