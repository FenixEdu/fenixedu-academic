package middleware.studentMigration;

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

import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudent {


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
		
			UpdateStudent.updateCorrectStudents(student);
//			break;
		}
		

		
	}


	/**
	 * 
	 * This method updates the student's information as well as his personal information
	 * 
	 */ 
	
	public static void updateCorrectStudents(MWAluno oldStudent) throws ExcepcaoPersistencia {
		
		
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();
	
		sp.iniciarTransaccao();
		
		// Read Fenix Student
		IStudent student = persistentStudent.readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
		
		if (student == null) {
//			System.out.println("Error Reading Fenix Student! Student Number [" + oldStudent.getNumber() + "]");
			return;
		}

		// All the students associated to this person
		
		List studentList = persistentStudent.readbyPerson(student.getPerson());

		
		// If the person has more than one student associated then we cannot change his information.
		// Having two student's associated means that the person has a Degree Student and a Master Degree Student
		// We admit that in this case the Master Degree information is the most recent one and therefore we won't change 
		// his information

		if (studentList.size() > 1) {

//System.out.println("MWAluno de Mestrado [Person ID" + student.getPerson().getIdInternal() + "]");

			return;
		}
		
//System.out.println("MWAluno [" + oldStudent.getNumber() + "]");


//  System.out.print(".");

		// Check if his Degree as changed
		
		
		
		// Check if the Branch has changed
		
		
		
		
		
		
		// Change all the information
		
		PersonUtils.updateStudentPerson(student.getPerson(), oldStudent.getMiddlewarePerson());
		sp.confirmarTransaccao();
	}


}
