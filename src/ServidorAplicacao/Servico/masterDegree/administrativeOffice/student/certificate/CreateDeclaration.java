
package ServidorAplicacao
	.Servico
	.masterDegree
	.administrativeOffice
	.student
	.certificate;

import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.Specialization;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CreateDeclaration implements IServico {

	private static CreateDeclaration servico = new CreateDeclaration();

	/**
	 * The singleton access method of this class.
	 **/
	public static CreateDeclaration getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CreateDeclaration() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "CreateDeclaration";
	}

	public InfoStudentCurricularPlan run(InfoStudent infoStudent, Specialization specialization)
		throws Exception {

		IStudentCurricularPlan studentCurricularPlan = null;

		ISuportePersistente sp = null;

		IRole role = null;
		IPessoa person = null;

		try {
			sp = SuportePersistenteOJB.getInstance();

			studentCurricularPlan =
				sp
					.getIStudentCurricularPlanPersistente()
					.readActiveStudentAndSpecializationCurricularPlan(
					infoStudent.getNumber(),
					infoStudent.getDegreeType(), 
					specialization);
					
	
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx =
				new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (studentCurricularPlan == null)
		    return null;
		else
			return Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
			
		//		try {
		//			// Give the Master Degree Candidate Role
		//			IPersonRole personRole = new PersonRole();
		//			role = sp.getIPersistentRole().readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
		//			personRole.setPerson(person);
		//			personRole.setRole(role);
		//				
		//			sp.getIPersistentPersonRole().write(personRole);
		//
		//		} catch (ExistingPersistentException ex) {
		//			// This person is already a Candidate. No need to give the role again
		//		} catch (ExcepcaoPersistencia ex) {
		//			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
		//			newEx.fillInStackTrace();
		//			throw newEx;
		//		} 
		//		
		//		// Return the new Candidate
		
	}
}
