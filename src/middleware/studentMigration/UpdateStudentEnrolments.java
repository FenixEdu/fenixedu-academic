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
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
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
		while(iterator.hasNext()) {
			MWAluno oldStudent = (MWAluno) iterator.next();
			
			try {
				
				sp.iniciarTransaccao();
				sp.clearCache();	
				
				// Read The middleware Enrolments
				oldStudent.setEnrolments(persistentEnrolment.readByStudentNumber(oldStudent.getNumber()));
				
				UpdateStudentEnrolments.updateStudentEnrolment(oldStudent, sp);
				sp.confirmarTransaccao();
				
				System.out.println("Aluno " + oldStudent.getNumber());
				System.out.println("Inscricoes " + oldStudent.getEnrolments().size());
				break;
				
			} catch(Exception e) {
				System.out.println("Error Migrating Student " + oldStudent.getNumber() + " enrolments");
				e.printStackTrace(System.out);
			}
		}
	}


	/**
	 * 
	 * This method updates the student's information as well as his personal information
	 * 
	 */ 
	
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
			
			// Annul the Enrolments
			
			annulEnrolments(enrolments2Annul, sp);
			
			// Find The New Enrolments
			
			List enrolments2Write = getEnrolments2Write(studentEnrolments, oldStudent.getEnrolments(), sp);


System.out.println("Novas inscricoes " + enrolments2Write.size());
			
			
			// Create The New Enrolments
			writeEnrolments(enrolments2Write, studentCurricularPlan, sp);
			
			
		} catch(Exception e) {
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
	private static void writeEnrolments(List enrolments2Write, IStudentCurricularPlan studentCurricularPlan, SuportePersistenteOJB sp) throws Exception {
		Iterator iterator = enrolments2Write.iterator();
		while(iterator.hasNext()) {
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
				System.out.println("Error Reading Curricular Course !! Course Code : " + mwEnrolment.getCoursecode() + " DCP " + degreeCurricularPlan.getIdInternal());
				throw new Exception();
			}
			
			ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(0);
			
			// Get the Curricular Course Scope 
			
			ICurricularYear curricularYear = sp.getIPersistentCurricularYear().readCurricularYearByYear(mwEnrolment.getCurricularcourseyear());
			ICurricularSemester curricularSemester = sp.getIPersistentCurricularSemester().readCurricularSemesterBySemesterAndCurricularYear(mwEnrolment.getCurricularcoursesemester(), curricularYear);

			ICurricularCourseScope curricularCourseScope = sp.getIPersistentCurricularCourseScope().readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(curricularCourse, curricularSemester, branch);

			if (curricularCourseScope == null) {
				System.out.println("Error Reading Curricular Course Scope !!" + mwEnrolment.getCoursecode());
				throw new Exception();
			}			

			// Create the Enrolment
			IEnrolment enrolment = new Enrolment();
//			sp.getIPersistentEnrolment().simpleLockWrite(enrolment);
			enrolment.setCurricularCourseScope(curricularCourseScope);
			enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL_OBJ);
			enrolment.setEnrolmentState(EnrolmentState.ENROLED);
			enrolment.setExecutionPeriod(executionPeriod);
			enrolment.setStudentCurricularPlan(studentCurricularPlan);
			
			
			// Create The Enrolment Evaluation
			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
//			sp.getIPersistentEnrolmentEvaluation().simpleLockWrite(enrolmentEvaluation);
			
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
		
		while(oldEnrolmentIterator.hasNext()) {
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
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if ((enrolment.getCurricularCourseScope().getBranch().equals(branch) &&
				(enrolment.getCurricularCourseScope().getCurricularCourse().getCode().equalsIgnoreCase(StringUtils.trim(mwEnrolment.getCoursecode())) &&
				(enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan)) && 
				(enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().equals(mwEnrolment.getCurricularcourseyear()) &&
				(enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().equals(mwEnrolment.getCurricularcoursesemester())) &&
				(enrolment.getExecutionPeriod().equals(executionPeriod)))))) {
					return true;
				}
		}
		return false;
	}


	/**
	 * @param enrolments2Annul
	 * @param sp
	 */
	private static void annulEnrolments(List enrolments2Annul, SuportePersistenteOJB sp) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * @param studentEnrolments
	 * @param list
	 * @return A list with the Curricular Courses to Annul
	 */
	private static List getEnrolments2Annul(MWAluno oldStudent, List studentEnrolments, List oldEnrolments, SuportePersistenteOJB sp) throws Exception {
		return null;
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



	private static IBranch getBranch(Integer degreeCode, Integer branchCode, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

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
