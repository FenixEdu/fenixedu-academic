package middleware.studentMigration;

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

import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudent {


	public static void main(String args[]) throws Exception {

		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();		
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		
		System.out.println("Reading Students ....");
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		List result = persistentAluno.readAll();
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		System.out.println("Updating " + result.size() + " students ...");

				
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
	
	public static void updateCorrectStudents(MWAluno oldStudent) throws Exception {
		
		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
	
			sp.iniciarTransaccao();
			
			// Read Fenix Student
			IStudent student = persistentStudent.readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
			
			if (student == null) {
				System.out.println("Error Reading Fenix Student! Student Number [" + oldStudent.getNumber() + "]");
				sp.confirmarTransaccao();
				return;
			}
	
			// All the students associated to this person
			
			List studentList = persistentStudent.readbyPerson(student.getPerson());
	
			
			// If the person has more than one student associated then we cannot change his information.
			// Having two student's associated means that the person has a Degree Student and a Master Degree Student
			// We admit that in this case the Master Degree information is the most recent one and therefore we won't change 
			// his information
	
			if (studentList.size() > 1) {
				System.out.println("MWAluno de Mestrado [Person ID" + student.getPerson().getIdInternal() + "]");
				sp.confirmarTransaccao();
				return;
			}
			
	//System.out.println("MWAluno [" + oldStudent.getNumber() + "]");
	
	
	//  System.out.print(".");
	
	
			// Read Active Student Curricular Plan
			IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);
	
	
			// Check if his Degree as changed
			
	
			IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(oldStudent); 
	
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
				
				System.out.println("The Student has changed his degree!!");
				
				
	
			} else {
				System.out.print(".");
				// Update Student Curricular Plan
				
			}		
			
			
			
			
			// Check if the Branch has changed
			
			
			
			
			
			
			// Change all the information
			
			PersonUtils.updateStudentPerson(student.getPerson(), oldStudent.getMiddlewarePerson());
			sp.confirmarTransaccao();

		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}
	

	/**
	 * @param oldStudent
	 * @return the New Degree Curricular Plan
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWAluno oldStudent) throws PersistentMiddlewareSupportException, ExcepcaoPersistencia {
	
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();


		// Get the Old Degree
		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), new Integer(0));
		
		// Get the Actual Degree Curricular Plan for this Degree

		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();

		
		String degreeName = StringUtils.prechomp(mwBranch.getDescription(), "DE ");
		
		
		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYear(degreeName, executionPeriod.getExecutionYear());

		return executionDegree.getCurricularPlan();
	}


}
