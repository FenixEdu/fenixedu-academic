/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEvaluation;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author João Mota
 *
 * 
 */
public class EditEvaluation implements IServico {

	private static EditEvaluation service = new EditEvaluation();

	/**
	 * The singleton access method of this class.
	 **/
	public static EditEvaluation getService() {
		return service;
	}

	/**
	 * The ctor of this class.
	 **/
	private EditEvaluation() {
	}
	public String getNome() {
		return "EditEvaluation";
	}

	public Boolean run(
		InfoEvaluation infoEvaluationOld,
		InfoEvaluation infoEvaluationNew)
		throws FenixServiceException {

		try {
			IEvaluation evaluationNew =
				Cloner.copyInfoEvaluation2IEvaluation(infoEvaluationNew);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			if (infoEvaluationOld != null) {

				IEvaluation evaluationOld =
					Cloner.copyInfoEvaluation2IEvaluation(infoEvaluationOld);

				IEvaluation evaluationFromDB =
					sp.getIPersistentEvaluation().readByExecutionCourse(
						evaluationOld.getExecutionCourse());

				if (evaluationFromDB != null) {
					evaluationFromDB.setEvaluationElements(
						evaluationNew.getEvaluationElements());
					evaluationFromDB.setEvaluationElementsEn(
						evaluationNew.getEvaluationElementsEn());
				}
				sp.getIPersistentEvaluation().lockWrite(evaluationFromDB);
			} else {

				evaluationNew.setExecutionCourse(
					sp
						.getIDisciplinaExecucaoPersistente()
						.readByExecutionCourseInitialsAndExecutionPeriod(
							evaluationNew.getExecutionCourse().getSigla(),
							evaluationNew
								.getExecutionCourse()
								.getExecutionPeriod()));

				sp.getIPersistentEvaluation().lockWrite(evaluationNew);
			}
			return new Boolean(true);
		} catch (ExistingPersistentException e) {
			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

}
