
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.util.Cloner;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorAplicacao.IServico;
import Util.EnrolmentEvaluationState;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentMark implements IServico {
    
    private static GetEnrolmentMark servico = new GetEnrolmentMark();
    
    /**
     * The singleton access method of this class.
     **/
    public static GetEnrolmentMark getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private GetEnrolmentMark() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "GetEnrolmentMark";
    }
    
    
    public InfoEnrolmentEvaluation run(IEnrolment enrolment)  {

		List enrolmentEvaluations = enrolment.getEvaluations();

		if ((enrolment == null) || (enrolment.getEvaluations() == null) || (enrolment.getEvaluations().size() == 0)) {
			return null;
		}        

		// if there's only one evaluation ...
		if (enrolmentEvaluations.size() == 1) {
			if (!((IEnrolmentEvaluation) enrolmentEvaluations.get(0)).getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)){
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) enrolmentEvaluations.get(0);
				return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
			} else {
				return null;
			}

		}

		Iterator iterator = enrolmentEvaluations.iterator();
		List enrolmentEvaluationsFinal = new ArrayList();
		while(iterator.hasNext()){
			IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) iterator.next();
			if (!enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)){
				enrolmentEvaluationsFinal.add(enrolmentEvaluation);
			}
		}
		
		BeanComparator dateComparator = new BeanComparator("gradeAvailableDate");

		Collections.sort(enrolmentEvaluationsFinal, dateComparator);
		Collections.reverse(enrolmentEvaluationsFinal);

		return Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation((IEnrolmentEvaluation) enrolmentEvaluationsFinal.get(0));
    }
    
}