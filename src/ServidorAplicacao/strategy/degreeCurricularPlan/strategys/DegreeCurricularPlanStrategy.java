package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import DataBeans.InfoFinalResult;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEnrolmentInExtraCurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;
import Util.EvaluationType;
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
	
	public boolean checkMark(String mark, EvaluationType et){

	    if(et.getType().intValue() == EvaluationType.FINAL)
	    { 
	        return MarkType.getMarks(degreeCurricularPlan.getMarkType()).contains(mark);
	    }
	    else
	    {
	        boolean result = false;			
			StringTokenizer st = new StringTokenizer(mark, ".");

	        if(st.countTokens() > 0 && st.countTokens() < 3)
	        {
	            result = MarkType.getMarksEvaluation(degreeCurricularPlan.getMarkType()).contains(st.nextToken());
	            if(result && st.hasMoreTokens())
	            {
	                try
	                {
	                    Double markDouble = new Double(mark);
	                    if(markDouble.doubleValue() < 0 || markDouble.doubleValue() > 20)
	                    {
	                        result = false;
	                    }
	                }catch(NumberFormatException ex)
	                {
	                    return false;
	                }
	            }
	        }
	        return result;        
	    }
	}

	public Double calculateStudentRegularAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		float marks = 0;
		int numberOfCourses = 0;
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
	
		Iterator iterator = enrolments.iterator();
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if ((enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)) &&
				(!enrolment.getCurricularCourseScope().getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE_OBJ))){
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
		
		if (marks == 0){
			return new Double(0);
		}
		return NumberUtils.formatNumber(new Double(marks/numberOfCourses), 1);		
	}
	
	public Double calculateStudentWeightedAverage(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		float marks = 0;
		int numberOfWeigths = 0;
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
	
		Iterator iterator = enrolments.iterator();
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if ((enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)) &&
				(!enrolment.getCurricularCourseScope().getCurricularCourse().getType().equals(CurricularCourseType.P_TYPE_COURSE_OBJ))){
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
		
		if (marks == 0){
			return new Double(0);
		}
		
		return NumberUtils.formatNumber(new Double(marks/numberOfWeigths), 1);		
	}



	public void calculateStudentAverage(IStudentCurricularPlan studentCurricularPlan, InfoFinalResult infoFinalResult) throws ExcepcaoPersistencia {
		
		// Degrees that use the Mixed Average (Average between Simple and Weighted average)
		if ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT02/03")) || 
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MT03/05"))){
			Double simpleAverage = this.calculateStudentRegularAverage(studentCurricularPlan);
			Double weightedAverage = this.calculateStudentWeightedAverage(studentCurricularPlan);
			
			infoFinalResult.setAverageSimple(String.valueOf(simpleAverage));
			infoFinalResult.setAverageWeighted(String.valueOf(weightedAverage));
			
			infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(new Double((simpleAverage.floatValue()+weightedAverage.floatValue())/2), 0)));
			return;
		}

		// Degrees that use the Best Average (Best between Simple and Weighted average)
		if ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MB02/03")) || 
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MB03/04")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MIOES02/03"))) {
				Double simpleAverage = this.calculateStudentRegularAverage(studentCurricularPlan);
				Double weightedAverage = this.calculateStudentWeightedAverage(studentCurricularPlan);
		
				infoFinalResult.setAverageSimple(String.valueOf(simpleAverage));
				infoFinalResult.setAverageWeighted(String.valueOf(weightedAverage));
				
				if (simpleAverage.floatValue() > weightedAverage.floatValue()){
					infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(simpleAverage, 0)));
				} else {
					infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(weightedAverage, 0)));
				}
				return;
		}
		
		
		// Degrees that use the Weighted Average 
		if ((this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEE03/05")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MF02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MF03/05")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MC02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MC03/05")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEMAT02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEQ03/04")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MSIG02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MCES02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEIC02/03")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("MEIC03/05")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("ML03/05")) ||
			(this.getDegreeCurricularPlan().getName().equalsIgnoreCase("ML02/03"))) {
				
				Double weightedAverage = this.calculateStudentWeightedAverage(studentCurricularPlan);

				infoFinalResult.setAverageWeighted(String.valueOf(weightedAverage));
				infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(weightedAverage, 0)));
				return;
		}
		
		// Everything else uses the simple Average
		
		Double simpleAverage = this.calculateStudentRegularAverage(studentCurricularPlan);
		infoFinalResult.setAverageSimple(String.valueOf(simpleAverage));
				
		infoFinalResult.setFinalAverage(String.valueOf(NumberUtils.formatNumber(simpleAverage, 0)));
		
	}


//	public static void main(String[] args) throws ExcepcaoPersistencia{
//		
//		SuportePersistenteOJB.getInstance().iniciarTransaccao();
//		ICurso degree = SuportePersistenteOJB.getInstance().getICursoPersistente().readBySigla("MC");
//		
//		IDegreeCurricularPlan degreeCurricularPlan = SuportePersistenteOJB.getInstance().getIPersistentDegreeCurricularPlan().readByNameAndDegree(
//			"MC02/03", degree);
//			
//			
//			
//		System.out.println();
//			
//		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
//		IMasterDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
//		
//		
//		IStudentCurricularPlan studentCurricularPlan = SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
//				new Integer(5325), TipoCurso.MESTRADO_OBJ);
//		
//		
//		System.out.println(degreeCurricularPlanStrategy.calculateStudentRegularAverage(studentCurricularPlan));
//				
//		SuportePersistenteOJB.getInstance().confirmarTransaccao();
//		
//		
//		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
//		DegreeCurricularPlanStrategy degreeCurricularPlanStrategy = new DegreeCurricularPlanStrategy(degreeCurricularPlan);
//		degreeCurricularPlan.setMarkType(MarkType.TYPE20_OBJ);
//		
//		System.out.println(degreeCurricularPlanStrategy.checkMark("RE"));
		
//	}



}