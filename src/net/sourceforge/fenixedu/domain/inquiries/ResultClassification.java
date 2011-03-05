package net.sourceforge.fenixedu.domain.inquiries;

public enum ResultClassification {

    RED(true, true), YELLOW(true, true), GREEN(true, false), BLUE(true, false), PURPLE(true, true), GREY(false, false);

    private boolean relevantResult;
    private boolean mandatoryComment;

    private ResultClassification(boolean relevantResult, boolean mandatoryComment) {
	setRelevantResult(relevantResult);
	setMandatoryComment(mandatoryComment);
    }

    public boolean isRelevantResult() {
	return relevantResult;
    }

    public void setRelevantResult(boolean relevantResult) {
	this.relevantResult = relevantResult;
    }

    public void setMandatoryComment(boolean mandatoryComment) {
	this.mandatoryComment = mandatoryComment;
    }

    public boolean isMandatoryComment() {
	return mandatoryComment;
    }
}
