package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

public class ExamDateEntry {

    private String curricularCourseName;

    private String firstSeasonDate;

    private String firstSeasonHour;

    private String secondSeasonDate;

    private String secondSeasonHour;

    public ExamDateEntry() {

    }

    public ExamDateEntry(final String curricularCourseName, final String firstSeasonDate, final String firstSeasonHour,
	    final String secondSeasonDate, final String secondSeasonHour) {
	this.curricularCourseName = curricularCourseName;
	this.firstSeasonDate = firstSeasonDate;
	this.firstSeasonHour = firstSeasonHour;
	this.secondSeasonDate = secondSeasonDate;
	this.secondSeasonHour = secondSeasonHour;

    }

    public String getCurricularCourseName() {
	return curricularCourseName;
    }

    public void setCurricularCourseName(String curricularCourseName) {
	this.curricularCourseName = curricularCourseName;
    }

    public String getFirstSeasonDate() {
	return firstSeasonDate;
    }

    public void setFirstSeasonDate(String firstSeasonDate) {
	this.firstSeasonDate = firstSeasonDate;
    }

    public String getFirstSeasonHour() {
	return firstSeasonHour;
    }

    public void setFirstSeasonHour(String firstSeasonHour) {
	this.firstSeasonHour = firstSeasonHour;
    }

    public String getSecondSeasonDate() {
	return secondSeasonDate;
    }

    public void setSecondSeasonDate(String secondSeasonDate) {
	this.secondSeasonDate = secondSeasonDate;
    }

    public String getSecondSeasonHour() {
	return secondSeasonHour;
    }

    public void setSecondSeasonHour(String secondSeasonHour) {
	this.secondSeasonHour = secondSeasonHour;
    }

}
