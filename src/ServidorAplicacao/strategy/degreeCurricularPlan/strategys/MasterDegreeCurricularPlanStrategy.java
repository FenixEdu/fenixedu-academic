package ServidorAplicacao.strategy.degreeCurricularPlan.strategys;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoEnrolmentEvaluation;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IEnrolmentInExtraCurricularCourse;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.commons.student.GetEnrolmentGrade;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentState;


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
		double studentCredits = 0;
		
		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();
		
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
		
		Iterator iterator = enrolments.iterator();
		
		if (studentCurricularPlan.getGivenCredits() != null) {
			studentCredits += studentCurricularPlan.getGivenCredits().doubleValue();
		}

		while(iterator.hasNext()){
			IEnrollment enrolment = (IEnrollment) iterator.next();
	
			if ((enrolment.getEnrollmentState().equals(EnrollmentState.APROVED))&&
				(!(enrolment instanceof IEnrolmentInExtraCurricularCourse))){
				studentCredits += enrolment.getCurricularCourse().getCredits().doubleValue(); 
			}
		}
		
		return (studentCredits >= degreeCurricularPlan.getNeededCredits().doubleValue());
	}
	
	
	public Date dateOfEndOfScholarship(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia{
		
	    Date date = null;
		InfoEnrolmentEvaluation infoEnrolmentEvaluation = null;
		
//		float studentCredits = 0;
//		
//		IDegreeCurricularPlan degreeCurricularPlan = super.getDegreeCurricularPlan();
		
		List enrolments = SuportePersistenteOJB.getInstance().getIPersistentEnrolment().readAllByStudentCurricularPlan(studentCurricularPlan);
		
		Iterator iterator = enrolments.iterator();
		
		while(iterator.hasNext()){
			IEnrollment enrolment = (IEnrollment) iterator.next();
			if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)){
			    
			    try {
                    infoEnrolmentEvaluation  = GetEnrolmentGrade.getService().run(enrolment);
                } catch (FenixServiceException e) {
                    e.printStackTrace();
                    continue;
                }
			    
				if (infoEnrolmentEvaluation.getExamDate() == null){
					continue;
				}
				
				if (date == null){
				    date = new Date(infoEnrolmentEvaluation.getExamDate().getTime());
					continue;
				}
                
				if(infoEnrolmentEvaluation.getExamDate().after(date)) {
				    date.setTime(infoEnrolmentEvaluation.getExamDate().getTime());
				}
				
			}
		}
		return date;
	}



//	public static void main(String[] args) throws ExcepcaoPersistencia{
//		
//		SuportePersistenteOJB.getInstance().iniciarTransaccao();
//		ICurso degree = SuportePersistenteOJB.getInstance().getICursoPersistente().readBySigla("MMA");
//		
//		IDegreeCurricularPlan degreeCurricularPlan = SuportePersistenteOJB.getInstance().getIPersistentDegreeCurricularPlan().readByNameAndDegree(
//			"MMA02/03", degree);
//			
//			
//			
//		IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory.getInstance();
//		IMasterDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory.getDegreeCurricularPlanStrategy(degreeCurricularPlan);
//		
//		
//		IStudentCurricularPlan studentCurricularPlan = SuportePersistenteOJB.getInstance().getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
//				new Integer(5145), TipoCurso.MESTRADO_OBJ);
//		
//		
//		System.out.println(Data.format2DayMonthYear(degreeCurricularPlanStrategy.dateOfEndOfScholarship(studentCurricularPlan)));
//		System.out.println(degreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan));
//		System.out.println(degreeCurricularPlanStrategy.calculateStudentRegularAverage(studentCurricularPlan));
//		System.out.println(degreeCurricularPlanStrategy.calculateStudentWeightedAverage(studentCurricularPlan));
//		
//		InfoFinalResult infoFinalResult = new InfoFinalResult();
//		infoFinalResult.setAverageSimple("4.5");
//		
//		degreeCurricularPlanStrategy.calculateStudentAverage(studentCurricularPlan, infoFinalResult);
//		
//		System.out.println(infoFinalResult.getFinalAverage());
//		
//		
//		SuportePersistenteOJB.getInstance().confirmarTransaccao();
//		
//	}

}