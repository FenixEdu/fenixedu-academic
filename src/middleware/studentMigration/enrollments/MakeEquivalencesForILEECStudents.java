package middleware.studentMigration.enrollments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;
import Util.StudentCurricularPlanState;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class MakeEquivalencesForILEECStudents
{
	private static int totalEnrollmentsCreated = 0;
	private static int totalEnrollmentEvaluationsCreated = 0;
    
	public static void main(String args[])
	{
		IStudent student = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();

			System.out.println("[INFO] Reading Students...");

			fenixPersistentSuport.iniciarTransaccao();
			
			List result = MakeEquivalencesForILEECStudents.getListOfStudents(fenixPersistentSuport);
	
			fenixPersistentSuport.confirmarTransaccao();
	
			System.out.println("[INFO] Total number of student curriculums to update [" + result.size() + "].");

			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				student = (IStudent) iterator.next();
				System.out.println("[INFO] Updating student with number [" + student.getNumber() + "]...");
				fenixPersistentSuport.iniciarTransaccao();
				MakeEquivalencesForILEECStudents.makeEquivalences(student, fenixPersistentSuport);
				fenixPersistentSuport.confirmarTransaccao();
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 401] Exception giving equivalences for student [" + student.getNumber() + "] enrolments!");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total Enrolments created: [" + MakeEquivalencesForILEECStudents.totalEnrollmentsCreated + "].");
		System.out.println("[INFO] Total EnrolmentEvaluations created: [" + MakeEquivalencesForILEECStudents.totalEnrollmentEvaluationsCreated + "].");
	}

	/**
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static List getListOfStudents(ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();
		List studentsList = new ArrayList();
		
		IDegreeCurricularPlan degreeCurricularPlan = MakeEquivalencesForILEECStudents.getDegreeCurricularPlan(new Integer(14), fenixPersistentSuport);
		List studentCurricularPlansList = persistentStudentCurricularPlan.readAllByDegreeCurricularPlanAndState(degreeCurricularPlan, StudentCurricularPlanState.ACTIVE_OBJ);
		
		Iterator iterator = studentCurricularPlansList.iterator();
		while(iterator.hasNext())
		{
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
			if(!studentsList.contains(studentCurricularPlan.getStudent()))
			{
				studentsList.add(studentCurricularPlan.getStudent());
			}	
		}

		if(!studentsList.isEmpty())
		{
			return studentsList;
		} else
		{
			return null;
		}
	}

	/**
	 * @param degreeCode
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = fenixPersistentSuport.getIPersistentDegreeCurricularPlan();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		if (mwDegreeTranslation != null) {
			ICurso degree = mwDegreeTranslation.getDegree();
			List result = persistentDegreeCurricularPlan.readByDegreeAndState(degree, DegreeCurricularPlanState.ACTIVE_OBJ);
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) result.get(0);
			return degreeCurricularPlan;
		} else {
			return null;
		}
	}

	/**
	 * @param student
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void makeEquivalences(IStudent student, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlan pastStudentCurricularPlan = MakeEquivalencesForILEECStudents.getStudentCurricularPlan(student, StudentCurricularPlanState.PAST_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 402] Could not obtain past StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		IStudentCurricularPlan currentStudentCurricularPlan = MakeEquivalencesForILEECStudents.getStudentCurricularPlan(student, StudentCurricularPlanState.ACTIVE_OBJ, fenixPersistentSuport);
		if (pastStudentCurricularPlan == null) {
			System.out.println("[ERROR 403] Could not obtain current StudentCurricularPlan for Student with number: [" + student.getNumber() + "]!");
			return;
		}
		
		MakeEquivalencesForILEECStudents.writeAndUpdateEquivalences(student, pastStudentCurricularPlan, currentStudentCurricularPlan, fenixPersistentSuport);
	}

	/**
	 * @param student
	 * @param studentCurricularPlanState
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IStudentCurricularPlan getStudentCurricularPlan(IStudent student, StudentCurricularPlanState studentCurricularPlanState,ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();

		List result = persistentStudentCurricularPlan.readAllByStudentAntState(student, studentCurricularPlanState);
		if ((result != null) && (!result.isEmpty())) {
			return (IStudentCurricularPlan) result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param student
	 * @param pastStudentCurricularPlan
	 * @param currentStudentCurricularPlan
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void writeAndUpdateEquivalences(IStudent student, IStudentCurricularPlan pastStudentCurricularPlan, IStudentCurricularPlan currentStudentCurricularPlan, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWEquivalenciasIleec mwEquivalenciasIleecDAO = mws.getIPersistentMWEquivalenciasIleec();

		List pastEnrolments = pastStudentCurricularPlan.getEnrolments();
		Iterator iterator = pastEnrolments.iterator();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();

			ICurricularCourse curricularCourse = enrolment.getCurricularCourseScope().getCurricularCourse();
		}
	}

}