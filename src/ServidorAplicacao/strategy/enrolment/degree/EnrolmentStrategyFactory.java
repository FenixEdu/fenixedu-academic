package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLERCI;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyFactory {

	public static final int LERCI = 1;

	private static IEnrolmentStrategy strategyInstance = null;

	private static SuportePersistenteOJB persistentSupport = null;
	private static IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	private static IPersistentCurricularCourse persistentCurricularCourse = null;
	
	private static IPersistentEnrolment persistentEnrolment = null;

	public static synchronized IEnrolmentStrategy getEnrolmentStrategyInstance(int degree, EnrolmentContext enrolmentContext)
		throws ExcepcaoPersistencia {
		if (enrolmentContext.getStudent() == null)
			throw new IllegalArgumentException("Must initialize student in context!");
			
		if (enrolmentContext.getSemester() == null)
			throw new IllegalArgumentException("Must initialize semester in context!");

		if (enrolmentContext.getDegree() == null)
			throw new IllegalArgumentException("Must initialize degree in context!");
			
		if (strategyInstance == null) {
			switch (degree) {
				case LERCI :
					strategyInstance = new EnrolmentStrategyLERCI();
					strategyInstance.setEnrolmentContext(prepareEnrolmentContext(enrolmentContext));
					break;
				default :
					break;
			}
		}
		return strategyInstance;
	}

	public static synchronized void resetInstance() {
		if (strategyInstance != null) {
			strategyInstance = null;
		}
	}

	private static EnrolmentContext prepareEnrolmentContext(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		
		IStudent student = enrolmentContext.getStudent();
		
		// gets actual student curricular plan 
		IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
		
		
		List studentCurricularPlanCurricularCourses = studentActiveCurricularPlan.getDegreeCurricularPlan().getCurricularCourses(); 
		
		
		final List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);
		
		final List studentEnrolmentsWithStateApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate (){

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getState().equals(new EnrolmentState(EnrolmentState.APROVED));
			}
		});
		
		final List studentDoneCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateApproved,new Transformer(){
				public Object transform(Object obj) {
					IEnrolment enrolment = (IEnrolment) obj;
					return enrolment.getCurricularCourse();
				}});
			

		List enrolmentsWithStateNotApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate(){

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
				return !studentDoneCurricularCourses.contains(curricularCourse) && enrolment.getState().equals(new EnrolmentState(EnrolmentState.NOT_APROVED));
			}
		
		});
		
		List curricularCoursesEnrolled = (List) CollectionUtils.collect(enrolmentsWithStateNotApproved, new Transformer(){
		public Object transform(Object obj) {
			IEnrolment enrolment = (IEnrolment) obj;
			return enrolment.getCurricularCourse();
		}}); 
		
		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(computeCurricularCoursesNotYetDoneByStudent(studentCurricularPlanCurricularCourses, studentDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		return enrolmentContext;
	}

	private static List computeCurricularCoursesFromStudentDegreeCurricularPlan(IStudent student) throws ExcepcaoPersistencia {

		IStudentCurricularPlan studentCurricularPlan = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		List curricularCoursesList = null;

		
		degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
		curricularCoursesList = persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);

		return curricularCoursesList;
	}

	private static List computeAprovedCurricularCoursesFromStudent(IStudent student) throws ExcepcaoPersistencia {

		IStudentCurricularPlan studentCurricularPlan = null;
		List enrolmentsList = null;
		List curricularCoursesList = null;

		studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
		enrolmentsList =
			persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				studentCurricularPlan,
				new EnrolmentState(EnrolmentState.APROVED));

		Iterator iterator = enrolmentsList.iterator();
		curricularCoursesList = new ArrayList();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			curricularCoursesList.add(enrolment.getCurricularCourse());
		}

		return curricularCoursesList;
	}

	private static List computeCurricularCoursesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent) {
		return (List) CollectionUtils.subtract(curricularCoursesFromStudentDegreeCurricularPlan, aprovedCurricularCoursesFromStudent);
	}
	
}