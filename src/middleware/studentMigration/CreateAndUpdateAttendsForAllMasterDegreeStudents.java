package middleware.studentMigration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;
import Util.TipoCurso;

/**
 * @author David Santos
 * 28/Out/2003
 */

public class CreateAndUpdateAttendsForAllMasterDegreeStudents
{
	private static HashMap attendsCreated = new HashMap();
	private static int totalAttendsCreated = 0;
    
	public static void main(String args[])
	{
		IStudent student = null;

		try {
			ISuportePersistente fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = fenixPersistentSuport.getIPersistentStudent();

			fenixPersistentSuport.iniciarTransaccao();
	
			Integer numberOfStudents = persistentStudent.countAll();
	
			fenixPersistentSuport.confirmarTransaccao();
	
			System.out.println("[INFO] Total number of student curriculums to update [" + numberOfStudents + "].");

			int numberOfElementsInSpan = 2000;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++) {

				fenixPersistentSuport.iniciarTransaccao();

				System.gc();

				System.out.println("[INFO] Reading Students...");
				List result = persistentStudent.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
	
				fenixPersistentSuport.confirmarTransaccao();
		
				System.out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
		
				Iterator iterator = result.iterator();
				while (iterator.hasNext()) {
					student = (IStudent) iterator.next();

					if (student.getDegreeType().equals(TipoCurso.MESTRADO_OBJ))
					{
						fenixPersistentSuport.iniciarTransaccao();
						
						CreateAndUpdateAttendsForAllMasterDegreeStudents.createAndUpdateAttends(student, fenixPersistentSuport);
						
						fenixPersistentSuport.confirmarTransaccao();
					}	
				}
			}
		} catch (Throwable e) {
			System.out.println("[ERROR 01] Exception creating and updating Attends for student [" + student.getNumber() + "]!");
			e.printStackTrace(System.out);
		}

		System.out.println("[INFO] DONE!");
		System.out.println("[INFO] Total Attends created: [" + CreateAndUpdateAttendsForAllMasterDegreeStudents.totalAttendsCreated + "].");
	}

	/**
	 * @param student
	 * @param fenixPersistentSuport
	 */
	private static void createAndUpdateAttends(IStudent student, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = fenixPersistentSuport.getIStudentCurricularPlanPersistente();
		IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(student.getNumber(), TipoCurso.MESTRADO_OBJ);

		if (studentCurricularPlan != null)
		{
			List studentEnrolments = studentCurricularPlan.getEnrolments();
			Iterator iterator = studentEnrolments.iterator();
			while (iterator.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterator.next();
				CreateAndUpdateAttendsForAllMasterDegreeStudents.createOrUpdateAttend(student, enrolment, fenixPersistentSuport);
			}
		}	
	}

	/**
	 * @param student
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IFrequenta createOrUpdateAttend(IStudent student, IEnrolment enrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentExecutionCourse persistentExecutionCourse = fenixPersistentSuport.getIDisciplinaExecucaoPersistente();
		IFrequentaPersistente persistentAttend = fenixPersistentSuport.getIFrequentaPersistente();

		ICurricularCourse curricularCourse = enrolment.getCurricularCourseScope().getCurricularCourse();
		IExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();
		
		if (executionPeriod.getState().equals(PeriodState.CURRENT))
		{
			IExecutionCourse executionCourse = persistentExecutionCourse.readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);

			IFrequenta attend = null;
			
			if (executionCourse == null)
			{
				System.out.println("[ERROR 02] Could not find ExecutionCourse for CurricularCourse with code [" + curricularCourse.getCode() + "] and name [" + curricularCourse.getName() + "] for student [" + student.getNumber() + "]!");
			} else
			{
				attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student, executionCourse);

				String key = student.getIdInternal().toString() + executionCourse.getIdInternal().toString();
				if (attend == null)
				{
					attend = (IFrequenta) CreateAndUpdateAttendsForAllMasterDegreeStudents.attendsCreated.get(key);
				}	
				
				if (attend != null)
				{
					persistentAttend.simpleLockWrite(attend);
					attend.setEnrolment(enrolment);
				} else
				{
					attend = new Frequenta();
					persistentAttend.simpleLockWrite(attend);
					attend.setAluno(student);
					attend.setDisciplinaExecucao(executionCourse);
					attend.setEnrolment(enrolment);
					CreateAndUpdateAttendsForAllMasterDegreeStudents.attendsCreated.put(key, attend);
				}
				CreateAndUpdateAttendsForAllMasterDegreeStudents.totalAttendsCreated++;
			}

			if (attend == null)
			{
				System.out.println("[ERROR 03] Couldn't create nor update Attend!");
			}	
			return attend;
		} else
		{
			return null;
		}	
	}
}