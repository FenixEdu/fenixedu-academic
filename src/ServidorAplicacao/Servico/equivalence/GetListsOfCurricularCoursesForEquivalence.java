package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class GetListsOfCurricularCoursesForEquivalence implements IServico {

	private static GetListsOfCurricularCoursesForEquivalence service = new GetListsOfCurricularCoursesForEquivalence();

	public static GetListsOfCurricularCoursesForEquivalence getService() {
		return GetListsOfCurricularCoursesForEquivalence.service;
	}

	private GetListsOfCurricularCoursesForEquivalence() {
	}

	public final String getNome() {
		return "GetListsOfCurricularCoursesForEquivalence";
	}

	public InfoEquivalenceContext run(InfoStudent infoStudent, IUserView responsible, InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

		InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

		List studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences = new ArrayList();
		List curricularCourseScopesFromActiveDegreeCurricularPlanForStudent = new ArrayList();

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

			IStudent student = Cloner.copyInfoStudent2IStudent(infoStudent);

			List studentCurricularPlansList = persistentStudentCurricularPlan.readAllFromStudent(student.getNumber().intValue());

			Iterator iterator = studentCurricularPlansList.iterator();
			while(iterator.hasNext()) {
				IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();

				if(!studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE_OBJ)) {
					List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentCurricularPlan);
//					printEnrolments(studentEnrolments);

					List studentAprovedEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
						public boolean evaluate(Object obj) {
							IEnrolment enrolment = (IEnrolment) obj;
							return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
						}
					});
					studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences.addAll(GetListsOfCurricularCoursesForEquivalence.getEnrolmentsWithNoEquivalences(studentAprovedEnrolments, persistentSupport));
				}
			}

			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			final IDegreeCurricularPlan activeDegreeCurricularPlanForStudent = studentActiveCurricularPlan.getDegreeCurricularPlan();
			List curricularCoursesFromActiveDegreeCurricularPlanForStudent = activeDegreeCurricularPlanForStudent.getCurricularCourses();

			List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

			List studentAprovedEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getEnrolmentState().equals(EnrolmentState.APROVED);
				}
			});

			List curricularCoursesScopesFromStudentAprovedEnrolments = (List) CollectionUtils.collect(studentAprovedEnrolments, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getCurricularCourseScope();
				}
			});

			List studentEnroledEnrolments = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
				public boolean evaluate(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED) || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED);
				}
			});

			List curricularCoursesScopesFromStudentEnroledEnrolments = (List) CollectionUtils.collect(studentEnroledEnrolments, new Transformer() {
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getCurricularCourseScope();
				}
			});

//			List otherIrrelevanteCurricularCourses = (List) CollectionUtils.select(curricularCoursesFromActiveDegreeCurricularPlanForStudent, new Predicate() {
//				public boolean evaluate(Object obj) {
//					ICurricularCourse curricularCourse = (ICurricularCourse) obj;
//					return	curricularCourse.getType().equals(CurricularCourseType.TFC_COURSE_OBJ) ||
//							curricularCourse.getType().equals(CurricularCourseType.TRAINING_COURSE_OBJ);
//				}
//			});

			Iterator iterator1 = curricularCoursesFromActiveDegreeCurricularPlanForStudent.iterator();
			List aux1 = new ArrayList();
			while(iterator1.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
//				if	(
//						(curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()) ||
//						(curricularCourseScope.getBranch().getName().equals("") && curricularCourseScope.getBranch().getCode().equals(""))) &&
//						(!curricularCoursesScopesFromStudentAprovedEnrolments.contains(curricularCourseScope)) &&
////						(!otherIrrelevanteCurricularCourses.contains(curricularCourseScope.getCurricularCourse())) &&
//						(!curricularCoursesScopesFromStudentEnroledEnrolments.contains(curricularCourseScope))
//					) {
//					aux1.add(curricularCourseScope);
//				}


				Iterator iterator2 = curricularCourse.getScopes().iterator();
				while(iterator2.hasNext()) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator2.next();
					/**
					 * DAVID: Se o ramo ao qual pertence a disciplina é igual ao ramo do aluno ou
					 * a disicplina é do tronco comum ou
					 * o aluno está no tronco comum e
					 * o aluno não foi já dado como aprovado ou inscrito à disicplina
					 */
					if	(
							(curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()) ||
							(curricularCourseScope.getBranch().getName().equals("") && curricularCourseScope.getBranch().getCode().equals(""))
							|| (studentActiveCurricularPlan.getBranch().getName().equals("") && studentActiveCurricularPlan.getBranch().getCode().equals("")))	&&
							(!curricularCoursesScopesFromStudentAprovedEnrolments.contains(curricularCourseScope)) &&
							(!curricularCoursesScopesFromStudentEnroledEnrolments.contains(curricularCourseScope))
						) {
						aux1.add(curricularCourseScope);
					}
				}


			}

			List aux2 = (List) CollectionUtils.union(curricularCoursesScopesFromStudentAprovedEnrolments, curricularCoursesScopesFromStudentEnroledEnrolments);
			curricularCourseScopesFromActiveDegreeCurricularPlanForStudent.addAll(this.subtract(aux1, aux2));

			if(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences != null && curricularCourseScopesFromActiveDegreeCurricularPlanForStudent != null) {
				infoEquivalenceContext.setInfoEnrolmentsToGiveEquivalence(this.cloneEnrolmentsToInfoEnrolments(studentAprovedEnrolmentsWithDiferentDegreeCurricularPlanAndWithNoEquivalences));
				infoEquivalenceContext.setInfoCurricularCourseScopesToGetEquivalence(this.cloneCurricularCourseScopesToInfoCurricularCourseScopes(curricularCourseScopesFromActiveDegreeCurricularPlanForStudent));
			} else {
				infoEquivalenceContext.setInfoEnrolmentsToGiveEquivalence(new ArrayList());
				infoEquivalenceContext.setInfoCurricularCourseScopesToGetEquivalence(new ArrayList());
			}
			
			infoEquivalenceContext.setCurrentInfoExecutionPeriod(infoExecutionPeriod);
			infoEquivalenceContext.setInfoStudentCurricularPlan(Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentActiveCurricularPlan));
			infoEquivalenceContext.setResponsible(responsible);

			return infoEquivalenceContext;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
	
//	private void printEnrolments(List studentEnrolments) {
//		Iterator iterator = studentEnrolments.iterator();
//		while(iterator.hasNext()) {
//			IEnrolment enrolment = (IEnrolment) iterator.next();
//			System.out.print("Cadeira: " + enrolment.getCurricularCourseScope().getCurricularCourse().getName());
//			System.out.println(" estado: " + enrolment.getEnrolmentState());
//		}
//	}

	private List cloneCurricularCourseScopesToInfoCurricularCourseScopes(List curricularCourseScopes) {
		List infoCurricularCourseScopes = new ArrayList();
		Iterator iterator = curricularCourseScopes.iterator();
		while(iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			InfoCurricularCourseScope infoCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
			infoCurricularCourseScopes.add(infoCurricularCourseScope);
		}
		return infoCurricularCourseScopes;
	}

	private List cloneEnrolmentsToInfoEnrolments(List enrolments) {
		List infoEnrolments = new ArrayList();
		Iterator iterator = enrolments.iterator();
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			InfoEnrolment infoEnrolment = Cloner.copyIEnrolment2InfoEnrolment(enrolment);
			infoEnrolments.add(infoEnrolment);
		}
		return infoEnrolments;
	}

	public static List getEnrolmentsWithNoEquivalences(List enrolments, ISuportePersistente persistentSupport) throws ExcepcaoPersistencia {
		List enrolmentsWithNoEquivalences = new ArrayList();
		Iterator iterator = enrolments.iterator();
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			IPersistentEquivalentEnrolmentForEnrolmentEquivalence persistentEnrolmentEquivalenceRestriction = persistentSupport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();
			List result = persistentEnrolmentEquivalenceRestriction.readByEquivalentEnrolment(enrolment);
			if(result.isEmpty()) {
				enrolmentsWithNoEquivalences.add(enrolment);
			}
		}
		return enrolmentsWithNoEquivalences;
	}

	private List subtract(List finalCurricularCourseScopesList, List curricularCourseScopesToRemove) {

		final List curricularCoursesToRemove = (List) CollectionUtils.collect(curricularCourseScopesToRemove, new Transformer() {
			public Object transform(Object obj) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
				return curricularCourseScope.getCurricularCourse();
			}
		});

		List finalList = (List) CollectionUtils.select(finalCurricularCourseScopesList, new Predicate() {
			public boolean evaluate(Object arg0) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
				return !curricularCoursesToRemove.contains(curricularCourseScope.getCurricularCourse());
			}
		});

		return finalList;
	}

}