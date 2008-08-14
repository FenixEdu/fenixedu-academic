package net.sourceforge.fenixedu.domain;

public enum FrequencyType {

    DAILY("D", 1), WEEKLY("S", 7), BIWEEKLY("Q", 14);

    private String abbreviation;

    private int numberOfDays;

    private FrequencyType(String abbreviation_, int ordinal) {
	setAbbreviation(abbreviation_);
	setNumberOfDays(ordinal);
    }

    public String getName() {
	return name();
    }

    public String getAbbreviation() {
	return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
	this.abbreviation = abbreviation;
    }

    public void setNumberOfDays(int ordinal) {
	this.numberOfDays = ordinal;
    }

    public int getNumberOfDays() {
	return numberOfDays;
    }
}
