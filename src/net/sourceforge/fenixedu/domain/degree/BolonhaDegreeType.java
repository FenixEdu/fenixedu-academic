package net.sourceforge.fenixedu.domain.degree;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;

public enum BolonhaDegreeType {

    DEGREE(CurricularPeriodType.THREE_YEAR,3,180),

    MASTER_DEGREE(CurricularPeriodType.TWO_YEAR,2,120),

    INTEGRATED_MASTER_DEGREE(CurricularPeriodType.FIVE_YEAR,5,300),
    
    ADVANCED_STUDIES_DIPLOMA(CurricularPeriodType.YEAR,1,30),
    
    ADVANCED_FORMATION_DIPLOMA(CurricularPeriodType.YEAR,1,30),
    
    SPECIALIZATION_DEGREE(CurricularPeriodType.YEAR,1,30);

    private CurricularPeriodType curricularPeriodType;
    private int years;
    private double defaultEctsCredits;

    private BolonhaDegreeType(CurricularPeriodType curricularPeriodType, int years, double defaultEctsCredits) {
        this.curricularPeriodType = curricularPeriodType;
        this.years = years;
        this.defaultEctsCredits = defaultEctsCredits;
    }

    public String getName() {
        return name();
    }

    public CurricularPeriodType getCurricularPeriodType() {
        return curricularPeriodType;
    }
    
    public int getYears() {
        return this.years;
    }

    public double getDefaultEctsCredits() {
        return defaultEctsCredits;
    }

}
