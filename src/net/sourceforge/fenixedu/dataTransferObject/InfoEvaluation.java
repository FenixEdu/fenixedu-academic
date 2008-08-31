package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoOnlineTest;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author Ângela
 * 
 *         25/6/2003
 */
public class InfoEvaluation extends InfoShowOccupation implements ISiteComponent {

    public static final Comparator<InfoEvaluation> COMPARATOR_BY_START = new Comparator<InfoEvaluation>() {

	@Override
	public int compare(InfoEvaluation o1, InfoEvaluation o2) {
	    return o1.getInicio().compareTo(o2.getInicio());
	}
	
    };

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

    // Methods inherited from abstract InfoShowOccupations - not used
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

    public void copyFromDomain(Evaluation evaluation) {
	super.copyFromDomain(evaluation);
	if (evaluation != null) {
	    setPublishmentMessage(evaluation.getPublishmentMessage());
	}
    }

    /**
     * @param evaluation
     * @return
     */
    public static InfoEvaluation newInfoFromDomain(Evaluation evaluation) {
	InfoEvaluation infoEvaluation = null;
	if (evaluation != null) {
	    if (evaluation instanceof WrittenEvaluation) {
		infoEvaluation = InfoWrittenEvaluation.newInfoFromDomain((WrittenEvaluation) evaluation);
	    } else if (evaluation instanceof FinalEvaluation) {
		infoEvaluation = InfoFinalEvaluation.newInfoFromDomain((FinalEvaluation) evaluation);
	    } else if (evaluation instanceof OnlineTest) {
		infoEvaluation = InfoOnlineTest.newInfoFromDomain((OnlineTest) evaluation);
	    } else {
		infoEvaluation = new InfoEvaluation();
		infoEvaluation.copyFromDomain(evaluation);
	    }
	}
	return infoEvaluation;
    }
}