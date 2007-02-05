package net.sourceforge.fenixedu.domain;

public enum FrequencyType {

    DAILY("D"), WEEKLY("S"), BIWEEKLY("Q");

    private String abbreviation;
    
    private FrequencyType(String abbreviation_) {
	setAbbreviation(abbreviation_);
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
}
