package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.gratuity;

import java.util.Iterator;
import java.util.List;

import Dominio.IGuide;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadGratuityInformationByStudentCurricularPlanID implements IServico {

	private static ReadGratuityInformationByStudentCurricularPlanID servico = new ReadGratuityInformationByStudentCurricularPlanID();

	/**
	 * The singleton access method of this class.
	 **/
	public static ReadGratuityInformationByStudentCurricularPlanID getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadGratuityInformationByStudentCurricularPlanID() {
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "ReadGratuityInformationByStudentCurricularPlanID";
	}

	public List run(Integer studentCurricularPlanID) throws FenixServiceException {
		
		
		List guides = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
			studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente().readByOId(studentCurricularPlanTemp, false);
			
			guides = sp.getIPersistentGuide().readByPerson(studentCurricularPlan.getStudent().getPerson().getNumeroDocumentoIdentificacao(), 
										studentCurricularPlan.getStudent().getPerson().getTipoDocumentoIdentificacao());



		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		if (guides == null){
			throw new NonExistingServiceException();
		}

		Iterator guidesIterator = guides.iterator();
		while(guidesIterator.hasNext()){
			IGuide guide = (IGuide) guidesIterator.next();
//			Iterator guideEntryIterator = guide.get
		}


		return null;
	}
}
