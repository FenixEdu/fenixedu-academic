
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
		
		// if there if no aproval in the enrolment ....
		if (!enrolment.getEnrolmentState().equals(EnrolmentState.APROVED)){
			return new InfoEnrolmentEvaluation(); 
		}

		// if there's only one evaluation ...
		if (enrolmentEvaluations.size() == 1){
			return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation((IEnrolmentEvaluation) enrolmentEvaluations.get(0));
		}

		BeanComparator dateComparator = new BeanComparator("gradeAvailableDate");

		Collections.sort(enrolmentEvaluations, dateComparator);
		Collections.reverse(enrolmentEvaluations);

		IEnrolmentEvaluation latestEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);


		// if the last evaluation is Not of "IMPROVEMENT" type
		if (!latestEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)){
			return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
		} else {
			IEnrolmentEvaluation previousEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(1);
			Integer latestMark = null;
			try{
				latestMark = new Integer(latestEvaluation.getGrade());
			} catch (NumberFormatException e) {
				// If there's an Exception , the the student wasn't able to improve
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
			}
			
			// if there is no exception we must check wick is the higher grade
			
			Integer previousMark = new Integer(previousEvaluation.getGrade());
			
			if (previousMark.intValue() >= latestMark.intValue()){
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(previousEvaluation);
			} else {
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(latestEvaluation);
			}
		}

    }
    
}