package middleware.studentMigration.enrollments;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.IMWTreatedEnrollment;
import middleware.middlewareDomain.MWCurricularCourseOutsideStudentDegree;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWEnrolment;
import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWTreatedEnrollment;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWCurricularCourseOutsideStudentDegree;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWTreatedEnrollment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.enrolment.DeleteEnrolment;
import ServidorAplicacao.Servico.enrolment.WriteEnrolment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author David Santos in 28/Out/2003
 */

public class UpdateStudentEnrolments
{
	private static boolean NEW_ENROLMENTS = true;
	private static boolean TO_FILE = true;

	private static HashMap enrollmentsCreated = new HashMap();
	private static HashMap enrollmentEvaluationsCreated = new HashMap();
	private static IExecutionPeriod executionPeriod = null;
	
	private static PrintWriter out = null;
	
	public static void main(String args[])
	{
	
		UpdateStudentEnrolments.NEW_ENROLMENTS = Boolean.valueOf(args[0]).booleanValue();
		UpdateStudentEnrolments.TO_FILE = Boolean.valueOf(args[1]).booleanValue();
		
		MWStudent oldStudent = null;
		ISuportePersistente sp = null;
		try
		{
			out = new PrintWriter(System.out, true);
			if (UpdateStudentEnrolments.TO_FILE)
			{
				String fileName = args[2];
				FileOutputStream file = new FileOutputStream(fileName);
				out = new PrintWriter(file);
			}
			
			IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWAluno persistentMWAluno = mws.getIPersistentMWAluno();
			IPersistentMWEnrolment persistentEnrolment = mws.getIPersistentMWEnrolment();
			sp = SuportePersistenteOJB.getInstance();
	
			sp.iniciarTransaccao();
			executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			Integer numberOfStudents = persistentMWAluno.countAll();
			out.println("[INFO] UpdateStudentEnrolments");
			out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");
			sp.confirmarTransaccao();
	
			int numberOfElementsInSpan = 1;
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
	
			for (int span = 0; span < numberOfSpans; span++)
			{
				sp.iniciarTransaccao();
				sp.clearCache();
				out.println("[INFO] Reading MWStudents...");
				List result = persistentMWAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
				sp.confirmarTransaccao();
	
				out.println("[INFO] Updating [" + result.size() + "] student curriculums...");
	
				Iterator iterator = result.iterator();
				while (iterator.hasNext())
				{
					oldStudent = (MWStudent) iterator.next();
					sp.iniciarTransaccao();
					oldStudent.setEnrolments(persistentEnrolment.readByStudentNumber(oldStudent.getNumber()));
					UpdateStudentEnrolments.updateStudentEnrolment(oldStudent, sp);
					sp.confirmarTransaccao();
					UpdateStudentEnrolments.enrollmentsCreated.clear();
					UpdateStudentEnrolments.enrollmentEvaluationsCreated.clear();
				}
			}
		} catch (Throwable e)
		{
			out.println("[ERROR 10] Exception migrating student [" + oldStudent.getNumber() + "] enrolments!");
			out.println("[ERROR 10] Number: [" + oldStudent.getNumber() + "]");
			out.println("[ERROR 10] Degree: [" + oldStudent.getDegreecode() + "]");
			out.println("[ERROR 10] Branch: [" + oldStudent.getBranchcode() + "]");
			e.printStackTrace(out);
		}
	
		ReportEnrolment.report(out);
		
		out.close();
		
		UpdateStudentEnrolments.reset();
	}

	/**
	 * @param oldStudent
	 * @param sp
	 * @throws Throwable
	 */
	private static void updateStudentEnrolment(MWStudent oldStudent, ISuportePersistente sp) throws Throwable
	{
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();

		// Read Fenix Student.
		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			out.println("[ERROR 01] Cannot find Fenix student with number [" + oldStudent.getNumber() + "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			out.println("[ERROR 02] Cannot find Fenix StudentCurricularPlan for student number [" + oldStudent.getNumber() + "]!");
			return;
		}

//		List studentEnrolments = sp.getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
		List studentEnrolments = oldStudent.getEnrolments();

		UpdateStudentEnrolments.writeEnrolments(studentEnrolments, studentCurricularPlan, oldStudent, sp);
	}

	/**
	 * @param enrolments2Write
	 * @param studentCurricularPlan
	 * @param oldStudent
	 * @param sp
	 * @throws Throwable
	 */
	private static void writeEnrolments(List enrolments2Write, IStudentCurricularPlan studentCurricularPlan, MWStudent oldStudent, ISuportePersistente sp) throws Throwable
	{
		Iterator iterator = enrolments2Write.iterator();
		while (iterator.hasNext())
		{
			final MWEnrolment mwEnrolment = (MWEnrolment) iterator.next();

			// Get the Fenix DegreeCurricularPlan of the Student.
			IDegreeCurricularPlan degreeCurricularPlan = UpdateStudentEnrolments.getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan, sp);

			if (degreeCurricularPlan == null)
			{
				out.println("[ERROR 03.1] Degree Curricular Plan Not Found!");
				continue;
			}

			ICurricularCourse curricularCourse = UpdateStudentEnrolments.getCurricularCourse(mwEnrolment, degreeCurricularPlan, sp, true);

			if (curricularCourse == null)
			{
				continue;
			}

			if (!UpdateStudentEnrolments.hasExecutionInGivenPeriod(curricularCourse, executionPeriod, sp))
			{
				ReportEnrolment.addExecutionCourseNotFound(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
				continue;
			}

			IEnrolment enrolment = UpdateStudentEnrolments.createEnrolment(mwEnrolment, studentCurricularPlan, sp, curricularCourse);

			// Update the corresponding Fenix Attend if it exists.
			IFrequenta attend = UpdateStudentEnrolments.updateAttend(curricularCourse, enrolment, mwEnrolment, sp);
			if (attend == null)
			{
				// NOTE [DAVID]: This kind of report is only pesented the first time the migration
				// process is executed.
				// This happens because although this is a situation of error report, this kind of error
				// doesn't
				// forbid the enrolment to be created in the Fenix DB. Thus the second time the process
				// is executed, the
				// enrolment for this particular CurricularCourse will have already been created in the
				// DB so this
				// CurricularCourse is no longer considered for this execution of the process.
				// This is for information only.
				ReportEnrolment.addAttendNotFound(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
				UpdateStudentEnrolments.createAttend(curricularCourse, enrolment, mwEnrolment, sp);
			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param enrolment
	 * @param mwEnrolment
	 * @param sp
	 * @return
	 * @throws Throwable
	 */
	private static IFrequenta updateAttend(ICurricularCourse curricularCourse, IEnrolment enrolment, MWEnrolment mwEnrolment, ISuportePersistente sp) throws Throwable
	{
		IExecutionCourse executionCourse = sp.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
		IFrequenta attend = null;
		if (executionCourse == null)
		{
			// NOTE [DAVID]: This should not happen at this point.
			ReportEnrolment.addExecutionCourseNotFound(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
		} else
		{
			IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(mwEnrolment.getNumber(), TipoCurso.LICENCIATURA_OBJ);
			attend = sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if (attend != null)
			{
				sp.getIFrequentaPersistente().simpleLockWrite(attend);
				attend.setEnrolment(enrolment);
			}

		}
		return attend;
	}

	/**
	 * @param studentCurricularPlan
	 * @param sp
	 * @param curricularCourse
	 * @return
	 * @throws Throwable
	 */
	private static IEnrolment createEnrolment(MWEnrolment mwEnrolment, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente sp, ICurricularCourse curricularCourse) throws Throwable
	{
		IEnrolment enrolment = UpdateStudentEnrolments.createAndUpdateEnrolment(mwEnrolment, studentCurricularPlan, curricularCourse, sp);
		UpdateStudentEnrolments.createAndUpdateEnrolmentEvaluation(mwEnrolment, enrolment, sp);
		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @param studentCurricularPlan
	 * @param curricularCourse
	 * @param fenixPersistentSuport
	 * @return
	 * @throws Throwable
	 */
	private static IEnrolment createAndUpdateEnrolment(MWEnrolment mwEnrolment, IStudentCurricularPlan studentCurricularPlan, ICurricularCourse curricularCourse, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolment persistentEnrolment = fenixPersistentSuport.getIPersistentEnrolment();

		IEnrolment enrolment = persistentEnrolment.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(studentCurricularPlan, curricularCourse, executionPeriod);
		
		if (enrolment == null) {

			IEnrolment enrolmentToObtainKey = new Enrolment();
			enrolmentToObtainKey.setStudentCurricularPlan(studentCurricularPlan);
			enrolmentToObtainKey.setCurricularCourse(curricularCourse);
			enrolmentToObtainKey.setExecutionPeriod(executionPeriod);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentKey(enrolmentToObtainKey);

			enrolment = (IEnrolment) UpdateStudentEnrolments.enrollmentsCreated.get(key);

			if (enrolment == null) {
				enrolment = new Enrolment();

				fenixPersistentSuport.getIPersistentEnrolment().simpleLockWrite(enrolment);

				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				
				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
				
				EnrolmentState enrolmentState = null;
				if ( (mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")) )
				{
					enrolmentState = EnrolmentState.ENROLED;
				} else
				{
					enrolmentState = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentStateByGrade(mwEnrolment);
				}
				enrolment.setEnrolmentState(enrolmentState);

				UpdateStudentEnrolments.enrollmentsCreated.put(key, enrolment);
				ReportEnrolment.addEnrolmentMigrated();
			}
		}

		return enrolment;
	}

	/**
	 * @param mwEnrolment
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void createAndUpdateEnrolmentEvaluation(MWEnrolment mwEnrolment, IEnrolment enrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = fenixPersistentSuport.getIPersistentEnrolmentEvaluation();

		if ( (mwEnrolment.getGrade() == null) || (mwEnrolment.getGrade().equals("")) )
		{
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

			enrolmentEvaluation.setCheckSum(null);
			enrolmentEvaluation.setEmployee(null);
			enrolmentEvaluation.setEnrolment(enrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolmentEvaluation.setExamDate(null);
			enrolmentEvaluation.setGrade(null);
			enrolmentEvaluation.setGradeAvailableDate(null);
			enrolmentEvaluation.setObservation(null);
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
			enrolmentEvaluation.setWhen(null);

			UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);
			
		} else
		{
			Date whenAltered = UpdateStudentEnrolments.getWhenAlteredDate(mwEnrolment);

			String grade = CreateAndUpdateAllStudentsPastEnrolments.getAcurateGrade(mwEnrolment);

			IEnrolmentEvaluation enrolmentEvaluation = persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(enrolment, EnrolmentEvaluationType.NORMAL_OBJ, grade);
			
			IEnrolmentEvaluation enrolmentEvaluationToObtainKey = new EnrolmentEvaluation();
			enrolmentEvaluationToObtainKey.setEnrolment(enrolment);
			enrolmentEvaluationToObtainKey.setGrade(grade);
			enrolmentEvaluationToObtainKey.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolmentEvaluationToObtainKey.setWhen(whenAltered);
			String key = CreateAndUpdateAllStudentsPastEnrolments.getEnrollmentEvaluationKey(enrolmentEvaluationToObtainKey);

			if (enrolmentEvaluation == null) {
				
				enrolmentEvaluation = (IEnrolmentEvaluation) UpdateStudentEnrolments.enrollmentEvaluationsCreated.get(key);

				if (enrolmentEvaluation == null) {
					enrolmentEvaluation = new EnrolmentEvaluation();

					fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

					enrolmentEvaluation.setEnrolment(enrolment);
					enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
					enrolmentEvaluation.setGrade(grade);

					UpdateStudentEnrolments.updateEnrollmentEvaluation(mwEnrolment, enrolment, fenixPersistentSuport, whenAltered, enrolmentEvaluation);

					UpdateStudentEnrolments.enrollmentEvaluationsCreated.put(key, enrolmentEvaluation);

					CreateAndUpdateAllStudentsPastEnrolments.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);
					ReportEnrolment.addEnrolmentEvaluationMigrated();

					UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);

				} else {
					if(UpdateStudentEnrolments.NEW_ENROLMENTS)
					{
						fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
						UpdateStudentEnrolments.updateEnrollmentEvaluation(mwEnrolment, enrolment, fenixPersistentSuport, whenAltered, enrolmentEvaluation);
						CreateAndUpdateAllStudentsPastEnrolments.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

						UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);
						
					}
				}
			} else {
				if(UpdateStudentEnrolments.NEW_ENROLMENTS)
				{
					fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
					UpdateStudentEnrolments.updateEnrollmentEvaluation(mwEnrolment, enrolment, fenixPersistentSuport, whenAltered, enrolmentEvaluation);
					CreateAndUpdateAllStudentsPastEnrolments.updateEnrollmentStateAndEvaluationType(enrolment, enrolmentEvaluation);

					UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);
					
				} else
				{
					if(enrolment.getEvaluations().size() == 1)
					{
						fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
						fenixPersistentSuport.getIPersistentEnrolmentEvaluation().deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
						ReportEnrolment.addEnrolmentEvaluationDeleted();
						fenixPersistentSuport.getIPersistentEnrolment().simpleLockWrite(enrolment);
						fenixPersistentSuport.getIPersistentEnrolment().deleteByOID(Enrolment.class, enrolment.getIdInternal());
						UpdateStudentEnrolments.cleanEnrollmentRelations(enrolment, fenixPersistentSuport);
						ReportEnrolment.addEnrolmentDeleted();

						UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);
						
					} else if(enrolment.getEvaluations().size() > 1)
					{
						fenixPersistentSuport.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
						fenixPersistentSuport.getIPersistentEnrolmentEvaluation().deleteByOID(EnrolmentEvaluation.class, enrolmentEvaluation.getIdInternal());
						ReportEnrolment.addEnrolmentEvaluationDeleted();

						UpdateStudentEnrolments.writeTreatedMWEnrollment(mwEnrolment);
						
					}
				}
			}
		}
	}

	/**
	 * @param mwEnrolment
	 * @return
	 */
	private static Date getWhenAlteredDate(MWEnrolment mwEnrolment)
	{
		Date whenAltered = null;
		if (mwEnrolment.getExamdate() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(mwEnrolment.getEnrolmentyear().intValue(), 9, 1);
			whenAltered = new Date(calendar.getTimeInMillis());
		} else {
			whenAltered = mwEnrolment.getExamdate();
		}
		long dateInLongFormat = whenAltered.getTime();
		dateInLongFormat = dateInLongFormat + (mwEnrolment.getIdinternal().longValue() * 1000);
		Date newDate = new Date(dateInLongFormat);
		return newDate;
	}

	/**
	 * @param mwEnrolment
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @param newDate
	 * @param enrolmentEvaluation
	 * @throws Throwable
	 */
	private static void updateEnrollmentEvaluation(MWEnrolment mwEnrolment, IEnrolment enrolment, ISuportePersistente fenixPersistentSuport, Date newDate, IEnrolmentEvaluation enrolmentEvaluation) throws Throwable
	{
		if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED))
		{
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
		} else
		{
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
		}
		enrolmentEvaluation.setExamDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setObservation(mwEnrolment.getRemarks());
		enrolmentEvaluation.setPersonResponsibleForGrade(CreateAndUpdateAllStudentsPastEnrolments.getPersonResponsibleForGrade(mwEnrolment, fenixPersistentSuport));
		enrolmentEvaluation.setGradeAvailableDate(mwEnrolment.getExamdate());
		enrolmentEvaluation.setWhen(newDate);
		enrolmentEvaluation.setEmployee(CreateAndUpdateAllStudentsPastEnrolments.getEmployee(mwEnrolment, fenixPersistentSuport));
		enrolmentEvaluation.setCheckSum(null);
	}

	/**
	 * @param enrolment
	 * @param fenixPersistentSuport
	 * @throws Throwable
	 */
	private static void cleanEnrollmentRelations(IEnrolment enrolment, ISuportePersistente fenixPersistentSuport) throws Throwable
	{
		DeleteEnrolment.deleteAttend(enrolment);
//		IExecutionCourse executionCourse = fenixPersistentSuport.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(enrolment.getCurricularCourse(), executionPeriod);
//		if (executionCourse == null)
//		{
//			return;
//		}
//
//		IFrequenta attend = fenixPersistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(enrolment.getStudentCurricularPlan().getStudent(), executionCourse);
//
//		if (attend != null)
//		{
//			fenixPersistentSuport.getIFrequentaPersistente().simpleLockWrite(attend);
//			attend.setEnrolment(null);
//		}
	}

	/**
	 * @param mwEnrolment
	 * @param sp
	 * @param degreeCurricularPlan
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourse getCurricularCourseFromAnotherDegree(final MWEnrolment mwEnrolment, ISuportePersistente sp, IDegreeCurricularPlan degreeCurricularPlan) throws Throwable
	{
		ICurricularCourse curricularCourse = null;

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWCurricularCourseOutsideStudentDegree persistentMWCurricularCourseOutsideStudentDegree = mws.getIPersistentMWCurricularCourseOutsideStudentDegree();

		// First of all we look in the MWCurricularCourseOutsideStudentDegree table to see if there is
		// already a
		// correspondence between this CurricularCourse and this Degree.
		MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegree =
			persistentMWCurricularCourseOutsideStudentDegree.readByCourseCodeAndDegreeCode(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode());
		if (mwCurricularCourseOutsideStudentDegree != null)
		{
			curricularCourse = mwCurricularCourseOutsideStudentDegree.getCurricularCourse();

			// If there is no correspondence yet, let us look if this CurricularCourse has only one
			// ExecuitonCourse in the given period.
		} else if (UpdateStudentEnrolments.curricularCourseHasOnlyOneExecutionInGivenPeriod(executionPeriod, mwEnrolment, sp, degreeCurricularPlan))
		{

			// If there is only one ExecutionCourse for all the existing CurricularCourses then we can
			// choose
			// any CurricularCourse but this choice is registred in table
			// MWCurricularCourseOutsideStudentDegree
			// to keep coherence of choice and to make the next similar choice quicker.
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			List curricularCourses =
				persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
					StringUtils.trim(mwEnrolment.getCoursecode()),
					degreeCurricularPlan.getDegree().getTipoCurso(),
					degreeCurricularPlan.getState());
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);

			MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite = new MWCurricularCourseOutsideStudentDegree();
			persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
			mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
			mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

		} else
		{

			IFrequentaPersistente attendDAO = sp.getIFrequentaPersistente();
			List attendList = attendDAO.readByStudentNumberInCurrentExecutionPeriod(mwEnrolment.getNumber());
			List attendsWithCurricularCourseCode = (List) CollectionUtils.select(attendList, new Predicate()
			{
				public boolean evaluate(Object input)
				{
					IFrequenta attend = (IFrequenta) input;

					String courseCode = mwEnrolment.getCoursecode();

					List associatedCurricularCourses = attend.getDisciplinaExecucao().getAssociatedCurricularCourses();
					Iterator iterator = associatedCurricularCourses.iterator();
					while (iterator.hasNext())
					{
						ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
						if (curricularCourse.getCode().equals(courseCode))
						{
							return true;
						}
					}
					return false;
				}
			});

			if (attendsWithCurricularCourseCode.size() > 0)
			{
				List associatedCurricularCourses = ((IFrequenta) attendsWithCurricularCourseCode.get(0)).getDisciplinaExecucao().getAssociatedCurricularCourses();

				curricularCourse = (ICurricularCourse) associatedCurricularCourses.get(0);

				MWCurricularCourseOutsideStudentDegree mwCurricularCourseOutsideStudentDegreeToWrite = new MWCurricularCourseOutsideStudentDegree();
				persistentMWCurricularCourseOutsideStudentDegree.simpleLockWrite(mwCurricularCourseOutsideStudentDegreeToWrite);
				mwCurricularCourseOutsideStudentDegreeToWrite.setCourseCode(mwEnrolment.getCoursecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setDegreeCode(mwEnrolment.getDegreecode());
				mwCurricularCourseOutsideStudentDegreeToWrite.setCurricularCourse(curricularCourse);

			} else
			{
				ReportEnrolment.addFoundCurricularCourseInOtherDegrees(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
			}
		}

		return curricularCourse;
	}

	/**
	 * @param curricularCourses
	 * @return
	 */
	private static boolean hasDiferentDegrees(List curricularCourses)
	{
		int numberOfDegrees = CollectionUtils.getCardinalityMap(curricularCourses).size();
		return (numberOfDegrees > 1);
	}

	/**

	 * @param degreeCode
	 * @param studentCurricularPlan
	 * @param sp
	 * @throws Throwable
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente sp) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);

		ICursoExecucao executionDegree =
			sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(mwDegreeTranslation.getDegree().getNome(), executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);

		if (executionDegree == null)
		{
			out.println("[ERROR 06] the degree has no execution in [" + executionPeriod.getExecutionYear().getYear() + "]!");
			return null;
		} else
		{
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(executionDegree.getCurricularPlan()))
			{
				out.println("[INFO] the student [" + studentCurricularPlan.getStudent().getNumber() + "] has changed his degree!");
				return studentCurricularPlan.getDegreeCurricularPlan();
			} else
			{
				return executionDegree.getCurricularPlan();
			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param enrolment
	 * @param mwEnrolment
	 * @param sp
	 * @throws Throwable
	 */
	private static void createAttend(ICurricularCourse curricularCourse, IEnrolment enrolment, MWEnrolment mwEnrolment, ISuportePersistente sp) throws Throwable
	{
		IExecutionCourse executionCourse = sp.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);

		if (executionCourse == null)
		{
			// NOTE [DAVID]: This error report can be added here even if it was added before in the
			// updateAttend() method
			// because this addition wont repeat same occurrences. This should not happen at this point.
			ReportEnrolment.addExecutionCourseNotFound(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
		} else
		{
			IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(mwEnrolment.getNumber(), TipoCurso.LICENCIATURA_OBJ);
//			IFrequenta attend = new Frequenta();
//			sp.getIFrequentaPersistente().simpleLockWrite(attend);
//			attend.setAluno(student);
//			attend.setDisciplinaExecucao(executionCourse);
//			attend.setEnrolment(enrolment);
			WriteEnrolment.createAttend(student, curricularCourse, executionPeriod, enrolment);
			// NOTE [DAVID]: This is for information only.
			ReportEnrolment.addCreatedAttend(mwEnrolment.getCoursecode(), mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @param mwEnrolment
	 * @param sp
	 * @return
	 * @throws Throwable
	 */
	private static boolean hasExecutionInGivenPeriod(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod, ISuportePersistente sp) throws Throwable
	{
		IExecutionCourse executionCourse = sp.getIPersistentExecutionCourse().readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
		if (executionCourse == null)
		{
			return false;
		} else
		{
			return true;
		}
	}

	/**
	 * @param executionPeriod
	 * @param mwEnrolment
	 * @param sp
	 * @param degreeCurricularPlan
	 * @return
	 * @throws Throwable
	 */
	private static boolean curricularCourseHasOnlyOneExecutionInGivenPeriod(IExecutionPeriod executionPeriod, MWEnrolment mwEnrolment, ISuportePersistente sp, IDegreeCurricularPlan degreeCurricularPlan) throws Throwable
	{
		IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
		List curricularCourses =
			persistentCurricularCourse.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
				StringUtils.trim(mwEnrolment.getCoursecode()),
				degreeCurricularPlan.getDegree().getTipoCurso(),
				degreeCurricularPlan.getState());
		List executionCourses = new ArrayList();

		Iterator iterator1 = curricularCourses.iterator();
		while (iterator1.hasNext())
		{
			ICurricularCourse curricularCourse = (ICurricularCourse) iterator1.next();
			List associatedExecutionCourses = curricularCourse.getAssociatedExecutionCourses();
			Iterator iterator2 = associatedExecutionCourses.iterator();
			while (iterator2.hasNext())
			{
				IExecutionCourse executionCourse = (IExecutionCourse) iterator2.next();
				if (executionCourse.getExecutionPeriod().equals(executionPeriod))
				{
					if (!executionCourses.contains(executionCourse))
					{
						executionCourses.add(executionCourse);
					}
				}
			}
		}

		if (executionCourses.size() == 1)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param sp
	 * @param solveSomeProblems
	 * @return
	 * @throws Throwable
	 */
	private static ICurricularCourse getCurricularCourse(MWEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp, boolean solveSomeProblems) throws Throwable
	{
		String courseCode = null;
		if (solveSomeProblems)
		{
			courseCode = getRealCurricularCourseCodeForCodesAZx(mwEnrolment);
		} else
		{
			courseCode = mwEnrolment.getCoursecode();
		}

		// Get the list of Fenix CurricularCourses with that code for the selected DegreeCurricularPlan.
		List curricularCourses = sp.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(courseCode), degreeCurricularPlan);

		ICurricularCourse curricularCourse = null;

		// Ideally we find only one CurricularCourse but we never know, we may find more or even less.
		if (curricularCourses.size() != 1)
		{
			if (curricularCourses.size() > 1)
			{
				out.println("[ERROR 05] Several Fenix CurricularCourses with code [" + courseCode + "] were found for Degree [" + mwEnrolment.getDegreecode() + "]!");
				return null;
			} else // size == 0
				{
				// We did not find any CurricularCourse with that code in that DegreeCurricularPlan.
				// Now we try to read by the CurricularCourse code only.
				IPersistentCurricularCourse curricularCourseDAO = sp.getIPersistentCurricularCourse();
				curricularCourses =
					curricularCourseDAO.readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(
						StringUtils.trim(courseCode),
						degreeCurricularPlan.getDegree().getTipoCurso(),
						degreeCurricularPlan.getState());

				if (curricularCourses.size() == 1)
				{
					curricularCourse = (ICurricularCourse) curricularCourses.get(0);
				} else if (curricularCourses.size() > 1)
				{
					if (hasDiferentDegrees(curricularCourses))
					{
						curricularCourse = getCurricularCourseFromAnotherDegree(mwEnrolment, sp, degreeCurricularPlan);
						if (curricularCourse == null)
						{
							// NOTE [DAVID]: This is for information only.
							ReportEnrolment.addReplicatedCurricularCourses(courseCode, curricularCourses);
							return null;
						}
					} else
					{
						curricularCourse = (ICurricularCourse) curricularCourses.get(0);
					}
				} else
				{
					ReportEnrolment.addCurricularCourseNotFound(courseCode, mwEnrolment.getDegreecode().toString(), mwEnrolment.getNumber().toString());
					return null;
				}
			}
		} else // curricularCourses.size() == 1
			{
			curricularCourse = (ICurricularCourse) curricularCourses.get(0);
		}
		return curricularCourse;
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// ------------------------------- METHODS TO SOLVE SPECIFIC PROBLEMS
	// ----------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param mwEnrolment
	 */
	private static String getRealCurricularCourseCodeForCodesAZx(MWEnrolment mwEnrolment)
	{
		if (mwEnrolment.getCoursecode().equals("AZ2") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "QN";
		} else if (mwEnrolment.getCoursecode().equals("AZ3") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "PY";
		} else if (mwEnrolment.getCoursecode().equals("AZ4") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "P5";
		} else if (mwEnrolment.getCoursecode().equals("AZ5") && mwEnrolment.getCurricularcoursesemester().intValue() == 2)
		{
			return "UN";
		} else if (mwEnrolment.getCoursecode().equals("AZ6") && mwEnrolment.getCurricularcoursesemester().intValue() == 1)
		{
			return "U8";
		} else
		{
			return mwEnrolment.getCoursecode();
		}
	}




	/**
	 * @param mwEnrolment
	 * @throws ExcepcaoPersistencia
	 */
	protected static void writeTreatedMWEnrollment(MWEnrolment mwEnrolment) throws ExcepcaoPersistencia
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWTreatedEnrollment mwTreatedEnrollmentDAO = mws.getIPersistentMWTreatedEnrollment();
		IMWTreatedEnrollment mwTreatedEnrollment = new MWTreatedEnrollment();
		mwTreatedEnrollmentDAO.simpleLockWrite(mwTreatedEnrollment);
		mwTreatedEnrollment.setBranchcode(mwEnrolment.getBranchcode());
		mwTreatedEnrollment.setCoursecode(mwEnrolment.getCoursecode());
		mwTreatedEnrollment.setCurricularcoursesemester(mwEnrolment.getCurricularcoursesemester());
		mwTreatedEnrollment.setCurricularcourseyear(mwEnrolment.getCurricularcourseyear());
		mwTreatedEnrollment.setDegreecode(mwEnrolment.getDegreecode());
		mwTreatedEnrollment.setEnrolmentyear(mwEnrolment.getEnrolmentyear());
		mwTreatedEnrollment.setExamdate(mwEnrolment.getExamdate());
		mwTreatedEnrollment.setGrade(mwEnrolment.getGrade());
		mwTreatedEnrollment.setIdinternal(mwEnrolment.getIdinternal());
		mwTreatedEnrollment.setNumber(mwEnrolment.getNumber());
		mwTreatedEnrollment.setRemarks(mwEnrolment.getRemarks());
		mwTreatedEnrollment.setSeason(mwEnrolment.getSeason());
		mwTreatedEnrollment.setTeachernumber(mwEnrolment.getTeachernumber());
		mwTreatedEnrollment.setUniversitycode(mwEnrolment.getUniversitycode());
	}

	private static void reset()
	{
		NEW_ENROLMENTS = true;
		TO_FILE = true;
		enrollmentsCreated.clear();
		enrollmentEvaluationsCreated.clear();
		executionPeriod = null;
		out = null;
	}

}