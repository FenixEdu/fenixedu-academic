package ServidorAplicacao.Servico.equivalence;

import DataBeans.equivalence.InfoEquivalenceContext;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ValidateEquivalence implements IServico {

	private static ValidateEquivalence service = new ValidateEquivalence();

	public static ValidateEquivalence getService() {
		return ValidateEquivalence.service;
	}

	private ValidateEquivalence() {
	}

	public final String getNome() {
		return "ValidateEquivalence";
	}

	public InfoEquivalenceContext run(InfoEquivalenceContext infoEquivalenceContext) throws FenixServiceException {

		if(infoEquivalenceContext.getErrorMessages() != null) {
			infoEquivalenceContext.getErrorMessages().clear();
		} 

		if(infoEquivalenceContext.getChosenInfoEnrolmentsToGiveEquivalence().isEmpty()) {
			infoEquivalenceContext.setErrorMessage("error.no.curricular.courses.to.give.equivalence");
		}

		if(infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalence().isEmpty()) {
			infoEquivalenceContext.setErrorMessage("error.no.curricular.courses.to.get.equivalence");
		}

		if((infoEquivalenceContext.getErrorMessages() == null) || infoEquivalenceContext.getErrorMessages().isEmpty()) {
			infoEquivalenceContext.setSuccess(true);
		} else {
			infoEquivalenceContext.setSuccess(false);
		}

		return infoEquivalenceContext;
	}
}