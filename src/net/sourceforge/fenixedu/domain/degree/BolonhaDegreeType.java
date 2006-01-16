package net.sourceforge.fenixedu.domain.degree;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;

public enum BolonhaDegreeType {

    DEGREE(CurricularPeriodType.THREE_YEAR,3),

    MASTER_DEGREE(CurricularPeriodType.TWO_YEAR,2),

    INTEGRATED_MASTER_DEGREE(CurricularPeriodType.FIVE_YEAR, 5);

    private CurricularPeriodType curricularPeriodType;
    private int years;

    private BolonhaDegreeType(CurricularPeriodType curricularPeriodType, int years) {
        this.curricularPeriodType = curricularPeriodType;
        this.years = years;
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

}
