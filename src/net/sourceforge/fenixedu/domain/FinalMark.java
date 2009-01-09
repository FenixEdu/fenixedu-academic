package net.sourceforge.fenixedu.domain;

public class FinalMark extends FinalMark_Base {

    public FinalMark() {
	super();
	setGradeListVersion(0);
    }

    public FinalMark(final Attends attends, final FinalEvaluation finalEvaluation, final String markValue) {
	this();
	setAttend(attends);
	setEvaluation(finalEvaluation);
	setMark(markValue);
    }

    public void setFinalEvaluation(FinalEvaluation finalEvaluation) {
	setEvaluation(finalEvaluation);
    }

    public FinalEvaluation getFinalEvaluation() {
	return (FinalEvaluation) getEvaluation();
    }
}
