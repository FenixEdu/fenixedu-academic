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
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class CreateStudent {


	public static void main(String args[]) throws Exception {

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		
		System.out.println("Reading new Students ....");


		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		sp.iniciarTransaccao();
		List result = persistentAluno.readAll();

		sp.confirmarTransaccao();
		
		System.out.println("Creating " + result.size() + " new Students ...");


		Iterator iterator = result.iterator();
		while(iterator.hasNext()) {
			MWStudent student = (MWStudent) iterator.next();
		
			try {
				sp.iniciarTransaccao();
				sp.clearCache();
				CreateStudent.createStudent(student, sp);
				sp.confirmarTransaccao();
			} catch(Exception e) {
				System.out.println("Error Migrating Student " + student.getNumber());
				e.printStackTrace(System.out);
			}
		}
	}


	/**
	 * 
	 * This method creates new Students 
	 * 
	 */ 
	private static IExecutionPeriod executionPeriod = null;
	
	public static void createStudent(MWStudent oldStudent, SuportePersistenteOJB sp) throws Exception {
		
		try {
//			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
		
			// Check if the person Exists
			// If it does then we will not change his information
		
			executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			
			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(oldStudent.getDocumentidnumber(), PersonUtils.getDocumentIdType(oldStudent.getMiddlewarePerson().getDocumentidtype()));
		
			if (person == null) {
				person = PersonUtils.createPersonFromStudent(oldStudent, sp);
			}

			// Create The Student
			IStudent student = createStudent(oldStudent, sp, person);

			// Create the Student's Curricular Plan

			createStudentCurricularPlan(sp, student, oldStudent);

		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new Exception(e);
		}
		
		
	}


	private static void createStudentCurricularPlan(ISuportePersistente sp, IStudent student, MWStudent oldStudent) throws Exception {
		
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		
		sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);

		// Get the Degree Curricular Plan
		studentCurricularPlan.setDegreeCurricularPlan(getDegreeCurricularPlan(oldStudent, sp));



		// Get the Branch
		studentCurricularPlan.setBranch(getBranch(oldStudent, studentCurricularPlan.getDegreeCurricularPlan(), sp));
		
		if (studentCurricularPlan.getBranch() == null) {
			System.out.println("Error : Branch [Degree:" + oldStudent.getDegreecode() + " Branch:" + oldStudent.getBranchcode() + "] not found !");

			return;			
		}		 
		
		
		studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
		studentCurricularPlan.setGivenCredits(null);
		studentCurricularPlan.setSpecialization(null);
		studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
		studentCurricularPlan.setStudent(student);
	}


	/**
	 * @param oldStudent
	 * @return The Degree Curricular Plan
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWStudent oldStudent, ISuportePersistente sp) throws Exception {
	
//		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();


		// Get the Old Degree
		
		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), new Integer(0));

		if (mwBranch == null) {
			System.out.println("Error reading Branch " + oldStudent.getBranchcode() + " for degree " + oldStudent.getDegreecode());
			throw new Exception();
		}
		
		// Get the Actual Degree Curricular Plan for this Degree

		
		String degreeName = StringUtils.substringAfter(mwBranch.getDescription(), "DE ");
//		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");

		
		if (degreeName.indexOf("TAGUS") != -1) {
			degreeName = "Engenharia Informática e de Computadores - Taguspark";
		}


		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);

		return executionDegree.getCurricularPlan();
	}


	/**
	 * @param oldStudent
	 * @return The Student's Branch
	 */
	private static IBranch getBranch(MWStudent oldStudent, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
		
		
		// Get the old BRanch
		
		sp.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), oldStudent.getBranchcode());
		
		// Get the new one		
		
		branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());

//		if(mwbranch.getDescription().startsWith("CURSO DE ")) {
//			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
//		} else {
//			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());
//		}

		if (branch == null) {
			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null) {
			System.out.println("Ramo Antigo " + mwbranch);
		}
		
		return branch;
	}


	private static IStudent createStudent(MWStudent oldStudent, ISuportePersistente sp, IPessoa person) throws ExcepcaoPersistencia {
		IStudent student = new Student();
		sp.getIPersistentStudent().simpleLockWrite(student);

		student.setNumber(oldStudent.getNumber());
		student.setPerson(person);
		student.setState(new StudentState(StudentState.INSCRITO));
		student.setDegreeType(TipoCurso.LICENCIATURA_OBJ);

		IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(new StudentType(StudentType.NORMAL));
		student.setStudentKind(studentKind);
		return student;
	}
}
