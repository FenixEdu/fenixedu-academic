package middleware.studentMigration;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWStudent;
import middleware.middlewareDomain.MWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import middleware.personMigration.PersonUtils;

import org.apache.commons.lang.StringUtils;

import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudent {

	private static IExecutionPeriod executionPeriod = null;
	
	public static void main(String args[]) throws Exception {

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		
//		System.out.println("Reading Students ....");
//		sp.iniciarTransaccao();
//		List result = persistentAluno.readAll();
//		sp.confirmarTransaccao();
//		System.out.println("Updating " + result.size() + " students ...");
		sp.iniciarTransaccao();
		Integer numberOfStudents = persistentAluno.countAll();
		sp.confirmarTransaccao();
		int numberOfElementsInSpan = 1;
		
		int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
		numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
		
		for (int span = 0; span < numberOfSpans; span++) {
			sp.iniciarTransaccao();
			sp.clearCache();	
			System.out.println("Reading Students...");
			List result = persistentAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
			System.out.println("Updating " + result.size() + " students ...");
			sp.confirmarTransaccao();		
			Iterator iterator = result.iterator();
			while(iterator.hasNext()) {
				MWStudent student = (MWStudent) iterator.next();
				try {
					sp.iniciarTransaccao();
					UpdateStudent.updateCorrectStudents(student, sp);
					sp.confirmarTransaccao();
				} catch(Exception e) {
					System.out.println("Error Migrating Student " + student.getNumber());
					e.printStackTrace(System.out);
				}
			}

		}
	}


	/**
	 * 
	 * This method updates the student's information as well as his personal information
	 * 
	 */ 
	
	public static void updateCorrectStudents(MWStudent oldStudent, SuportePersistenteOJB sp) throws Exception {
		
		try {
			
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
	
			
			executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			
			// Read Fenix Student
			IStudent student = persistentStudent.readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
			
			if (student == null) {
				System.out.println("Error Reading Fenix Student! Student Number [" + oldStudent.getNumber() + "]");
				sp.confirmarTransaccao();
				return;
			}
	
			// All the students associated to this person
			
			List studentList = persistentStudent.readbyPerson(student.getPerson());
	
			

			// Check if his Degree as changed
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(oldStudent, sp); 

			// Read The Branch		
			IBranch branch = getBranch(oldStudent, degreeCurricularPlan, sp);


			// Read Active Student Curricular Plan
			IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);
				
			
			if (studentCurricularPlan == null) {
				System.out.println("No Active Student Curricular Plan !!" + student.getNumber());
				createNewStudentCurricularPlan(student, degreeCurricularPlan, branch, sp);
				
			} else {
			
				sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);	
		
		
				if (!studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
					System.out.print("The Student [" + oldStudent.getNumber() + "] has changed his degree!!");
					System.out.println(" [" + studentCurricularPlan.getDegreeCurricularPlan().getName() + " -> " + degreeCurricularPlan.getName() + "]");
					
					studentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE_OBJ);
	
					studentCurricularPlan.setBranch(branch);
									
					createNewStudentCurricularPlan(student, degreeCurricularPlan, branch, sp);
	
				} else {
					// Update Student Curricular Plan
					studentCurricularPlan.setBranch(branch);
				}		
			}
			

			// If the person has one master degree curricular plan associated then we cannot change his information.
			// This means that the person has a Degree Student and a Master Degree Student
			// We admit that in this case the Master Degree information is the most recent one and therefore we won't change 
			// his information

	
			if (hasMasterDegree(studentList, sp)) {
				System.out.println("Master Degree Student Found [Person ID " + student.getPerson().getIdInternal() + "]");
			} else {
				// Change all the information
				PersonUtils.updateStudentPerson(student.getPerson(), oldStudent.getMiddlewarePerson());
			}
			
			
		} catch(Exception e) {
			System.out.println("Aluno " + oldStudent.getNumber());
			System.out.println("Degree " + oldStudent.getDegreecode());
			System.out.println("Branch " + oldStudent.getBranchcode());
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}
	

	/**
	 * @param student
	 * @param degreeCurricularPlan
	 * @param branch
	 * @param sp
	 */
	private static void createNewStudentCurricularPlan(IStudent student, IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, ISuportePersistente sp) throws ExcepcaoPersistencia {
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
	
		studentCurricularPlan.setBranch(branch);
		studentCurricularPlan.setClassification(new Double(0));
		studentCurricularPlan.setCompletedCourses(new Integer(0));
		studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
		studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
		studentCurricularPlan.setEnrolledCourses(new Integer(0));
		studentCurricularPlan.setEnrolments(null);
		studentCurricularPlan.setGivenCredits(new Double(0));
		studentCurricularPlan.setSpecialization(null);
		studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
		studentCurricularPlan.setStudent(student);
	}


	/**
	 * @param studentList
	 * @return boolean 
	 */
	private static boolean hasMasterDegree(List studentList, ISuportePersistente sp) throws ExcepcaoPersistencia {
		Iterator iterator = studentList.iterator();
		while(iterator.hasNext()) {
			IStudent student = (IStudent) iterator.next();
			
			List studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente().readAllFromStudent(student.getNumber().intValue());

			if (((IStudentCurricularPlan) studentCurricularPlanList.get(0)).getDegreeCurricularPlan().getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ)){
				return true;
			}
		}
		return false;
	}


	/**
	 * @param oldStudent
	 * @return the New Degree Curricular Plan
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWStudent oldStudent, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
	
//		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();


		// Get the Old Degree
		
		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), new Integer(0));
		
		// Get the Actual Degree Curricular Plan for this Degree

		
		String degreeName = StringUtils.substringAfter(mwBranch.getDescription(), "DE ");
//		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");

		
		if (degreeName.indexOf("TAGUS") != -1) {
			degreeName = "Engenharia Informática e de Computadores - Taguspark";
		}

		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);

		return executionDegree.getCurricularPlan();
	}



	private static IBranch getBranch(MWStudent oldStudent, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
		
		
		// Get the old BRanch
		
		sp.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), oldStudent.getBranchcode());
		
		// Get the new one		
		
		
		if (mwbranch == null) {
			System.out.println("Aluno " + oldStudent.getNumber());
			System.out.println("Curso " + oldStudent.getDegreecode());
			System.out.println("Ramo " + oldStudent.getBranchcode());
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
