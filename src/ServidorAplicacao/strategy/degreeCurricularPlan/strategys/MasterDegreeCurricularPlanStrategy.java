package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Calendar;
import java.util.Date;
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


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
 
public class MasterDegreeCurricularPlanStrategy extends DegreeCurricularPlanStrategy implements IMasterDegreeCurricularPlanStrategy {

	public MasterDegreeCurricularPlanStrategy(IDegreeCurricularPlan degreeCurricularPlan) {
		super(degreeCurricularPlan);
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
			if ((enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ))&&
				(!(enrolment instanceof IEnrolmentInExtraCurricularCourse))){
				studentCredits += enrolment.getCurricularCourseScope().getCurricularCourse().getCredits().floatValue(); 
			}
		}
		
		return (studentCredits >= degreeCurricularPlan.getNeededCredits().floatValue());
	}
	
	
	public Date dateOfEndOfScholarship(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		
		Calendar date = null;
		
		boolean result = false;
		float studentCredits = 0;
		
		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();
		
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
		
		Iterator iterator = enrolments.iterator();
		
		while(iterator.hasNext()){
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if (enrolment.getEnrolmentState().equals(EnrolmentState.APROVED_OBJ)){
				Iterator evaluations = enrolment.getEvaluations().iterator();
				while(evaluations.hasNext()){
					IEnrolmentEvaluation evaluation = (IEnrolmentEvaluation) evaluations.next(); 
					if (evaluation.getExamDate() == null){
						continue;
					}
					
					if (date == null){
						date = Calendar.getInstance();
						date.setTime(evaluation.getExamDate());
						continue;
					}
					
					Calendar examDate = Calendar.getInstance();
					examDate.setTime(evaluation.getExamDate());
					
					if (examDate.get(Calendar.YEAR) > date.get(Calendar.YEAR)){
						date.setTime(evaluation.getExamDate());
						continue;
					}


					if ((examDate.get(Calendar.MONTH) > date.get(Calendar.MONTH )) &&
						(examDate.get(Calendar.YEAR) > date.get(Calendar.YEAR ))){
							date.setTime(evaluation.getExamDate());
							continue;
					}

					
					if ((examDate.get(Calendar.DAY_OF_MONTH) > date.get(Calendar.DAY_OF_MONTH )) &&
						(examDate.get(Calendar.MONTH) > date.get(Calendar.MONTH )) &&
						(examDate.get(Calendar.YEAR) > date.get(Calendar.YEAR ))){
							date.setTime(evaluation.getExamDate());
					}
					
				}
			}
		}
		
		return date.getTime();
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
//				new Integer(5124), TipoCurso.MESTRADO_OBJ);
//		
//		
//		System.out.println(Data.format2DayMonthYear(degreeCurricularPlanStrategy.dateOfEndOfScholarship(studentCurricularPlan)));
//				
//		SuportePersistenteOJB.getInstance().confirmarTransaccao();
//		
//	}

}