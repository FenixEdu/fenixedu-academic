package middleware.studentMigration;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.personMigration.PersonUtils;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Pessoa;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudentIDNumbers {

	private static IExecutionPeriod executionPeriod = null;
	

	public static void main(String args[]) throws Exception {

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		
		System.out.println("Reading Students ....");


		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		
		sp.iniciarTransaccao();
		List result = persistentAluno.readAll();
		sp.confirmarTransaccao();
		
		System.out.println("Updating " + result.size() + " Student Document Id Numbers ...");


		Iterator iterator = result.iterator();
		while(iterator.hasNext()) {
			MWAluno student = (MWAluno) iterator.next();
		
			sp.iniciarTransaccao();
			sp.clearCache();
			UpdateStudentIDNumbers.updateStudentIDNumbers(student, sp);
			sp.confirmarTransaccao();
		}
	}


	/**
	 * 
	 * This method creates new Students 
	 * 
	 */ 
	public static void updateStudentIDNumbers(MWAluno oldStudent, SuportePersistenteOJB sp) throws Exception {
		
		try {
			
			// Check if the Person Exists
			IPessoa person = sp.getIPessoaPersistente().lerPessoaPorNumDocIdETipoDocId(oldStudent.getDocumentidnumber(), PersonUtils.getDocumentIdType(oldStudent.getMiddlewarePerson().getDocumentidtype()));

			if (person != null) {
				System.out.println("The Person already exists !! Student [" + oldStudent.getNumber() + "] Person ID [" + oldStudent.getDocumentidnumber() + "]");
				return;
			}

		
			IStudent student = sp.getIPersistentStudent().readByNumero(oldStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
			
			IPessoa personTemp = new Pessoa();
			personTemp.setIdInternal(student.getPerson().getIdInternal());
			
			person = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true); 
			person.setNumeroDocumentoIdentificacao(oldStudent.getDocumentidnumber());
		
		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new Exception(e);
		}
	}
	
	
}
