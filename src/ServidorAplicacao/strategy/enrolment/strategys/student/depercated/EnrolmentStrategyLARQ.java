//package ServidorAplicacao.strategy.enrolment.strategys.student;
//
//import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAutomaticEnrolmentRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterCurricularYearPrecedence;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterRestrictedOptionalCoursesRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterRestrictedOptionalDegreeRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterTFCRule;
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
//public class EnrolmentStrategyLARQ extends EnrolmentStrategy implements IEnrolmentStrategy {
//
//	public EnrolmentStrategyLARQ() {
//	}
//
//	public EnrolmentContext getAvailableCurricularCourses() {
//		
//		IEnrolmentRule enrolmentRule = null;
//
//		super.setEnrolmentContext(super.filterBySemester(super.getEnrolmentContext()));
//
//		super.setEnrolmentContext(super.filterByExecutionCourses(super.getEnrolmentContext()));
//
////		enrolmentRule = new EnrolmentFilterPrecedenceRule();
////		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);
//
//		enrolmentRule = new EnrolmentFilterCurricularYearPrecedence();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		enrolmentRule = new EnrolmentFilterAutomaticEnrolmentRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));		
//
//		enrolmentRule = new EnrolmentFilterTFCRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
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
//		enrolmentRule = new EnrolmentFilterRestrictedOptionalCoursesRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//
//	public EnrolmentContext getDegreesForOptionalCurricularCourses() {
//
//		IEnrolmentRule enrolmentRule = null;
//
//		enrolmentRule = new EnrolmentFilterRestrictedOptionalDegreeRule();
//		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//		return super.getEnrolmentContext();
//	}
//}