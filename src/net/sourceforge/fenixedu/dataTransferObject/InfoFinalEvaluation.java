package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoFinalEvaluation extends InfoEvaluation implements ISiteComponent {

    public void copyFromDomain(FinalEvaluation finalEvaluation) {
	super.copyFromDomain(finalEvaluation);
	if (finalEvaluation != null) {
	    setEvaluationType(EvaluationType.FINAL_TYPE);
	}
    }

    /**
     * @param finalEvaluation
     * @return
     */
    public static InfoFinalEvaluation newInfoFromDomain(FinalEvaluation finalEvaluation) {
	InfoFinalEvaluation infoFinalEvaluation = null;
	if (finalEvaluation != null) {
	    infoFinalEvaluation = new InfoFinalEvaluation();
	    infoFinalEvaluation.copyFromDomain(finalEvaluation);
	}
	return infoFinalEvaluation;
    }

}