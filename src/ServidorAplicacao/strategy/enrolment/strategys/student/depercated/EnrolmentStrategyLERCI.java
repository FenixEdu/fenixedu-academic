//package ServidorAplicacao.strategy.enrolment.strategys.student;
//
//import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalCoursesRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalDegreesRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAnualCurricularCourseRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAutomaticEnrolmentRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterBranchRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterCurricularYearPrecedence;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentValidateCurricularYearPrecedenceRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentValidateNACandNDRule;
//import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
//import ServidorAplicacao.strategy.enrolment.strategys.EnrolmentStrategy;
//import ServidorAplicacao.strategy.enrolment.strategys.IEnrolmentStrategy;
//
///**
// * @author dcs-rjao
// *
// * 3/Abr/2003
// */
//public class EnrolmentStrategyLERCI extends EnrolmentStrategy implements IEnrolmentStrategy {
//
//	public EnrolmentStrategyLERCI() {
//	}
//
//	public EnrolmentContext getAvailableCurricularCourses() {
//		IEnrolmentRule enrolmentRule = null;
//
//		super.setEnrolmentContext(super.filterBySemester(super.getEnrolmentContext()));
//
//		enrolmentRule = new EnrolmentFilterBranchRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		super.setEnrolmentContext(super.filterByExecutionCourses(super.getEnrolmentContext()));
//
//		enrolmentRule = new EnrolmentFilterCurricularYearPrecedence();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		enrolmentRule = new EnrolmentFilterAutomaticEnrolmentRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));		
//
//		enrolmentRule = new EnrolmentFilterAnualCurricularCourseRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		// Esta regra para ser geral para todos os cursos TEM que ser chamada em penultimo
////		enrolmentRule = new EnrolmentApplyPrecedencesRule();
////		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		// Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
//		enrolmentRule = new EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//
//	public EnrolmentContext validateEnrolment() {
//		IEnrolmentRule validateRule = null;
//
//		validateRule = new EnrolmentValidateNACandNDRule();
//		super.setEnrolmentContext(validateRule.apply(super.getEnrolmentContext()));
//
//		validateRule = new EnrolmentValidateCurricularYearPrecedenceRule();
//		super.setEnrolmentContext(validateRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//
//	public EnrolmentContext getOptionalCurricularCourses() {
//		IEnrolmentRule enrolmentRule = null;
//
//		enrolmentRule = new EnrolmentFilterAllOptionalCoursesRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//
//	public EnrolmentContext getDegreesForOptionalCurricularCourses() {
//
//		IEnrolmentRule enrolmentRule = null;
//
//		enrolmentRule = new EnrolmentFilterAllOptionalDegreesRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//}