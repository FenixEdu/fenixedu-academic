package ServidorAplicacao.Servico.equivalence;

import java.util.Iterator;

import DataBeans.equivalence.InfoCurricularCourseScopeGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ConfirmEquivalence implements IServico {

	private static ConfirmEquivalence service = new ConfirmEquivalence();

	public static ConfirmEquivalence getService() {
		return ConfirmEquivalence.service;
	}

	private ConfirmEquivalence() {
	}

	public final String getNome() {
		return "ConfirmEquivalence";
	}

	public InfoEquivalenceContext run(InfoEquivalenceContext infoEquivalenceContext) throws FenixServiceException {
		Iterator iterator = infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade().iterator();
		while(iterator.hasNext()) {
			InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade = (InfoCurricularCourseScopeGrade) iterator.next();
			System.out.println(infoCurricularCourseScopeGrade);
		}
		return infoEquivalenceContext;
	}
}