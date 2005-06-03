package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IFinalEvaluation;
import net.sourceforge.fenixedu.domain.IOnlineTest;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.domain.ShiftType;

/**
 * @author Ângela
 * 
 * 25/6/2003
 */
public class InfoEvaluation extends InfoShowOccupation implements ISiteComponent {

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
            resultado = this.getPublishmentMessage().equals(infoEvaluation.getPublishmentMessage())
                    && this.getEvaluationType() == infoEvaluation.getEvaluationType();
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

    //Methods inherited from abstract InfoShowOccupations - not used
    public InfoShift getInfoShift() {
        return null;
    }

    public ShiftType getTipo() {
        return null;
    }

    public InfoRoomOccupation getInfoRoomOccupation() {
        return null;
    }

    public DiaSemana getDiaSemana() {
        return null;
    }

    public Calendar getInicio() {
        return null;
    }

    public Calendar getFim() {
        return null;
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
            if (evaluation instanceof IWrittenEvaluation) {
                infoEvaluation = InfoWrittenEvaluation
                        .newInfoFromDomain((IWrittenEvaluation) evaluation);
            } else if (evaluation instanceof IFinalEvaluation) {
                infoEvaluation = InfoFinalEvaluation.newInfoFromDomain((IFinalEvaluation) evaluation);
            } else if (evaluation instanceof IOnlineTest) {
                infoEvaluation = InfoOnlineTest.newInfoFromDomain((IOnlineTest) evaluation);
            } else {
                infoEvaluation = new InfoEvaluation();
                infoEvaluation.copyFromDomain(evaluation);
            }
        }
        return infoEvaluation;
    }
}