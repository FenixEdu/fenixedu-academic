/*
 *
 * Created on 2003/08/22
 */

package ServidorAplicacao.Servico.sop;

/**
 *
 * @author Luis Cruz & Sara Ribeiro
 **/

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditExecutionCourse implements IServico {

	private static EditExecutionCourse _servico = new EditExecutionCourse();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditExecutionCourse getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EditExecutionCourse() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditExecutionCourse";
	}

	public InfoExecutionCourse run(InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			
			IExecutionCourse executionCourseAux = new ExecutionCourse(infoExecutionCourse.getIdInternal());
						
			IExecutionCourse executionCourse =
				(IExecutionCourse) executionCourseDAO.readByOId(executionCourseAux, true);
				
			if (executionCourse == null) {
				throw new NonExistingServiceException();
			}

			//executionCourseDAO.lockWrite(executionCourse);

			executionCourse.setTheoreticalHours(
				infoExecutionCourse.getTheoreticalHours());
			executionCourse.setTheoPratHours(
				infoExecutionCourse.getTheoPratHours());
			executionCourse.setPraticalHours(
				infoExecutionCourse.getPraticalHours());
			executionCourse.setLabHours(infoExecutionCourse.getLabHours());

			infoExecutionCourse =
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse);
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return infoExecutionCourse;
	}

}