package DataBeans;

import Dominio.IEvaluation;
import Dominio.IExam;
import Dominio.IFinalEvaluation;
import Dominio.IOnlineTest;
import Util.EvaluationType;

/**
 * @author Ângela
 * 
 * 25/6/2003
 */
public class InfoEvaluation extends InfoObject implements ISiteComponent {

    private String publishmentMessage;

    private EvaluationType evaluationType;

    public InfoEvaluation() {
        setPublishmentMessage(null);
        setEvaluationType(null);
    }

    public InfoEvaluation(String publishmentMessage, EvaluationType type) {
        this();
        setPublishmentMessage(publishmentMessage);
        setEvaluationType(type);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoEvaluation) {
            InfoEvaluation infoEvaluation = (InfoEvaluation) obj;
            resultado = this.getPublishmentMessage().equals(
                    infoEvaluation.getPublishmentMessage())
                    && this.getEvaluationType() == infoEvaluation
                            .getEvaluationType();
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "publishmentMessage = " + this.publishmentMessage + "; ";
        result += "type = " + this.evaluationType + "]";
        return result;
    }

    /**
     * @return
     */
    public String getPublishmentMessage() {
        return publishmentMessage;
    }

    /**
     * @param string
     */
    public void setPublishmentMessage(String string) {
        this.publishmentMessage = string;
    }

    /**
     * @return
     */
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    /**
     * @param type
     */
    public void setEvaluationType(EvaluationType type) {
        evaluationType = type;
    }

    public void copyFromDomain(IEvaluation evaluation) {
        super.copyFromDomain(evaluation);
        if (evaluation != null) {
            setPublishmentMessage(evaluation.getPublishmentMessage());
        }
    }

    /**
     * @param evaluation
     * @return
     */
    public static InfoEvaluation newInfoFromDomain(IEvaluation evaluation) {
        InfoEvaluation infoEvaluation = null;
        if (evaluation != null) {
            if (evaluation instanceof IExam) {
                infoEvaluation = InfoExam.newInfoFromDomain((IExam) evaluation);
            } else if (evaluation instanceof IFinalEvaluation) {
                infoEvaluation = InfoFinalEvaluation
                        .newInfoFromDomain((IFinalEvaluation) evaluation);
            } else if (evaluation instanceof IOnlineTest) {
                infoEvaluation = InfoOnlineTest
                        .newInfoFromDomain((IOnlineTest) evaluation);
            }
        }
        return infoEvaluation;
    }
}