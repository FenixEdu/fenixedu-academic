package middleware.studentMigration;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.persistentMiddlewareSupport.IPersistentAluno;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import middleware.personMigration.PersonUtils;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class CreateStudent {


	public static void main(String args[]) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();		
		
		broker.beginTransaction();
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentAluno persistentAluno = mws.getIPersistentAluno();
		
		System.out.println("Reading ....");

		List result = persistentAluno.readAll();

		System.out.println(result.size());

//		MWAluno mw_aluno = persistentMw_Aluno.readByNumber(new Integer(49119));
//
//
//		UpdateStudent.updateCorrectStudents(mw_aluno);
		
		
		broker.commitTransaction();


		Iterator iterator = result.iterator();
		while(iterator.hasNext()) {
			MWAluno student = (MWAluno) iterator.next();
		
			CreateStudent.createStudent(student);
//			break;
		}
		

		
	}


	/**
	 * 
	 * This method creates new Students 
	 * 
	 */ 
	
	public static void createStudent(MWAluno oldStudent) throws ExcepcaoPersistencia {
		
		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();
	
		sp.iniciarTransaccao();

		
		// Check if the person Exists
		// If it does then we will not change his information
		
		IPessoa person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(oldStudent.getDocumentidnumber(), PersonUtils.getDocumentIdType(oldStudent.getMiddlewarePerson().getDocumentidtype()));
		
		if (person == null) {
			person = PersonUtils.createPersonFromStudent(oldStudent);
		}
		
		// Create The Student
		IStudent student = createStudent(oldStudent, sp, person);
		sp.confirmarTransaccao();




		sp.iniciarTransaccao();
		// Create the Student's Curricular Plan
		createStudentCurricularPlan(sp, student);
		sp.confirmarTransaccao();
	}


	private static void createStudentCurricularPlan(ISuportePersistente sp, IStudent student) throws ExcepcaoPersistencia {
		
		// Get the Branch
		
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
//		studentCurricularPlan.setBranch();
		studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
//		studentCurricularPlan.setDegreeCurricularPlan();
		studentCurricularPlan.setGivenCredits(null);
		studentCurricularPlan.setSpecialization(null);
		studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
		studentCurricularPlan.setStudent(student);

		sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
	}


	private static IStudent createStudent(MWAluno oldStudent, ISuportePersistente sp, IPessoa person) throws ExcepcaoPersistencia {
		IStudent student = new Student();
		student.setNumber(oldStudent.getNumber());
		student.setPerson(person);
		student.setState(new StudentState(StudentState.INSCRITO));
		
		IStudentKind studentKind = sp.getIPersistentStudentKind().readByStudentType(new StudentType(StudentType.NORMAL));
		
		student.setStudentKind(studentKind);
		sp.getIPersistentStudent().simpleLockWrite(student);
		return student;
	}


}
