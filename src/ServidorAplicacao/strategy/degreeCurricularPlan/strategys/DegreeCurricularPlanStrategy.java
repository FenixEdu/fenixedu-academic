package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Iterator;
import java.util.List;

import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEnrolmentInExtraCurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.MarkType;
import Util.NumberUtils;


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

	public Double calculateStudentRegularAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		float marks = 0;
		int numberOfCourses = 0;
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
	
		Iterator iterator = enrolments.iterator();
		int courses = 0;
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ)){
				if (!(enrolment instanceof IEnrolmentInExtraCurricularCourse)){
					
					Iterator evaluations = enrolment.getEvaluations().iterator();
					int enrolmentMark = 0;
					while(evaluations.hasNext()){
						IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) evaluations.next();
						try {
							if (new Integer(enrolmentEvaluation.getGrade()).intValue() > enrolmentMark){
								enrolmentMark = new Integer(enrolmentEvaluation.getGrade()).intValue();
							}
						} catch (NumberFormatException e) {
							// This mark will not count for the average
						}
					}
					if (enrolmentMark > 0) {
						marks += enrolmentMark;
						numberOfCourses++;		
					}
				}
			}
			
		}
		return NumberUtils.formatNumber(new Double(marks/numberOfCourses), 1);		
	}
	
	public Double calculateStudentWeightedAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		float marks = 0;
		int numberOfWeigths = 0;
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
	
		Iterator iterator = enrolments.iterator();
		int courses = 0;
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ)){
				if (!(enrolment instanceof IEnrolmentInExtraCurricularCourse)){
					
					Iterator evaluations = enrolment.getEvaluations().iterator();
					int enrolmentMark = 0;
					float enrolmentWeight = 0;
					while(evaluations.hasNext()){
						IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) evaluations.next();
						try {
							if (new Integer(enrolmentEvaluation.getGrade()).intValue() > enrolmentMark){
								enrolmentMark = new Integer(enrolmentEvaluation.getGrade()).intValue();
								enrolmentWeight = enrolment.getCurricularCourseScope().getCurricularCourse().getCredits().floatValue();		
							}
						} catch (NumberFormatException e) {
							// This mark will not count for the average
						}
					}
					if (enrolmentMark > 0) {
						marks += (enrolmentMark * enrolmentWeight);
						numberOfWeigths += enrolmentWeight;		
					}
					
					
				}
			}
			
		}
		return NumberUtils.formatNumber(new Double(marks/numberOfWeigths), 1);		
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