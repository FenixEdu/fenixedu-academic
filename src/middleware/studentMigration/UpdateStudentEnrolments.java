package middleware.studentMigration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MwEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.commons.lang.StringUtils;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudentEnrolments {

	private static IExecutionPeriod executionPeriod = null;
	private static int enrolmentNotWritten = 0;
	private static int enrolmentWritten = 0;
	private static int curricularCoursesNotFound = 0;
	private static int curricularCourseScopesNotFound = 0;
	private static int executionCoursesNotFound = 0;
	private static int attendsNotFound = 0;
	private static int attendsUpdated = 0;
	



	public static void main(String args[]) throws Exception {

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		IPersistentMWEnrolment persistentEnrolment = mws.getIPersistentMWEnrolment();
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

		System.out.println("Reading Students ....");
		sp.iniciarTransaccao();
		List result = persistentAluno.readAll();
		sp.confirmarTransaccao();
		System.out.println("Updating " + result.size() + " student Curriculums ...");

		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			MWAluno oldStudent = (MWAluno) iterator.next();

			try {

				sp.iniciarTransaccao();
				sp.clearCache();
				// Read The middleware Enrolments
				oldStudent.setEnrolments(persistentEnrolment.readByStudentNumber(oldStudent.getNumber()));

				UpdateStudentEnrolments.updateStudentEnrolment(oldStudent, sp);
				sp.confirmarTransaccao();
			} catch (Exception e) {
				System.out.println("Error Migrating Student " + oldStudent.getNumber() + " enrolments");
				e.printStackTrace(System.out);

				throw new Exception();

			}
		}
		
		
		System.out.println("Done !");
		System.out.println("Curricular Courses Not Found : " + curricularCoursesNotFound);
		System.out.println("Curricular Course Scopes Not Found : " + curricularCourseScopesNotFound);
		System.out.println("Enrolments Not Written : " + enrolmentNotWritten);
		System.out.println("Enrolments Written : " + enrolmentWritten);
		System.out.println("Execution Courses Not Found : " + executionCoursesNotFound);
		System.out.println("Attends Not Found : " + attendsNotFound);
		System.out.println("Attends Updated : " + attendsUpdated);
		

	}

	public static void updateStudentEnrolment(MWAluno oldStudent, SuportePersistenteOJB sp) throws Exception {

		try {

			executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();

			IPersistentStudent persistentStudent = sp.getIPersistentStudent();

			// Read Fenix Student
			IStudent student = persistentStudent.readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

			if (student == null) {
				System.out.println("Error Reading Fenix Student! Student Number [" + oldStudent.getNumber() + "]");
				return;
			}

			IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveByStudentNumberAndDegreeType(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);

			if (studentCurricularPlan == null) {
				System.out.println("Error Reading Student Curricular Plan! Student Number [" + oldStudent.getNumber() + "]");
				return;
			}

			List studentEnrolments = sp.getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);

			// Find the Enrolments That No Longer Exist
			List enrolments2Annul = getEnrolments2Annul(oldStudent, studentEnrolments, oldStudent.getEnrolments(), sp);

			// Find The New Enrolments

			List enrolments2Write = getEnrolments2Write(studentEnrolments, oldStudent.getEnrolments(), sp);


			// Annul the Enrolments

			annulEnrolments(enrolments2Annul, sp);

			// Create The New Enrolments
			writeEnrolments(enrolments2Write, studentCurricularPlan, oldStudent, sp);

		} catch (Exception e) {
			System.out.println("Aluno " + oldStudent.getNumber());
			System.out.println("Degree " + oldStudent.getDegreecode());
			System.out.println("Branch " + oldStudent.getBranchcode());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	/**
	 * @param enrolments2Write
	 * @param sp
	 */
	private static void writeEnrolments(List enrolments2Write, IStudentCurricularPlan studentCurricularPlan, MWAluno oldStudent, SuportePersistenteOJB sp) throws Exception {
		Iterator iterator = enrolments2Write.iterator();
		while (iterator.hasNext()) {
			MwEnrolment mwEnrolment = (MwEnrolment) iterator.next();

			// Get the Degree Of the Curricular Course
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(mwEnrolment.getDegreecode(), sp);

			if (degreeCurricularPlan == null) {
				System.out.println("Error ! Degree Curricular Plan Not Found !");
				throw new Exception();
			}

			// Get The Branch for This Curricular Course
			IBranch branch = getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, sp);

			if (branch == null) {
				System.out.println("Error ! Branch Not Found !");
				throw new Exception();
			}

			// Get the Curricular Course 

			List curricularCourses = sp.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(mwEnrolment.getCoursecode()), degreeCurricularPlan);

			if (curricularCourses.size() != 1) {
				//				System.out.println("Error Reading Curricular Course !! Course Code : " + mwEnrolment.getCoursecode() + " DCP " + degreeCurricularPlan.getIdInternal());
				System.out.println("Erro ao ler a disciplina !! Codigo Disciplina : " + mwEnrolment.getCoursecode() + " Codigo Curso " + mwEnrolment.getDegreecode());
				
				curricularCoursesNotFound++;
				enrolmentNotWritten++;
				
				break;
				//				throw new Exception();
			}

			ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(0);

			// Get the Curricular Course Scope 

			List curricularCourseScopes = sp.getIPersistentCurricularCourseScope().readByCurricularCourseAndSemesterAndBranch(curricularCourse, mwEnrolment.getCurricularcoursesemester(), branch);

			if (curricularCourseScopes == null) {
				enrolmentNotWritten++;
				System.out.println("Error Reading Curricular Course Scope !!" + mwEnrolment.getCoursecode() + " Student Number " + oldStudent.getNumber());
				System.out.println("[CC " + curricularCourse.getIdInternal() + " Semester " + mwEnrolment.getCurricularcoursesemester() + " B " + branch.getIdInternal() + "]");
				System.out.println("Ano " + mwEnrolment.getCurricularcourseyear());
				System.out.println("Semestre " + mwEnrolment.getCurricularcoursesemester());
				return;
			}
			
			// If the size is greater than 1 find by year
			ICurricularCourseScope curricularCourseScope = null;

			if (curricularCourseScopes.size() == 1) {
				curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
			} else if ((curricularCourseScopes.size() > 1) || (curricularCourseScopes.size() == 0)){
				ICurricularYear curricularYear = sp.getIPersistentCurricularYear().readCurricularYearByYear(mwEnrolment.getCurricularcourseyear());
				ICurricularSemester curricularSemester =
					sp.getIPersistentCurricularSemester().readCurricularSemesterBySemesterAndCurricularYear(mwEnrolment.getCurricularcoursesemester(), curricularYear);
				curricularCourseScope =
					sp.getIPersistentCurricularCourseScope().readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);
				if (curricularCourseScope == null) {
//					System.out.println("-------------");
//					System.out.println("Ano Curricular " + curricularYear.getYear());
//					System.out.println("Semestre Curricular " + curricularSemester.getSemester());
//					System.out.println("Disciplina Curricular  " + curricularCourse.getCode() + " [" + curricularCourse.getIdInternal() + "]");
//					System.out.println("Ramo " + branch.getIdInternal());
				}
			}

			if (curricularCourseScope == null) {
//				System.out.println("Error Reading Curricular Course Scope Second Try !!" + mwEnrolment.getCoursecode() + " Student Number " + oldStudent.getNumber());
//				System.out.println("[CC " + curricularCourse.getIdInternal() + " Semester " + mwEnrolment.getCurricularcoursesemester() + " B " + branch.getIdInternal() + "]");
//				System.out.println("Ano " + mwEnrolment.getCurricularcourseyear());
//				System.out.println("Semestre " + mwEnrolment.getCurricularcoursesemester());

				curricularCourseScopesNotFound++;
				enrolmentNotWritten++;

				System.out.println("---------");
				System.out.println("Numero do Aluno " + mwEnrolment.getNumber());
				System.out.println("Codigo Curso " + mwEnrolment.getDegreecode());
				System.out.println("Codigo Disciplina " + mwEnrolment.getCoursecode());
				System.out.println("Codigo Ramo " + mwEnrolment.getBranchcode());
				System.out.println("Ano Curricular " + mwEnrolment.getCurricularcourseyear());
				System.out.println("Semestre Disciplina " + mwEnrolment.getCurricularcoursesemester());
				


				return;
				//				throw new Exception();
			}

			// Create the Enrolment
			IEnrolment enrolment = new Enrolment();
			sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
			enrolment.setCurricularCourseScope(curricularCourseScope);
			enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolment.setEnrolmentState(EnrolmentState.ENROLED);
			enrolment.setExecutionPeriod(executionPeriod);
			enrolment.setStudentCurricularPlan(studentCurricularPlan);

			// Create The Enrolment Evaluation
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);

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

			enrolmentWritten++;
			
			
			
			// Update the Corresponding Attend if it exists
			
			IDisciplinaExecucao executionCourse = sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
			if (executionCourse == null) {
				System.out.println("No Execution Found for Curricular Course " + mwEnrolment.getCoursecode());
				executionCoursesNotFound++;
				return;
			} 			
			IStudent student = sp.getIPersistentStudent().readByNumero(mwEnrolment.getNumber(), TipoCurso.LICENCIATURA_OBJ);

			IFrequenta attend = sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);

			if(attend == null){
				System.out.println("Student " + mwEnrolment.getNumber() + " has no Attend for " + mwEnrolment.getCoursecode() + " from Degree " + mwEnrolment.getDegreecode());
				attendsNotFound++;
				return;
			} else {
				sp.getIFrequentaPersistente().simpleLockWrite(attend);
				attend.setEnrolment(enrolment);
				attendsUpdated++;
			}	
		}
	}

	/**
	 * @param studentEnrolments
	 * @param list
	 * @return
	 */
	private static List getEnrolments2Write(List studentEnrolments, List oldEnrolments, SuportePersistenteOJB sp) throws Exception {
		List result = new ArrayList();

		Iterator oldEnrolmentIterator = oldEnrolments.iterator();

		while (oldEnrolmentIterator.hasNext()) {
			MwEnrolment mwEnrolment = (MwEnrolment) oldEnrolmentIterator.next();

			// Get the Degree Of the Curricular Course
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(mwEnrolment.getDegreecode(), sp);

			if (degreeCurricularPlan == null) {
				System.out.println("Error ! Degree Curricular Plan Not Found !");
				throw new Exception();
			}

			// Get The Branch for This Curricular Course
			IBranch branch = getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, sp);

			if (branch == null) {
				System.out.println("Error ! Branch Not Found !");
				throw new Exception();
			}

			// Check if The Enrolment exists
			if (!enrolmentExistsOnFenix(mwEnrolment, degreeCurricularPlan, branch, studentEnrolments, sp)) {
				result.add(mwEnrolment);
			}

		}
		return result;
	}

	/**
	 * @param mwEnrolment
	 * @param degreeCurricularPlan
	 * @param branch
	 * @param studentEnrolments
	 * @param sp
	 * @return
	 */
	private static boolean enrolmentExistsOnFenix(MwEnrolment mwEnrolment, IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, List studentEnrolments, SuportePersistenteOJB sp) {
		
		Iterator iterator = studentEnrolments.iterator();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if ((enrolment.getCurricularCourseScope().getBranch().equals(branch)
				&& (enrolment.getCurricularCourseScope().getCurricularCourse().getCode().equalsIgnoreCase(StringUtils.trim(mwEnrolment.getCoursecode()))
				&& (enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan))
				&& (enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear().equals(mwEnrolment.getCurricularcourseyear())
				&& (enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().equals(mwEnrolment.getCurricularcoursesemester()))
				&& (enrolment.getExecutionPeriod().equals(executionPeriod)))))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param enrolments2Annul
	 * @param sp
	 */
	private static void annulEnrolments(List enrolments2Annul, SuportePersistenteOJB sp) throws ExcepcaoPersistencia {

		Iterator iterator = enrolments2Annul.iterator();
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			
			// Find the Attend
			IDisciplinaExecucao executionCourse = sp.getIDisciplinaExecucaoPersistente().readbyCurricularCourseAndExecutionPeriod(enrolment.getCurricularCourseScope().getCurricularCourse(), executionPeriod);
			if (executionCourse == null) {
				continue;
			} 			
			IFrequenta attend = sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(enrolment.getStudentCurricularPlan().getStudent(), executionCourse);

			if(attend != null){
				sp.getIFrequentaPersistente().simpleLockWrite(attend);
				attend.setEnrolment(null);
			}	
			
			// Delete EnrolmentEvalutaion

			Iterator evaluations = enrolment.getEvaluations().iterator();
			while(evaluations.hasNext()) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) evaluations.next();
				sp.getIPersistentEnrolmentEvaluation().delete(enrolmentEvaluation);
			}
			
			// Delete Enrolment
			sp.getIPersistentEnrolment().delete(enrolment);
			
		}


	}

	/**
	 * @param studentEnrolments
	 * @param list
	 * @return A list with the Curricular Courses to Annul
	 */
	private static List getEnrolments2Annul(MWAluno oldStudent, List studentEnrolments, List oldEnrolments, SuportePersistenteOJB sp) throws Exception {
		List result = new ArrayList();

		Iterator fenixEnrolments = studentEnrolments.iterator();

		while (fenixEnrolments.hasNext()) {
			IEnrolment enrolment = (IEnrolment) fenixEnrolments.next();

			// Check if The Enrolment exists on the old System
			if (!enrolmentExistsOnAlmeidaServer(enrolment, oldEnrolments, sp)) {
				result.add(enrolment);
			}
		}

		return result;
	}

	/**
	 * @param enrolment
	 * @param oldEnrolments
	 * @param sp
	 * @return
	 */
	private static boolean enrolmentExistsOnAlmeidaServer(IEnrolment enrolment, List oldEnrolments, SuportePersistenteOJB sp) {
		Iterator iterator = oldEnrolments.iterator();
		while (iterator.hasNext()) {
			MwEnrolment mwEnrolment = (MwEnrolment) iterator.next();

			// To read an mw_Enrolment we need the student number, the Course Code, the Semester and the enrolment year

			if ((mwEnrolment.getNumber().equals(enrolment.getStudentCurricularPlan().getStudent().getNumber()))
				&& (StringUtils.trim(mwEnrolment.getCoursecode()).equals(enrolment.getCurricularCourseScope().getCurricularCourse().getCode()))
				&& (mwEnrolment.getCurricularcoursesemester().equals(enrolment.getExecutionPeriod().getSemester()))
				&& (enrolment.getExecutionPeriod().getExecutionYear().getYear().startsWith(mwEnrolment.getEnrolmentyear().toString()))) {
				return true;
			}

		}
		return false;
	}

	/**
	 * @param oldStudent
	 * @return the New Degree Curricular Plan
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

		// Get the Old Degree

		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(degreeCode, new Integer(0));

		// Get the Actual Degree Curricular Plan for this Degree

		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");

		if (degreeName.indexOf("TAGUS") != -1) {
			degreeName = "Engenharia Informática e de Computadores - Taguspark";
		}

		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);

		return executionDegree.getCurricularPlan();
	}

	private static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		IBranch branch = null;

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

		// Get the old BRanch

		sp.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(degreeCode, branchCode);

		// Get the new one		

		if (mwbranch == null) {
			System.out.println("Curso " + degreeCode);
			System.out.println("Ramo " + branchCode);
		}

		branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());

		if (branch == null) {
			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null) {
			System.out.println("DCP " + degreeCurricularPlan.getName());
			System.out.println("Ramo Inexistente " + mwbranch);
		}

		return branch;
	}

}
