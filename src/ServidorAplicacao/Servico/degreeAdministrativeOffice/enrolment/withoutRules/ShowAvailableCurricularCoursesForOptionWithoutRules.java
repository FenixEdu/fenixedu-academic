package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ShowAvailableCurricularCoursesForOptionWithoutRules implements IServico {

	private static ShowAvailableCurricularCoursesForOptionWithoutRules _servico = new ShowAvailableCurricularCoursesForOptionWithoutRules();

	public static ShowAvailableCurricularCoursesForOptionWithoutRules getService() {
		return _servico;
	}

	private ShowAvailableCurricularCoursesForOptionWithoutRules() {
	}

	public final String getNome() {
		return "ShowAvailableCurricularCoursesForOptionWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {

		EnrolmentContext enrolmentContext = EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);

		this.apply(enrolmentContext);

		return EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);



		// Small trick to be able to use code from class 'EnrolmentFilterAllOptionalCoursesRule' but with no restrivtion in terms of
		// minimal curricular year for curricular courses that can be chosen for optional courses:
//		int min_year_of_optional_courses = infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().getMinimalYearForOptionalCourses().intValue();
//		infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().setMinimalYearForOptionalCourses(new Integer(1));
//		
//		IEnrolmentRule enrolmentRule = new EnrolmentFilterAllOptionalCoursesRule();
//		EnrolmentContext enrolmentContext = enrolmentRule.apply(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
//		InfoEnrolmentContext infoEnrolmentContext2 = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
//		infoEnrolmentContext2.setOptionalInfoCurricularCoursesToChooseFromDegree(this.filterByExecutionCourses(infoEnrolmentContext2.getOptionalInfoCurricularCoursesToChooseFromDegree(), infoEnrolmentContext2.getInfoExecutionPeriod()));
//
//		infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoDegreeCurricularPlan().setMinimalYearForOptionalCourses(new Integer(min_year_of_optional_courses));
//
//		return infoEnrolmentContext2;
	}

	private void apply(EnrolmentContext enrolmentContext) throws FenixServiceException {

		try {
			List degreeCurricularPlanList = enrolmentContext.getChosenOptionalDegree().getDegreeCurricularPlans();
			
			List listOfCurricularCoursesFromDegrees = this.getListOfCurricularCoursesFromDegrees(degreeCurricularPlanList);
			
			List listOfCurricularCoursesFromStrudentAprovedAndEnroledEnrolments = this.getListOfCurricularCoursesFromStrudentAprovedAndEnroledEnrolments(enrolmentContext);
			
			List listOfCurricularCoursesAlreadyChosenForOption = (List) CollectionUtils.collect(enrolmentContext.getOptionalCurricularCoursesEnrolments(), new Transformer() {
				public Object transform(Object obj) {
					IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) obj;
					return enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();
				}
			});
			
			List listOfCurricularCoursesNotToShow = (List) CollectionUtils.union(listOfCurricularCoursesFromStrudentAprovedAndEnroledEnrolments, listOfCurricularCoursesAlreadyChosenForOption);
			
			List listOfCurricularCoursesToShow = (List) CollectionUtils.subtract(listOfCurricularCoursesFromDegrees, listOfCurricularCoursesNotToShow);
			
			List finalCurricularCourseList = new ArrayList();
			Iterator iterator = listOfCurricularCoursesToShow.iterator();
			while(iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				if( (!finalCurricularCourseList.contains(curricularCourse)) &&
					(!curricularCourse.getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE))) &&
					(!curricularCourse.getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) ) {
						finalCurricularCourseList.add(curricularCourse);
				}
			}
			
			enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(this.filterByExecutionCourses(finalCurricularCourseList, enrolmentContext.getExecutionPeriod()));
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	private List getListOfCurricularCoursesFromDegrees(List listOfDegreeCurricularPlans) {

		List listOfActiveDegreeCurricularPlans = (List) CollectionUtils.select(listOfDegreeCurricularPlans, new Predicate() {
			public boolean evaluate(Object obj) {
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
				return degreeCurricularPlan.getState().equals(new DegreeCurricularPlanState(DegreeCurricularPlanState.ACTIVE));
			}
		});

		if (listOfActiveDegreeCurricularPlans != null && !listOfActiveDegreeCurricularPlans.isEmpty()) {
			List listOfCurricularCoursesFromDegrees = new ArrayList();
			Iterator iterator = listOfActiveDegreeCurricularPlans.iterator();
			while (iterator.hasNext()) {
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
				try {
					ISuportePersistente sp = SuportePersistenteOJB.getInstance();
					IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
					List curricularCoursesList = persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
					listOfCurricularCoursesFromDegrees.addAll(curricularCoursesList);
				} catch (ExcepcaoPersistencia e) {
					e.printStackTrace(System.out);
					throw new IllegalStateException("Cannot read from data base" + e.toString());
				}
			}

//			List listOfCurricularCourseScopesFromDegrees = new ArrayList();
//			Iterator iterator2 = listOfCurricularCoursesFromDegrees.iterator();
//			while (iterator2.hasNext()) {
//				ICurricularCourse curricularCourse = (ICurricularCourse) iterator2.next();
//				listOfCurricularCourseScopesFromDegrees.addAll(curricularCourse.getScopes());
//			}
//
//			return listOfCurricularCourseScopesFromDegrees;

			return listOfCurricularCoursesFromDegrees;
		}
		return null;
	}

	private List getListOfCurricularCoursesFromStrudentAprovedAndEnroledEnrolments(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(enrolmentContext.getStudent().getNumber(), enrolmentContext.getStudent().getDegreeType());
		
		List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);
		
		List aprovedAndEnroledStudentEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return	enrolment.getEnrolmentState().equals(EnrolmentState.APROVED) ||
						enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED) ||
						enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED);
			}
		});
		
		List aprovedAndEnroledStudentCurricularCourses = (List) CollectionUtils.collect(aprovedAndEnroledStudentEnrolments, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourseScope().getCurricularCourse();
			}
		});

		return aprovedAndEnroledStudentCurricularCourses;
	}

	// FIXME [DAVID]: Eventualmente esta filtragem deverá ser feita dentro da própria regra.
	private List filterByExecutionCourses(List curricularCoursesList, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
		IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
		
		List curricularCoursesToRemove = new ArrayList();
		
		Iterator iterator = curricularCoursesList.iterator();
		while (iterator.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
		
			IExecutionCourse executionCourseCriteria = new ExecutionCourse();
			executionCourseCriteria.setExecutionPeriod(executionPeriod);
			executionCourseCriteria.setNome(curricularCourse.getName());
			executionCourseCriteria.setSigla(curricularCourse.getCode());
			List associatedExecutionCourses = persistentExecutionCourse.readByCriteria(executionCourseCriteria);
				
			if(associatedExecutionCourses.isEmpty()){
				curricularCoursesToRemove.add(curricularCourse);
			} else {
				associatedExecutionCourses.clear();
			}
		}
		
		curricularCoursesList.removeAll(curricularCoursesToRemove);
		
		return curricularCoursesList;
	}

}