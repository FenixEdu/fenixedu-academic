package DataBeans;

import Dominio.IFinalEvaluation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoFinalEvaluation extends InfoEvaluation implements
        ISiteComponent {

    /**
     * @param finalEvaluation
     * @return
     */
    public static InfoFinalEvaluation copyFromDomain(IFinalEvaluation finalEvaluation) {
        InfoFinalEvaluation infoFinalEvaluation = null;
        if (finalEvaluation != null) {
            infoFinalEvaluation = new InfoFinalEvaluation();
            infoFinalEvaluation.setIdInternal(finalEvaluation.getIdInternal());
            infoFinalEvaluation.setPublishmentMessage(finalEvaluation
                    .getPublishmentMessage());
        }
        return infoFinalEvaluation;
    }

}