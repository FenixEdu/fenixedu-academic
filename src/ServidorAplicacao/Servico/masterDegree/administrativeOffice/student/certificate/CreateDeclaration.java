
package ServidorAplicacao
	.Servico
	.masterDegree
	.administrativeOffice
	.student
	.certificate;

import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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

		sp = SuportePersistenteOJB.getInstance();
		
		try {
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

		if (studentCurricularPlan.getIdInternal() == null) 
			return null;
		 
		else
		
			return Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);
			
				
	}
}
