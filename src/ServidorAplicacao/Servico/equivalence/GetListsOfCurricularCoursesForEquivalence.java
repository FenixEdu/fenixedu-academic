package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoExecutionPeriod;
import DataBeans.degreeAdministrativeOffice.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;

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

	public InfoEquivalenceContext run(IUserView userView, InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

		InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

			IStudent student = persistentStudent.readByUsername(((UserView) userView).getUtilizador());
			IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
			final IDegreeCurricularPlan currentDegreeCurricularPlanForStudent = studentActiveCurricularPlan.getDegreeCurricularPlan();
			List curricularCoursesFromCurrentDegreeCurricularPlanForStudent = currentDegreeCurricularPlanForStudent.getCurricularCourses();

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

			List otherIrelevanteCurricularCourses = (List) CollectionUtils.select(curricularCoursesFromCurrentDegreeCurricularPlanForStudent, new Predicate() {
				public boolean evaluate(Object obj) {
					ICurricularCourse curricularCourse = (ICurricularCourse) obj;
					return	curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ) ||
							curricularCourse.getType().equals(CurricularCourseType.TFC_COURSE_OBJ) ||
							curricularCourse.getType().equals(CurricularCourseType.TRAINING_COURSE_OBJ);
				}
			});

			List curricularCourseScopesFromCurrentDegreeCurricularPlanForStudent = new ArrayList();
			Iterator iterator1 = curricularCoursesFromCurrentDegreeCurricularPlanForStudent.iterator();
			while(iterator1.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
				List scopes = curricularCourse.getScopes();
				Iterator iterator2 = scopes.iterator();
				while(iterator2.hasNext()) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator2.next();
					if	(
							(curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()) ||
							(curricularCourseScope.getBranch().getName().equals("") && curricularCourseScope.getBranch().getCode().equals(""))) &&
							(!curricularCoursesScopesFromStudentAprovedEnrolments.contains(curricularCourseScope)) &&
							(!curricularCoursesScopesFromStudentEnroledEnrolments.contains(curricularCourseScope)) &&
							(!otherIrelevanteCurricularCourses.contains(curricularCourseScope.getCurricularCourse()))
						) {
						// TODO DAVID-RICARDO: Perguntar se são estas mesmo as disciplinas a remover (opções, TFC, estágios, etc...)
						// In this manner, for this list, we are excluding curricular courses from other branches that not the student's;
						// curricular courses already aproved, enroled and temporarily enroled;
						// optional curricular courses; TFC and training curricular courses.
						curricularCourseScopesFromCurrentDegreeCurricularPlanForStudent.add(curricularCourseScope);
					}
				}
			}

			List curricularCoursesScopesFromStudentAprovedEnrolmentsWithDiferentDegreeCurricularPlan = (List) CollectionUtils.select(curricularCoursesScopesFromStudentAprovedEnrolments, new Predicate() {
				public boolean evaluate(Object obj) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
					return !curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan().equals(currentDegreeCurricularPlanForStudent);
				}
			});

			if(curricularCoursesScopesFromStudentAprovedEnrolments != null && curricularCourseScopesFromCurrentDegreeCurricularPlanForStudent != null) {
				infoEquivalenceContext.setInfoCurricularCourseScopesToGiveEquivalence(this.cloneCurricularCourseScopesToInfoCurricularCourseScopes(curricularCoursesScopesFromStudentAprovedEnrolmentsWithDiferentDegreeCurricularPlan));
				infoEquivalenceContext.setInfoCurricularCourseScopesToGetEquivalence(this.cloneCurricularCourseScopesToInfoCurricularCourseScopes(curricularCourseScopesFromCurrentDegreeCurricularPlanForStudent));
			} else {
				infoEquivalenceContext.setInfoCurricularCourseScopesToGiveEquivalence(new ArrayList());
				infoEquivalenceContext.setInfoCurricularCourseScopesToGetEquivalence(new ArrayList());
			}
			
			infoEquivalenceContext.setCurrentInfoExecutionPeriod(infoExecutionPeriod);
			infoEquivalenceContext.setInfoStudentCurricularPlan(Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentActiveCurricularPlan));

			return infoEquivalenceContext;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
	
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
}