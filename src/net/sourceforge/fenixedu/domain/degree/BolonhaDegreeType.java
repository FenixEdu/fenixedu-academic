package net.sourceforge.fenixedu.domain.degree;

public enum BolonhaDegreeType {

    DEGREE (3),

    MASTER_DEGREE (2),

    INTEGRATED_MASTER_DEGREE (5);

    public String getName() {
        return name();
    }
    
    private int years;
    private BolonhaDegreeType(int years) {
        this.years = years;
    }
    
    public int getYears() {
        return this.years;
    }
}
