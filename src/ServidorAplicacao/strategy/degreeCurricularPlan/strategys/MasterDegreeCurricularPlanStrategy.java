package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Iterator;
import java.util.List;

import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
 
public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements IMasterDegreeCurricularPlanStrategy {

	public MasterDegreeCurricularPlanStrategy() {
	}


	/**
	 * Checks if the Master Degree Student has finished his scholar part.<br>
	 * All his credits are added and compared to the ones required by his Degree Curricular Plan.
	 * @param The Student's Curricular Plan
	 * @return A boolean indicating if he has fineshed it or not.
	 */
	public boolean checkEndOfScholarship(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		boolean result = false;
		float studentCredits = 0;
		
		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();
		
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
		
		Iterator iterator = enrolments.iterator();
		
		if (studentCurricularPlan.getGivenCredits() != null) {
			studentCredits += studentCurricularPlan.getGivenCredits().floatValue();
		}
		
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ)){
				studentCredits += enrolment.getCurricularCourseScope().getCurricularCourse().getCredits().floatValue(); 
			}
		}
		
		return (studentCredits >= degreeCurricularPlan.getNeededCredits().floatValue());
	}



	public static void main(String[] args) throws ExcepcaoPersistencia{
		
		SuportePersistenteOJB.getInstance().iniciarTransaccao();
		ICurso degree = SuportePersistenteOJB.getInstance().getICursoPersistente().readBySigla("MC");
		
		IDegreeCurricularPlan degreeCurricularPlan = SuportePersistenteOJB.getInstance().getIPersistentDegreeCurricularPlan().readByNameAndDegree(
			"MC02/03", degree);
			
			
			
		System.out.println();
			
		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
		IMasterDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
		
		
		IStudentCurricularPlan studentCurricularPlan = SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
				new Integer(5124), TipoCurso.MESTRADO_OBJ);
		
		
		System.out.println(degreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan));
				
		SuportePersistenteOJB.getInstance().confirmarTransaccao();
		
	}

}