package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.gratuity;

import DataBeans.InfoGratuity;
import DataBeans.util.Cloner;
import Dominio.IGratuity;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadGratuityByStudentCurricularPlanID implements IServico {

	private static ReadGratuityByStudentCurricularPlanID servico = new ReadGratuityByStudentCurricularPlanID();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadGratuityByStudentCurricularPlanID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadGratuityByStudentCurricularPlanID() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadGratuityByStudentCurricularPlanID";
	}

	public InfoGratuity run(Integer studentCurricularPlanID) throws FenixServiceException {
		
		IGratuity gratuity = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			gratuity = sp.getIPersistentGratuity().readByStudentCurricularPlanIDAndState(studentCurricularPlanID, new State(State.ACTIVE));

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (gratuity == null){
			throw new NonExistingServiceException();
		}

		return Cloner.copyIGratuity2InfoGratuity(gratuity);
	}
}
