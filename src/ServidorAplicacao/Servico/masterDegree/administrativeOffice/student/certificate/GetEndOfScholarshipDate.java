
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.Date;

import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEndOfScholarshipDate implements IServico {

	private static GetEndOfScholarshipDate servico = new GetEndOfScholarshipDate();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static GetEndOfScholarshipDate getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GetEndOfScholarshipDate() { 
	}

	/**
	 * Returns The Service Name */

	public final String getNome() {
		return "GetEndOfScholarshipDate";
	}

	
	public Date run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws ExcepcaoPersistencia {

			IStudentCurricularPlan studentCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);


		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		
		IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());
	
	
		return masterDegreeCurricularPlanStrategy.dateOfEndOfScholarship(studentCurricularPlan);
	
	
	}
	
}
