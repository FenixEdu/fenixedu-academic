package DataBeans;

import Dominio.IFinalEvaluation;
import Util.EvaluationType;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoFinalEvaluation extends InfoEvaluation implements
        ISiteComponent {

    
    public void copyFromDomain(IFinalEvaluation finalEvaluation) {
     super.copyFromDomain(finalEvaluation);
     if (finalEvaluation!=null) {
        setEvaluationType(EvaluationType.FINAL_TYPE);
     }
    }
    /**
     * @param finalEvaluation
     * @return
     */
    public static InfoFinalEvaluation newInfoFromDomain(IFinalEvaluation finalEvaluation) {
        InfoFinalEvaluation infoFinalEvaluation = null;
        if (finalEvaluation != null) {
            infoFinalEvaluation = new InfoFinalEvaluation();
            infoFinalEvaluation.copyFromDomain(finalEvaluation);
        }
        return infoFinalEvaluation;
    }

}