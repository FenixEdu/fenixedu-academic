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

	@Deprecated
	public java.util.Date getSubmitDate(){
		org.joda.time.YearMonthDay ymd = getSubmitDateYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setSubmitDate(java.util.Date date){
		if(date == null) setSubmitDateYearMonthDay(null);
		else setSubmitDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}

	@Deprecated
	public java.util.Date getWhenSubmited(){
		org.joda.time.YearMonthDay ymd = getWhenSubmitedYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setWhenSubmited(java.util.Date date){
		if(date == null) setWhenSubmitedYearMonthDay(null);
		else setWhenSubmitedYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}


}
