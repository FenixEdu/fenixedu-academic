package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoMark;
import DataBeans.InfoSiteSubmitMarks;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.Enrolment;
import Dominio.Evaluation;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEvaluation;
import Dominio.IFrequenta;
import Dominio.IMark;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *
 */
public class ValidateSubmitMarks implements IServico {
	private static ValidateSubmitMarks _service = new ValidateSubmitMarks();

	private List enrolmentEvaluationList = new ArrayList();
	private List errorsNotEnrolmented = new ArrayList();
	private List errorsMarkNotPublished = new ArrayList();
	private boolean noMarks = true;
	private boolean allMarksNotPublished = true;

	/**
	 * The actor of this class.
	 **/
	private ValidateSubmitMarks() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ValidateSubmitMarks";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ValidateSubmitMarks getService() {
		return _service;
	}

	public InfoSiteSubmitMarks run(Integer executionCourseCode, Integer evaluationCode, UserView userView)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//execution course and execution course's site
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(executionCourseCode);
			executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);

			//evaluation
			IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
			IEvaluation evaluation = new Evaluation();
			evaluation.setIdInternal(evaluationCode);
			evaluation = (IEvaluation) persistentEvaluation.readByOId(evaluation, false);

			//final evaluation's marks list
			IPersistentMark persistentMark = sp.getIPersistentMark();
			List marksList = persistentMark.readBy(evaluation);

			List infoMarksList = verifySubmitMarks(marksList);

			return createInfoErrors(evaluation, infoMarksList, errorsNotEnrolmented, errorsMarkNotPublished, allMarksNotPublished, noMarks);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e.getMessage());
		}
	}

	private List verifySubmitMarks(List marksList) throws FenixServiceException {
		List infoMarksList = null;
		ISuportePersistente sp;

		errorsMarkNotPublished.clear();
		errorsNotEnrolmented.clear();
		try {
			sp = SuportePersistenteOJB.getInstance();

			if (marksList == null || marksList.size() <= 0) {
				throw new FenixServiceException("errors.submitMarks.noMarks");
			} else {
				IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
				IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();

				infoMarksList = new ArrayList();
				Iterator iterador = marksList.listIterator();
				while (iterador.hasNext()) {
					IMark mark = (IMark) iterador.next();

					if ((mark.getMark() == null) || (mark.getMark().length() <= 0)) {
						errorsMarkNotPublished.add(Cloner.copyIMark2InfoMark(mark));
						continue;
					} else {
						noMarks = false;
					}

					if ((mark.getPublishedMark() == null) || (mark.getPublishedMark().length() <= 0)) {
						errorsMarkNotPublished.add(Cloner.copyIMark2InfoMark(mark));
						continue;
					} else {
						allMarksNotPublished = false;
					}

					//attend
					IFrequenta frequenta = new Frequenta();
					frequenta.setIdInternal(mark.getKeyAttend());
					frequenta = (Frequenta) frequentaPersistente.readByOId(frequenta, false);

					//enrolment
					IEnrolment enrolment = new Enrolment();
					enrolment.setIdInternal(frequenta.getKeyEnrolment());
					enrolment = (Enrolment) persistentEnrolment.readByOId(enrolment, false);
					if (enrolment == null) {
						errorsNotEnrolmented.add(Cloner.copyIMark2InfoMark(mark));
						continue;
					}
					
					//data for enrolment evaluation				
					verifyYetSubmitMarks(enrolment);
					
					InfoMark infoMark = Cloner.copyIMark2InfoMark(mark);

					infoMarksList.add(infoMark);
				}
			}

			if (noMarks) {
				throw new FenixServiceException("errors.submitMarks.noMarks");
			}
			if (allMarksNotPublished) {
				throw new FenixServiceException("errors.submitMarks.allMarksNotPublished");
			}

			return infoMarksList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e.getMessage());
		}
	}

	private void verifyYetSubmitMarks(IEnrolment enrolment) throws FenixServiceException {
		ISuportePersistente sp;

		try {
			sp = SuportePersistenteOJB.getInstance();

			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();

			//verify if marks yet submit
			List enrolmentEvaluationList = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);
			if (enrolmentEvaluationList != null && enrolmentEvaluationList.size() > 0) {
				List enrolmentEvaluationListWithGrade = (List) CollectionUtils.select(enrolmentEvaluationList, new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) obj;
						return enrolmentEvaluation.getGrade().length() > 0;
					}
				});
				if (enrolmentEvaluationList.size() == enrolmentEvaluationListWithGrade.size()) {
					throw new FenixServiceException("errors.submitMarks.yetSubmited");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e.getMessage());
		}
	}

	private InfoSiteSubmitMarks createInfoErrors(
		IEvaluation evaluation,
		List marksList,
		List errorsNotEnrolmented,
		List errorsMarkNotPublished,
		boolean allMarksNotPublished,
		boolean noMarks)
		throws FenixServiceException {

		InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

		infoSiteSubmitMarks.setInfoEvaluation(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
		infoSiteSubmitMarks.setMarksList(marksList);
		infoSiteSubmitMarks.setErrorsNotEnrolmented(errorsNotEnrolmented);
		infoSiteSubmitMarks.setErrorsMarkNotPublished(errorsMarkNotPublished);
		infoSiteSubmitMarks.setAllMarksNotPublished(allMarksNotPublished);
		infoSiteSubmitMarks.setNoMarks(noMarks);

		return infoSiteSubmitMarks;
	}
}