//package ServidorAplicacao.strategy.enrolment.strategys.student;
//
//import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAutomaticEnrolmentRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterCurricularYearPrecedence;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterLEQTrainingCourseRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterRestrictedOptionalCoursesRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterRestrictedOptionalDegreeRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentValidateCurricularYearPrecedenceRule;
//import ServidorAplicacao.strategy.enrolment.rules.EnrolmentValidateLEQLaboratoryRule;
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
//public class EnrolmentStrategyLEQ extends EnrolmentStrategy implements IEnrolmentStrategy {
//
//	public EnrolmentStrategyLEQ() {
//	}
//
//	public EnrolmentContext getAvailableCurricularCourses() {
//		
//		IEnrolmentRule enrolmentRule = null;
//
////		if(this.isStudentAlowed()) {
//
//			super.setEnrolmentContext(super.filterBySemester(super.getEnrolmentContext()));
//
//			super.setEnrolmentContext(super.filterByExecutionCourses(super.getEnrolmentContext()));
//
//			super.setEnrolmentContext(super.filterScopesOfCurricularCoursesToBeChosenForOptionalCurricularCourses(super.getEnrolmentContext()));
//
//			enrolmentRule = new EnrolmentFilterCurricularYearPrecedence();
//			super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//			enrolmentRule = new EnrolmentFilterAutomaticEnrolmentRule();
//			super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));		
//
//			enrolmentRule = new EnrolmentFilterLEQTrainingCourseRule();
//			super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));		
//
//			// Esta regra para ser geral para todos os cursos TEM que ser chamada em penultimo
////			enrolmentRule = new EnrolmentApplyPrecedencesRule();
////			super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
//
//			// Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
//			enrolmentRule = new EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule();
//			super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
////		}
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
//		validateRule = new EnrolmentValidateLEQLaboratoryRule();
//		super.setEnrolmentContext(validateRule.apply(super.getEnrolmentContext()));
//
////		validateRule = new EnrolmentApplyPrecedencesForEnrollmentValidationRule();
////		super.setEnrolmentContext(validateRule.apply(super.getEnrolmentContext()));
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
////		enrolmentRule = new EnrolmentApplyPrecedencesForOptionalCurricularCoursesRule();
////		super.setEnrolmentContext(enrolmentRule.apply(super.getEnrolmentContext()));
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
//
////	private boolean isStudentAlowed() {
////		PersistentObjectOJBReader persistentObjectOJB = new PersistentObjectOJBReader();
////		persistentObjectOJB.beginTransaction();
////		Integer firstEnrolmentYear = persistentObjectOJB.readFirstEnrolmentYearOfStudentCurricularPlan(super.getEnrolmentContext().getStudentActiveCurricularPlan());
////		if(firstEnrolmentYear.intValue() < 1997) {
////			super.getEnrolmentContext().setFinalCurricularCoursesScopesSpanToBeEnrolled(new ArrayList());
////			return false;
////		} else {
////			return true;
////		}
////	}
//}