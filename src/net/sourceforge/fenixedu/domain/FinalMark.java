package net.sourceforge.fenixedu.domain;

public class FinalMark extends FinalMark_Base {
    
    public FinalMark() {
        super();
        setGradeListVersion(0);
    }
    
    public void setFinalEvaluation(IFinalEvaluation finalEvaluation) {
    	setEvaluation(finalEvaluation);
    }
    
    public IFinalEvaluation getFinalEvaluation() {
    	return (IFinalEvaluation) getEvaluation();
    }
}
