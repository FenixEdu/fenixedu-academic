
package ServidorAplicacao.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.IServico;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade implements IServico {
    
    private static GetEnrolmentGrade servico = new GetEnrolmentGrade();
    
    /**
     * The singleton access method of this class.
     **/
    public static GetEnrolmentGrade getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private GetEnrolmentGrade() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "GetEnrolmentGrade";
    }
    
    
    public InfoEnrolmentEvaluation run(IEnrolment enrolment)  {

		List enrolmentEvaluations = enrolment.getEvaluations();
		
		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0)) {
			return null;
		}        
		
		// if there's only one evaluation ...
		if (enrolmentEvaluations.size() == 1){

			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
			
			try {
				
				enrolmentEvaluation.setGrade((new Integer(enrolmentEvaluation.getGrade())).toString());	

			} catch(NumberFormatException e) {
			
			}
			
			return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
		}

		BeanComparator dateComparator = new BeanComparator("when");

		Collections.sort(enrolmentEvaluations, dateComparator);
		Collections.reverse(enrolmentEvaluations);

		IEnrolmentEvaluation latestEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);

		if (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED)){
			return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		}


		// if the last evaluation is Not of "IMPROVEMENT" type
		if (!latestEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)){
			latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
			return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		} else {
			IEnrolmentEvaluation previousEvaluation = null;
			for (int i = 1; i < enrolmentEvaluations.size(); i++){
				previousEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(i);
				if (!previousEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)){
					break;
				}
			}
			
			Integer latestMark = null;
			try{
				latestMark = new Integer(latestEvaluation.getGrade());
			} catch (NumberFormatException e) {
				// If there's an Exception , the the student wasn't able to improve
				previousEvaluation.setGrade((new Integer(previousEvaluation.getGrade())).toString());
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
			}
			
			// if there is no exception we must check wick is the higher grade
			
			Integer previousMark = new Integer(previousEvaluation.getGrade());
			
			if (previousMark.intValue() >= latestMark.intValue()){
				previousEvaluation.setGrade((new Integer(previousEvaluation.getGrade())).toString());
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
			} else {
				latestEvaluation.setGrade((new Integer(latestEvaluation.getGrade())).toString());
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
			}
		}

    }
    
}