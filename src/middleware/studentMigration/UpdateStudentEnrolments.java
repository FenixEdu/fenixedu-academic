package middleware.studentMigration;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MwEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWEnrolment;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
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
		executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();

		List result = persistentAluno.readAll();
		sp.confirmarTransaccao();
		System.out.println("Updating " + result.size() + " student Curriculums ...");

		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			MWAluno oldStudent = (MWAluno) iterator.next();

			try {

				sp.iniciarTransaccao();
//				sp.clearCache();
				// Read The middleware Enrolments
				oldStudent.setEnrolments(persistentEnrolment.readByStudentNumber(oldStudent.getNumber()));
				UpdateStudentEnrolments.updateStudentEnrolment(oldStudent, sp);
				sp.confirmarTransaccao();
			} catch (Exception e) {
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
		
		ReportEnrolment.report(new PrintWriter(System.out));
	}

	public static void updateStudentEnrolment(MWAluno oldStudent, SuportePersistenteOJB sp) throws Exception {

		try {


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
			List enrolments2Write = getEnrolments2Write(studentEnrolments, oldStudent.getEnrolments(), studentCurricularPlan, sp);


			// Annul the Enrolments
			annulEnrolments(enrolments2Annul, sp);

			// Create The New Enrolments
			writeEnrolments(enrolments2Write, studentCurricularPlan, oldStudent, sp);

		} catch (Exception e) {
			System.out.println("Error Migrating Student " + oldStudent.getNumber() + " enrolments");
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
			boolean sameDegree = true;
			MwEnrolment mwEnrolment = (MwEnrolment) iterator.next();

			// Get the Degree Of the Student
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan, sp);
			

			if (degreeCurricularPlan == null) {
				System.out.println("Error ! Degree Curricular Plan Not Found !");
				throw new Exception();
			}

			// Get The Branch (This could be the student branch or the curricular course branch
			IBranch branch = getBranch(mwEnrolment.getDegreecode(), mwEnrolment.getBranchcode(), degreeCurricularPlan, sp);

			if (branch == null) {
				System.out.println("Error ! Branch Not Found !");
				throw new Exception();
			}

			// Get the Curricular Course 
			List curricularCourses = sp.getIPersistentCurricularCourse().readbyCourseCodeAndDegreeCurricularPlan(StringUtils.trim(mwEnrolment.getCoursecode()), degreeCurricularPlan);
			ICurricularCourse curricularCourse = null;
			
			
			if (curricularCourses.size() != 1) {
				
				sameDegree = false;
				
				// if the result size is greater than 1 then check if the Branch Code match in Any
				if (curricularCourses.size() > 1) {
					System.out.println("Several Curricular Courses with Code found " + mwEnrolment.getCoursecode() + " found for Degree " + mwEnrolment.getDegreecode());
					
					curricularCoursesNotFound++;
					enrolmentNotWritten++;

					return;
				} 

				// Try to read by CourseCode Only (this will assume that all the Degree Curricular Plan name ends with "2003/2004"
				if (curricularCourses.size() == 0) {
					curricularCourses = sp.getIPersistentCurricularCourse().readbyCourseCode(StringUtils.trim(mwEnrolment.getCoursecode()));
					if (curricularCourses.size() == 1) {
						curricularCourse = (ICurricularCourse) curricularCourses.get(0);
					} else if (curricularCourses.size() > 1){
						if (hasDiferentDegrees(curricularCourses)) {
							ReportEnrolment.addNotFoundCurricularCourse(mwEnrolment.getCoursecode(), String.valueOf(mwEnrolment.getDegreecode().intValue()), new ArrayList());
//							System.out.println("Curricular Courses found outside degree curricular plan [" + mwEnrolment.getCoursecode() + "] : " + curricularCourses.size());
//							System.out.println("Enrolment Degree [" + mwEnrolment.getDegreecode() + "]");
							curricularCoursesNotFound++;
							enrolmentNotWritten++;

							return;
							
						} else {
							curricularCourse = (ICurricularCourse) curricularCourses.get(0);
						}
						
					} else {
						System.out.println("No Curricular Courses Found in others degrees. Curricular Course [" + mwEnrolment.getCoursecode() + "] Enrolment Degree [" + mwEnrolment.getDegreecode() + "]");
						curricularCoursesNotFound++;
						enrolmentNotWritten++;

						return;
					}
				} 
			} else {
				curricularCourse = (ICurricularCourse) curricularCourses.get(0);
			}
							
			// Get the Curricular Course Scope 

			List curricularCourseScopes = sp.getIPersistentCurricularCourseScope().readByCurricularCourseAndYearAndSemester(curricularCourse, mwEnrolment.getCurricularcourseyear(), mwEnrolment.getCurricularcoursesemester());

			ICurricularCourseScope curricularCourseScope = null;

			if ((curricularCourseScopes == null) || (curricularCourseScopes.isEmpty())) {
			
				// Try to read by Course and Year only
				
				curricularCourseScopes = sp.getIPersistentCurricularCourseScope().readByCurricularCourseAndYear(curricularCourse, mwEnrolment.getCurricularcourseyear());

				if ((curricularCourseScopes == null) || (curricularCourseScopes.isEmpty())) {
					
					
					// Se a disciplina da inscricao nao e do curso do aluno e nao conseguimos encontrar: 
					//  - por ano e semestre
					//  - por ano
					// Entao vamods tentar ler so por semestre que aparece na inscricao e se exisir apenas 1
					// e a que procuramos 
					if (!sameDegree) {
						curricularCourseScopes = sp.getIPersistentCurricularCourseScope().readByCurricularCourseAndSemester(curricularCourse, mwEnrolment.getCurricularcoursesemester());
					}

					if ((curricularCourseScopes != null) && (curricularCourseScopes.size() == 1)) {
						curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
					} else {
						System.out.println("Error Reading Curricular Course Scope !! No Scopes found for this Curricular Course and Year and then by Course and Semester.");
						System.out.println("Numero do Aluno " + mwEnrolment.getNumber());
						System.out.println("Codigo Curso " + mwEnrolment.getDegreecode());
						System.out.println("Codigo Disciplina " + mwEnrolment.getCoursecode());
						System.out.println("Codigo Ramo " + mwEnrolment.getBranchcode());
						System.out.println("Ano Curricular " + mwEnrolment.getCurricularcourseyear());
						System.out.println("Semestre Disciplina " + mwEnrolment.getCurricularcoursesemester());
						System.out.println("Curricular Course ID " + curricularCourse.getIdInternal());
						System.out.println("---------");
	
						curricularCourseScopesNotFound++;
						enrolmentNotWritten++;
	
						return;
					}
				} else {
					
//					System.out.println("Scopes " + curricularCourseScopes.size());
					
					// See if a Scope exists for this branch
					curricularCourseScope = findScopeForBranch(curricularCourseScopes, branch);
					
					if (curricularCourseScope == null) {
						curricularCourseScope = findScopeForBranch(curricularCourseScopes, studentCurricularPlan.getBranch());
					}
					
					// If there's no Scope, try to find the "empty" scope
					if (curricularCourseScope == null) {
						curricularCourseScope = findScopeForBranch(curricularCourseScopes, null);
					} 
					
					// if we can't find a scope and the degree of the course is diferent from the student's, them we ignore the branch
					if ((curricularCourseScope == null) && (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(curricularCourse.getDegreeCurricularPlan().getDegree()))) {
						curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
					}
					
					// If still we cannot find a scope ...
					
					if (curricularCourseScope == null) {
						System.out.println("Error Reading Curricular Course Scope after finding scopes by Curricular Course and Year!! ");
						System.out.println("Numero do Aluno " + mwEnrolment.getNumber());
						System.out.println("Codigo Curso " + mwEnrolment.getDegreecode());
						System.out.println("Codigo Disciplina " + mwEnrolment.getCoursecode());
						System.out.println("Codigo Ramo " + mwEnrolment.getBranchcode());
						System.out.println("Ano Curricular " + mwEnrolment.getCurricularcourseyear());
						System.out.println("Semestre Disciplina " + mwEnrolment.getCurricularcoursesemester());
						System.out.println("Curricular Course ID " + curricularCourse.getIdInternal());
						
						System.out.println("---------");

						curricularCourseScopesNotFound++;
						enrolmentNotWritten++;

						return;
					}
					
				}

			
			} else {
				
				if (curricularCourseScopes.size() == 1) {
					curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
				}

				
				// There is a list of Scopes for This Curricular Course, year and semester
				// See if a Scope exists for this branch
				if (curricularCourseScope == null) {
					curricularCourseScope = findScopeForBranch(curricularCourseScopes, branch);
				}
					
				if (curricularCourseScope == null) {
					curricularCourseScope = findScopeForBranch(curricularCourseScopes, studentCurricularPlan.getBranch());
				}
					
				// If there's no Scope, try to find the "empty" scope
				if (curricularCourseScope == null) {
					curricularCourseScope = findScopeForBranch(curricularCourseScopes, null);
				}

				// if we can't find a scope and the degree of the course is diferent from the student's, them we ignore the branch
				if ((curricularCourseScope == null) && (!studentCurricularPlan.getDegreeCurricularPlan().getDegree().equals(curricularCourse.getDegreeCurricularPlan().getDegree()))) {
					curricularCourseScope = (ICurricularCourseScope) curricularCourseScopes.get(0);
				}
					
				
				// If still we cannot find a scope ...
					
				if (curricularCourseScope == null) {
					System.out.println("Error Reading Curricular Course Scope after finding scopes by Curricular Course, Year and Semester!! " );
					System.out.println("Numero do Aluno " + mwEnrolment.getNumber());
					System.out.println("Codigo Curso " + mwEnrolment.getDegreecode());
					System.out.println("Codigo Disciplina " + mwEnrolment.getCoursecode());
					System.out.println("Codigo Ramo " + mwEnrolment.getBranchcode());
					System.out.println("Ano Curricular " + mwEnrolment.getCurricularcourseyear());
					System.out.println("Semestre Disciplina " + mwEnrolment.getCurricularcoursesemester());
					System.out.println("Curricular Course ID " + curricularCourse.getIdInternal());
					System.out.println("---------");

					curricularCourseScopesNotFound++;
					enrolmentNotWritten++;

					return;
				}
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
//				System.out.println("Student " + mwEnrolment.getNumber() + " has no Attend for " + mwEnrolment.getCoursecode() + " from Degree " + mwEnrolment.getDegreecode());
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
	 * @param curricularCourses
	 * @return
	 */
	private static boolean hasDiferentDegrees(List curricularCourses) {
		
		int numberOfDegrees = CollectionUtils.getCardinalityMap(curricularCourses).size();
		
		return (numberOfDegrees > 1);
		
	}



	/**
	 * @param curricularCourseScopes
	 * @param branch
	 * @return
	 */
	private static ICurricularCourseScope findScopeForBranch(List curricularCourseScopes, IBranch branch) {
		Iterator iterator = curricularCourseScopes.iterator();
		while(iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();

			
			if (branch == null) {
				if (
					((curricularCourseScope.getBranch().getCode().equals("")) && (curricularCourseScope.getBranch().getName().equals("")))
					||
					(curricularCourseScope.getBranch().getName().startsWith("CURSO DE"))) {
						return curricularCourseScope;
				}
			} else {
				if (curricularCourseScope.getBranch().equals(branch)) {
						return curricularCourseScope;
				}
			}
		}
		return null;
	}

	/**
	 * @param studentEnrolments
	 * @param list
	 * @return
	 */
	private static List getEnrolments2Write(List studentEnrolments, List oldEnrolments, IStudentCurricularPlan studentCurricularPlan, SuportePersistenteOJB sp) throws Exception {
		List result = new ArrayList();

		Iterator oldEnrolmentIterator = oldEnrolments.iterator();

		while (oldEnrolmentIterator.hasNext()) {
			MwEnrolment mwEnrolment = (MwEnrolment) oldEnrolmentIterator.next();

			// Get the Degree Of the Curricular Course
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(mwEnrolment.getDegreecode(), studentCurricularPlan, sp);

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
			if (
//				(enrolment.getCurricularCourseScope().getBranch().equals(branch)
				 (enrolment.getCurricularCourseScope().getCurricularCourse().getCode().equalsIgnoreCase(StringUtils.trim(mwEnrolment.getCoursecode()))
//				&& (enrolment.getCurricularCourseScope().getCurricularCourse().getDegreeCurricularPlan().equals(degreeCurricularPlan))
//				&& (enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear().equals(mwEnrolment.getCurricularcourseyear())
//				&& (enrolment.getCurricularCourseScope().getCurricularSemester().getSemester().equals(mwEnrolment.getCurricularcoursesemester()))
				&& (enrolment.getExecutionPeriod().equals(executionPeriod)))) {
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
//				&& (mwEnrolment.getCurricularcoursesemester().equals(enrolment.getExecutionPeriod().getSemester()))
//				&& (enrolment.getExecutionPeriod().getExecutionYear().getYear().startsWith(mwEnrolment.getEnrolmentyear().toString()))) {
				) {
				return true;
			}

		}
		return false;
	}

	/**
	 * @param oldStudent
	 * @return the New Degree Curricular Plan
	 */
//	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
//
//		IDegreeCurricularPlan degreeCurricularPlan = null;
//		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
//		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
//
//		// Get the Old Degree
//
//		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(degreeCode, new Integer(0));
//
//		// Get the Actual Degree Curricular Plan for this Degree
//
//		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");
//
//		if (degreeName.indexOf("TAGUS") != -1) {
//			degreeName = "Engenharia Informática e de Computadores - Taguspark";
//		}
//
//		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);
//
//		return executionDegree.getCurricularPlan();
//	}


	/**
	 * 
	 * @param degreeCode
	 * @param studentCurricularPlan
	 * @param sp
	 * @return
	 * @throws PersistentMiddlewareSupportException
	 * @throws ExcepcaoPersistencia
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(Integer degreeCode, IStudentCurricularPlan studentCurricularPlan, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(degreeCode);
		List degreeCurricularPlans = sp.getIPersistentDegreeCurricularPlan().readByDegree(mwDegreeTranslation.getDegree());
		
		Iterator iterator = degreeCurricularPlans.iterator();
		while(iterator.hasNext()) {
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
			if (degreeCurricularPlan.equals(studentCurricularPlan.getDegreeCurricularPlan())) {
				
				// Check for Execution
				
				
				if (sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeCurricularPlan.getDegree().getNome(), executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ) == null) {
					System.out.println("[ERROR] The Degree Has no Execution in " + executionPeriod.getExecutionYear().getYear());
					return null;
				} else {
					return degreeCurricularPlan;
				}
 			} 
		}
		
		System.out.println("[ERROR] The Student Has Changed his Degree ! " + studentCurricularPlan.getStudent().getNumber());
		return null;
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
