package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import Util.MarkType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class DegreeCurricularPlanStrategy implements IDegreeCurricularPlanStrategy {

	private IDegreeCurricularPlan degreeCurricularPlan = null;


	public DegreeCurricularPlanStrategy(){}

	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}


	public boolean checkMark(String mark){

		return MarkType.getMarks(degreeCurricularPlan.getMarkType()).contains(mark);
	}

	public Double calculateStudentAverage(IStudentCurricularPlan studentCurricularPlan){
		return null;
		
	}



//	public static void main(String[] args){
//		
//		
//		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
//		DegreeCurricularPlanStrategy degreeCurricularPlanStrategy = new DegreeCurricularPlanStrategy();
//		degreeCurricularPlanStrategy.setDegreeCurricularPlan(degreeCurricularPlan);
//		degreeCurricularPlan.setMarkType(MarkType.TYPE20_OBJ);
//		
//		System.out.println(degreeCurricularPlanStrategy.checkMark("RE"));
//		
//		
//		
//	}
//
//
//
}