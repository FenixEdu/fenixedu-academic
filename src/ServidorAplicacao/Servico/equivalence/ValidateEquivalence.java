package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.equivalence.InfoCurricularCourseScopeGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MarkType;

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

		if(infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade() != null) {
			IDegreeCurricularPlan degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoEquivalenceContext.getInfoStudentCurricularPlan().getInfoDegreeCurricularPlan());
			IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
			IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
			boolean toClear = false;
			Iterator iterator = infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade().iterator();
			while(iterator.hasNext()) {
				InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade = (InfoCurricularCourseScopeGrade) iterator.next();
				if(!degreeCurricularPlanStrategy.checkMark(infoCurricularCourseScopeGrade.getGrade())) {
					infoEquivalenceContext.setErrorMessage("error.wrong.grade");
					toClear = true;
					break;
				}
			}
			if(toClear) {
				infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade().clear();
			}
		}
		
		if((infoEquivalenceContext.getErrorMessages() == null) || infoEquivalenceContext.getErrorMessages().isEmpty()) {
			infoEquivalenceContext.setSuccess(true);
		} else {
			infoEquivalenceContext.setSuccess(false);
		}
		
		if(infoEquivalenceContext.isSuccess()) {
			List chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade = new ArrayList();
			Iterator iterator = infoEquivalenceContext.getChosenInfoEnrolmentsToGiveEquivalence().iterator();
			while(iterator.hasNext()) {
				InfoEnrolment infoEnrolment = (InfoEnrolment) iterator.next();
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
				InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade = new InfoCurricularCourseScopeGrade();
				infoCurricularCourseScopeGrade.setInfoCurricularCourseScope(infoEnrolment.getInfoCurricularCourseScope());
				if(infoEnrolmentEvaluation != null) {
					infoCurricularCourseScopeGrade.setGrade(infoEnrolmentEvaluation.getGrade());
				} else {
					infoCurricularCourseScopeGrade.setGrade(((String) ((List) MarkType.getMarks(MarkType.TYPE20_OBJ)).get(3)));
				}
				chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade.add(infoCurricularCourseScopeGrade);
			}
			infoEquivalenceContext.setChosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade(chosenInfoCurricularCourseScopesToGiveEquivalenceWithGrade);
		}

		return infoEquivalenceContext;
	}
}