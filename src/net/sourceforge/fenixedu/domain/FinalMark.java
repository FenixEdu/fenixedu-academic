package net.sourceforge.fenixedu.domain;

public class FinalMark extends FinalMark_Base {

    public FinalMark() {
	super();
	setGradeListVersion(0);
    }

    public void setFinalEvaluation(FinalEvaluation finalEvaluation) {
	setEvaluation(finalEvaluation);
    }

    public FinalEvaluation getFinalEvaluation() {
	return (FinalEvaluation) getEvaluation();
    }
}
