
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.enrolment;

import DataBeans.InfoFinalResult;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class FinalResult implements IServico {

	private static FinalResult servico = new FinalResult();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static FinalResult getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private FinalResult() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "FinalResult";
	}

	public InfoFinalResult run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws FenixServiceException, Exception {


		boolean result;

		// verify if the school part is concluded 
		// if concluded result is true
		result = true;
		if (result){
			InfoFinalResult infoFinalResult = new InfoFinalResult();
			infoFinalResult.setAverageSimple("13.5");
			infoFinalResult.setAverageWeighted("13.5");
			infoFinalResult.setFinalAverage("13");
			return infoFinalResult;	
		}	
		//if not concluded result is false
		else{
			result = false;
			return null;
		}
	}
}
