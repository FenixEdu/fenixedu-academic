package middleware.studentMigration;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.middlewareDomain.MWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import middleware.personMigration.PersonUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudentIDNumbers {


	public static void main(String args[]) throws Exception {

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();		
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		
		System.out.println("Reading Students ....");


		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		List result = persistentAluno.readAll();
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		
		System.out.println("Updating " + result.size() + " Student Document Id Numbers ...");


		Iterator iterator = result.iterator();
		while(iterator.hasNext()) {
			MWAluno student = (MWAluno) iterator.next();
		
			UpdateStudentIDNumbers.updateStudentIDNumbers(student);
		}
	}


	/**
	 * 
	 * This method creates new Students 
	 * 
	 */ 
	private static IExecutionPeriod executionPeriod = null;
	
	public static void updateStudentIDNumbers(MWAluno oldStudent) throws Exception {
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();

			sp.iniciarTransaccao();


			// Check if the Person Exists
			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(oldStudent.getDocumentidnumber(), PersonUtils.getDocumentIdType(oldStudent.getMiddlewarePerson().getDocumentidtype()));

			if (person != null) {
				System.out.println("The Person already exists !! Student [" + oldStudent.getNumber() + "] Person ID [" + oldStudent.getDocumentidnumber() + "]");
				sp.confirmarTransaccao();
				return;
			}

		
			IStudent student = sp.getIPersistentStudent().readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
			
			IPessoa personTemp = new Pessoa();
			personTemp.setIdInternal(student.getPerson().getIdInternal());
			
			person = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true); 
			person.setNumeroDocumentoIdentificacao(oldStudent.getDocumentidnumber());
		
		
			sp.confirmarTransaccao();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new Exception(e);
		}
		
		
	}


	private static void createStudentCurricularPlan(ISuportePersistente sp, IStudent student, MWAluno oldStudent) throws ExcepcaoPersistencia, PersistentMiddlewareSupportException {
		
		
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
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWAluno oldStudent, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
	
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();


		// Get the Old Degree
		
		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), new Integer(0));
		
		// Get the Actual Degree Curricular Plan for this Degree

		
		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");

		
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
	private static IBranch getBranch(MWAluno oldStudent, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
		
		
		// Get the old BRanch
		
		sp.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), oldStudent.getBranchcode());
		
		// Get the new one		
		
		branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());


		if (branch == null) {
			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null) {
			System.out.println("Ramo Antigo " + mwbranch);
		}
		
		return branch;
	}


	private static IStudent createStudent(MWAluno oldStudent, ISuportePersistente sp, IPessoa person) throws ExcepcaoPersistencia {
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
