package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Iterator;
import java.util.List;

import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.MarkType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class DegreeCurricularPlanStrategy implements IDegreeCurricularPlanStrategy {

	private IDegreeCurricularPlan degreeCurricularPlan = null;


	public DegreeCurricularPlanStrategy(IDegreeCurricularPlan degreeCurricularPlan){
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}


	public boolean checkMark(String mark){

		return MarkType.getMarks(degreeCurricularPlan.getMarkType()).contains(mark);
	}

	public Double calculateStudentAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		float average = 0;
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
	
		Iterator iterator = enrolments.iterator();
		int courses = 0;
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
//			enrolment.getEnrolmentState()
			
		}
		return new Double(average);
	}



//	public static void main(String[] args){
//		
//		
//		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
//		DegreeCurricularPlanStrategy degreeCurricularPlanStrategy = new DegreeCurricularPlanStrategy(degreeCurricularPlan);
//		degreeCurricularPlan.setMarkType(MarkType.TYPE20_OBJ);
//		
//		System.out.println(degreeCurricularPlanStrategy.checkMark("RE"));
//		
//	}



}