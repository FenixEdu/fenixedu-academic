
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.enrolment;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoFinalResult;
import DataBeans.InfoStudentCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;

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

	public InfoFinalResult run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws Exception {


		boolean result = false;

		//CLONER
		//IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);
		IStudentCurricularPlan studentCurricularPlan = InfoStudentCurricularPlan.newDomainFromInfo(infoStudentCurricularPlan);
		IDegreeCurricularPlan degreeCurricularPlan = InfoDegreeCurricularPlan.newDomainFromInfo(infoStudentCurricularPlan.getInfoDegreeCurricularPlan());
		ICurso degree = InfoDegree.newDomainFromInfo(infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getInfoDegree());
		degreeCurricularPlan.setDegree(degree);
		studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
		
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
	
		IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());


		// verify if the school part is concluded
		result = masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan);

		if (result == true){
			InfoFinalResult infoFinalResult = new InfoFinalResult();
			
			masterDegreeCurricularPlanStrategy.calculateStudentAverage(studentCurricularPlan, infoFinalResult);


			return infoFinalResult;	
		}	

		return null;
	}
}
