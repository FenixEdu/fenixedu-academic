package ServidorAplicacao.Servico.equivalence;

import java.util.List;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoStudent;
import Dominio.Enrolment;
import Dominio.IEnrolment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MarkType;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class GetEnrolmentEvaluation implements IServico {

	private static GetEnrolmentEvaluation service = new GetEnrolmentEvaluation();

	public static GetEnrolmentEvaluation getService() {
		return GetEnrolmentEvaluation.service;
	}

	private GetEnrolmentEvaluation() {
	}

	public final String getNome() {
		return "GetEnrolmentEvaluation";
	}

	public InfoEnrolmentEvaluation run(InfoStudent infoStudent, InfoEnrolment infoEnrolment) throws FenixServiceException {

		GetEnrolmentGrade service = GetEnrolmentGrade.getService();
		IEnrolment enrolmentCriteria = new Enrolment();
		enrolmentCriteria.setIdInternal(infoEnrolment.getIdInternal());
		IEnrolment enrolment = null;
		try {
			enrolment = (IEnrolment) SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readByOId(enrolmentCriteria, false);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		InfoEnrolmentEvaluation infoEnrolmentEvaluation = service.run(enrolment);

		if(infoEnrolmentEvaluation != null) {
			return infoEnrolmentEvaluation;
		} else {
			InfoEnrolmentEvaluation infoEnrolmentEvaluation2 = new InfoEnrolmentEvaluation();
			infoEnrolmentEvaluation2.setGrade(((String) ((List) MarkType.getMarks(MarkType.TYPE20_OBJ)).get(3)));
			return infoEnrolmentEvaluation2;
		}
	}
}